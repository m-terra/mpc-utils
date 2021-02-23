package org.mterra.mpc.seq;

import org.mterra.mpc.MpcUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MpcProject {

    final Map<Integer, SeqInfo> seqInfoMap = new TreeMap<>();
    private Document document;
    private Element seqs;
    private NodeList seqNodeList;
    private NodeList songSeqIdxNodeList;
    private Node sequenceNodeTemplate;

    public void loadSequencesAndSongs(File srcDir) {
        loadSequencesAndSongs(srcDir, "1");
    }

    public void loadSequencesAndSongs(File srcDir, String songNumber) {
        try {
            File allSeqs = new File(srcDir, MpcUtils.ALL_SEQS_FILE_NAME);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(allSeqs);
            loadElementsForSong(songNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changeSequenceIndexInSong(Integer position, String index) {
        Element songSeqIdx = (Element) songSeqIdxNodeList.item(position);
        songSeqIdx.setTextContent(index);
    }

    public void writeDocument(File targetDir) {
        try {
            File output = new File(targetDir, MpcUtils.ALL_SEQS_FILE_NAME);
            FileOutputStream outStream = new FileOutputStream(output);
            document.setXmlStandalone(true);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(outStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public NodeList getSeqNodeList() {
        return seqNodeList;
    }

    private void loadElementsForSong(String songNumber) {
        document.getDocumentElement().normalize();

        Element all = (Element) document.getDocumentElement().getElementsByTagName("AllSeqSamps").item(0);
        Element songs = (Element) all.getElementsByTagName("Songs").item(0);
        NodeList songsEls = songs.getElementsByTagName("Song");
        Element song = null;
        for (int i = 0; i < songsEls.getLength(); i++) {
            song = (Element) songsEls.item(i);
            if (song.getAttribute("number").equals(songNumber)) {
                break;
            }
        }

        if (song == null) {
            throw new RuntimeException("Song number " + songNumber + " not found");
        }

        Element name = (Element) song.getElementsByTagName("Name").item(0);
        songSeqIdxNodeList = song.getElementsByTagName("SeqIndex");
        String songName = name.getTextContent();

        seqs = (Element) all.getElementsByTagName("Sequences").item(0);
        seqNodeList = seqs.getElementsByTagName("Sequence");

        for (int i = 0; i < seqNodeList.getLength(); i++) {
            Element seqNode = (Element) seqNodeList.item(i);
            String seqNo = seqNode.getAttribute("number");
            Element nameEl = (Element) seqNode.getElementsByTagName("Name").item(0);
            seqInfoMap.put(Integer.parseInt(seqNo), new SeqInfo(nameEl.getTextContent(), seqNo));
        }

        for (int i = 0; i < songSeqIdxNodeList.getLength(); i++) {
            Element songEl = (Element) songSeqIdxNodeList.item(i);
            String seqNo = songEl.getTextContent();
            seqInfoMap.get(Integer.parseInt(seqNo) + 1).getPosInSong().add(i);
        }

        System.out.printf("Found song '%s' with '%s' entries, total sequences '%s'%n",
                songName, songSeqIdxNodeList.getLength(), seqNodeList.getLength());
    }

    public void removeAllSequences() {
        List<Node> toRemove = new ArrayList<>();
        for (int i = 0; i < seqs.getChildNodes().getLength(); i++) {
            Node node = seqs.getChildNodes().item(i);
            if (!(node.getNodeType() == Node.ELEMENT_NODE && ((Element) node).getTagName().equals("Count"))) {
                toRemove.add(node);
            }
            if (node.getNodeType() == Node.ELEMENT_NODE && ((Element) node).getTagName().equals("Sequence")) {
                sequenceNodeTemplate = node;
            }
        }
        for (Node node : toRemove) {
            seqs.removeChild(node);
        }
    }

    public void addSequence(Integer number, String name) {
        Element seq = (Element) sequenceNodeTemplate.cloneNode(true);
        seq.setAttribute("number", number.toString());
        Element nameEl = (Element) seq.getElementsByTagName("Name").item(0);
        nameEl.setTextContent(name);
        seqs.appendChild(seq);
    }
}
