package org.mterra.mpc.model;

import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.util.Helper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Project {

    private ProjectInfo projectInfo;
    private Document documentProject;
    private Document documentProjectSettings;

    public void load(ProjectInfo projectInfo) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            documentProject = db.parse(projectInfo.getProjectFile());
            if (projectInfo.hasProjectSettingsFile()) {
                documentProjectSettings = db.parse(projectInfo.getProjectSettingsFile());
            }
            this.projectInfo = projectInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getBpm() {
        NodeList bpms = documentProject.getDocumentElement().getElementsByTagName("MasterTempo.Value");
        if (bpms.getLength() > 0) {
            return bpms.item(0).getTextContent();
        } else {
            throw new RuntimeException("BPM not found in project '" + projectInfo.getProjectName() + "'");
        }
    }

    public void setQLinkMode(String mode) {
        String xpathExpression = "/Project/QLinkAssignments/CurrentMode";
        NodeList nodeList = Helper.evaluateXPath(documentProject, xpathExpression);
        nodeList.item(0).setTextContent(mode);
    }

    public void writeProjectDocument(File targetDir) {
        File output = new File(targetDir, projectInfo.getProjectFile().getName());
        Helper.writeDocument(documentProject, output);
    }

    public void writeProjectSettingsDocument(File targetDir) {
        File outputData = new File(targetDir, projectInfo.getProjectDataFolder().getName());
        File output = new File(outputData, projectInfo.getProjectSettingsFile().getName());
        Helper.writeDocument(documentProjectSettings, output);
    }

    public String getQLinkMode() {
        String xpathExpression = "/Project/QLinkAssignments/CurrentMode/text()";
        return Helper.evaluateXPathToStrings(documentProject, xpathExpression).get(0);
    }

    public void setQLinkAssignment(Integer qLinkIndex, String type, Integer typeIndex, String parameter, boolean momentary) {
        String xpathExpression = "/Project/QLinkAssignments/ProjectMode/QLink[@index='" + qLinkIndex + "']";
        NodeList nodeList = Helper.evaluateXPath(documentProject, xpathExpression);
        Element qlinkAssElement = (Element) nodeList.item(0);
        NodeList typeNodeList = qlinkAssElement.getElementsByTagName("Type");
        if (type != null) {
            Element typeElement;
            if (typeNodeList.getLength() > 0) {
                typeElement = (Element) typeNodeList.item(0);
            } else {
                typeElement = documentProject.createElement("Type");
                qlinkAssElement.appendChild(typeElement);
            }
            typeElement.setTextContent(type);
            typeElement.setAttribute("index", typeIndex.toString());
        } else {
            for (int i = 0; i < typeNodeList.getLength(); i++) {
                Element nodeType = (Element) typeNodeList.item(i);
                qlinkAssElement.removeChild(nodeType);
            }
        }
        qlinkAssElement.getElementsByTagName("Parameter").item(0).setTextContent(parameter);
        NodeList momentaryNodeList = qlinkAssElement.getElementsByTagName("Momentary");
        Element momentaryElement;
        if (momentaryNodeList.getLength() > 0) {
            momentaryElement = (Element) momentaryNodeList.item(0);
        } else {
            momentaryElement = documentProject.createElement("Momentary");
            qlinkAssElement.appendChild(momentaryElement);
        }
        momentaryElement.setTextContent(momentary ? "1" : "0");
    }

    public String getQLinkProjectAssignmentParameter(Integer qLinkIndex) {
        String xpathExpression = "/Project/QLinkAssignments/ProjectMode/QLink[@index='" + qLinkIndex + "']/Parameter/text()";
        return Helper.evaluateXPathToStrings(documentProject, xpathExpression).get(0);
    }

    public String getQLinkProjectAssignmentType(Integer qLinkIndex) {
        String xpathExpression = "/Project/QLinkAssignments/ProjectMode/QLink[@index='" + qLinkIndex + "']/Type/text()";
        return Helper.evaluateXPathToStrings(documentProject, xpathExpression).get(0);
    }

    public String getMasterEqInsertIndex() {
        String xpathExpression = "/Project/Mixer/Mixer.Output/*[Name='AIR Para EQ']";
        NodeList res = Helper.evaluateXPath(documentProject, xpathExpression);
        if (res.getLength() > 0) {
            return StringUtils.substringAfter(res.item(0).getNodeName(), "Insert");
        } else {
            return null;
        }
    }

    public void setArpLiveSettings() {
        if (documentProjectSettings != null) {
            setSettingsValue("Quantiser.TimeDivision", "1/4");
            setSettingsValue("Quantiser.Enabled", "1");
            setSettingsValue("Arpeggiator.Latch", "1");
            setSettingsValue("Arpeggiator.ArpIndex", "Up");
            setSettingsValue("Arpeggiator.StepSize", "960");
        }
    }

    public boolean isArpLatched() {
        String xpathExpression = "/settings/VALUE[@name='Arpeggiator.Latch']/@val";
        NodeList nodeList = Helper.evaluateXPath(documentProjectSettings, xpathExpression);
        return nodeList.getLength() == 0 || nodeList.item(0).getTextContent().equals("1");
    }

    private void setSettingsValue(String name, String value) {
        String xpathExpression = "/settings/VALUE[@name='" + name + "']";
        NodeList nodeList = Helper.evaluateXPath(documentProjectSettings, xpathExpression);
        if (nodeList.getLength() > 0) {
            ((Element) nodeList.item(0)).setAttribute("val", value);
        }
    }

}
