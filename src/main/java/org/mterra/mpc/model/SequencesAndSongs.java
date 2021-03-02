package org.mterra.mpc.model;

import org.mterra.mpc.MpcUtils;
import org.mterra.mpc.util.Helper;
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

public class SequencesAndSongs {

    private final Map<Integer, SeqInfo> seqInfoMap = new TreeMap<>();
    private Document document;
    private Element seqs;
    private NodeList seqNodeList;
    private NodeList songSeqIdxNodeList;
    private Node sequenceNodeTemplate;

    public void load(File srcDir) {
        load(srcDir, "1");
    }

    public void load(File srcDir, String songNumber) {
        try {
            File allSeqs = new File(srcDir, MpcUtils.ALL_SEQS_FILE_NAME);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(allSeqs);
            loadSong(songNumber);
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

    public Map<Integer, SeqInfo> getSeqInfoMap() {
        return seqInfoMap;
    }

    private void loadSong(String songNumber) {
        String xpathExpression = "/MPCVObject/AllSeqSamps/Songs/Song[@number='" + songNumber + "']";
        NodeList songNodeList = Helper.evaluateXPath(document, xpathExpression);
        if (songNodeList.getLength() == 0) {
            throw new RuntimeException("Song number " + songNumber + " not found");
        }
        Element song = (Element) songNodeList.item(0);
        Element name = (Element) song.getElementsByTagName("Name").item(0);
        songSeqIdxNodeList = song.getElementsByTagName("SeqIndex");
        String songName = name.getTextContent();

        Element all = (Element) document.getDocumentElement().getElementsByTagName("AllSeqSamps").item(0);
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

        System.out.printf("Loaded song '%s' with '%s' entries, total sequences '%s'%n",
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

    public boolean containsSequence(String name) {
        String xpathExpression = "/MPCVObject/AllSeqSamps/Sequences/Sequence[Name='" + name + "']/@number";
        List<String> res = Helper.evaluateXPathToStrings(document, xpathExpression);
        return res.size() > 0;
    }

}
