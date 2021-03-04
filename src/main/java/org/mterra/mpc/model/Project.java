package org.mterra.mpc.model;

import org.mterra.mpc.util.Constants;
import org.mterra.mpc.util.Helper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Project {

    private ProjectInfo projectInfo;
    private Document document;

    public void load(ProjectInfo projectInfo) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(projectInfo.getProjectFile());
            this.projectInfo = projectInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getBpm() {
        NodeList bpms = document.getDocumentElement().getElementsByTagName("BPM");
        if (bpms.getLength() > 0) {
            return ((Element) bpms.item(0)).getTextContent();
        } else {
            throw new RuntimeException("BPM not found in project '" + projectInfo.getProjectName() + "'");
        }
    }

    public void setQLinkMode(String mode) {
        String xpathExpression = "/Project/QLinkAssignments/CurrentMode";
        NodeList nodeList = Helper.evaluateXPath(document, xpathExpression);
        nodeList.item(0).setTextContent(mode);
    }

    public void writeDocument(File targetDir) {
        File output = new File(targetDir, projectInfo.getProjectFile().getName());
        Helper.writeDocument(document, output);
    }

    public String getQLinkMode() {
        String xpathExpression = "/Project/QLinkAssignments/CurrentMode/text()";
        return Helper.evaluateXPathToStrings(document, xpathExpression).get(0);
    }

    public void setQLinkProjectTrackAssignement(Integer qLinkIndex, Integer trackIndex, String parameter) {
        String xpathExpression = "/Project/QLinkAssignments/ProjectMode/QLink[@index='" + qLinkIndex + "']";
        NodeList nodeList = Helper.evaluateXPath(document, xpathExpression);
        Element qlinkAssElement = (Element) nodeList.item(0);
        NodeList typeNodeList = qlinkAssElement.getElementsByTagName("Type");
        Element typeElement;
        if (typeNodeList.getLength() > 0) {
            typeElement = (Element) typeNodeList.item(0);

        } else {
            typeElement = document.createElement("Type");
            qlinkAssElement.appendChild(typeElement);
        }
        typeElement.setTextContent(Constants.QLINK_TYPE_MIDI_TRACK);
        typeElement.setAttribute("index", trackIndex.toString());
        qlinkAssElement.getElementsByTagName("Parameter").item(0).setTextContent(parameter);
    }

    public String getQLinkProjectAssignementParameter(Integer qLinkIndex) {
        String xpathExpression = "/Project/QLinkAssignments/ProjectMode/QLink[@index='" + qLinkIndex + "']/Parameter/text()";
        return Helper.evaluateXPathToStrings(document, xpathExpression).get(0);
    }

    public String getQLinkProjectAssignementType(Integer qLinkIndex) {
        String xpathExpression = "/Project/QLinkAssignments/ProjectMode/QLink[@index='" + qLinkIndex + "']/Type/text()";
        return Helper.evaluateXPathToStrings(document, xpathExpression).get(0);
    }

}
