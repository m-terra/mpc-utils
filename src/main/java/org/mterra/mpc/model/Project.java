package org.mterra.mpc.model;

import org.mterra.mpc.MpcUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Project {

    private String projectName;
    private Document document;

    public void load(File srcDir, String projectName) {
        try {
            File allSeqs = new File(srcDir, projectName + "." + MpcUtils.PROJ_SUFFIX);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(allSeqs);
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
