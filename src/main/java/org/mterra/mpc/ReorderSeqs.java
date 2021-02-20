package org.mterra.mpc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import java.io.File;
import java.io.IOException;

public class ReorderSeqs {

    private File dir;
    private Document document;

    private ReorderSeqs(File dir) {
        this.dir = dir;
        loadDoc();
        reorderSeqs();
    }

    public static void inPath(File file) {
        new ReorderSeqs(file);
    }

    private void loadDoc() {
        try {
            File allSeqs = new File(dir, "All Sequences & Songs.xal");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(allSeqs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void reorderSeqs() {
        // MPCVObject/AllSeqSamps/Songs
        document.getDocumentElement().normalize();

        Element all = (Element) document.getDocumentElement().getElementsByTagName("AllSeqSamps").item(0);
        Element songs = (Element) all.getElementsByTagName("Songs").item(0);
        Element song = (Element) songs.getElementsByTagName("Song").item(0);
        Element name = (Element) song.getElementsByTagName("Name").item(0);
        NodeList seqIdxs =  song.getElementsByTagName("SeqIndex");
        String songName = name.getTextContent();

        System.out.printf("Found song '%s' with '%s' sequences used%n", songName, seqIdxs.getLength());

    }



}
