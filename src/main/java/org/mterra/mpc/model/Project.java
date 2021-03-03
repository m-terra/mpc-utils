package org.mterra.mpc.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Project {

    private String projectName;
    private Document document;

    public void load(ProjectInfo projectInfo) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(projectInfo.getProjectFile());
            this.projectName = projectName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getBpm() {
        NodeList bpms = document.getDocumentElement().getElementsByTagName("BPM");
        if (bpms.getLength() > 0) {
            return ((Element) bpms.item(0)).getTextContent();
        } else {
            throw new RuntimeException("BPM not found in project '" + projectName + "'");
        }
    }
}
