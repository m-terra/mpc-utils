package org.mterra.mpc.seq;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class ProjectInfo {

    final Map<Integer, SeqInfo> seqInfoMap = new TreeMap<>();
    Document document;
    Element seqs;
    NodeList seqNodeList;
    NodeList songSeqIdxNodeList;
    Node sequenceNodeTemplate;

    public void loadSeqsAndSong() {
        document.getDocumentElement().normalize();

        Element all = (Element) document.getDocumentElement().getElementsByTagName("AllSeqSamps").item(0);
        Element songs = (Element) all.getElementsByTagName("Songs").item(0);
        Element song = (Element) songs.getElementsByTagName("Song").item(0);
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
            seqInfoMap.get(Integer.parseInt(seqNo) + 1).posInSong.add(i);
        }

        System.out.printf("Found song '%s' with '%s' entries, total sequences '%s'%n",
                songName, songSeqIdxNodeList.getLength(), seqNodeList.getLength());
    }

    public Element getSequenceNodeByNumber(String number) {
        for (int i = 0; i < seqNodeList.getLength(); i++) {
            Element seqNode = (Element) seqNodeList.item(i);
            String seqNo = seqNode.getAttribute("number");
            if (Objects.equals(number, seqNo)) {
                return seqNode;
            }
        }
        throw new RuntimeException("SequenceNode with number '" + number + "' not found");
    }

    public void removeAllSequences() {
        List<Node> toRemove = new ArrayList<>();
        for (int i = 0; i < seqNodeList.getLength(); i++) {
            toRemove.add(seqNodeList.item(i));
        }
        sequenceNodeTemplate = toRemove.get(0);
        for (Node node : toRemove) {
            seqs.removeChild(node);
        }
    }

    public void addSequence(String number, String name) {
        Element seq = (Element) sequenceNodeTemplate.cloneNode(true);
        seq.setAttribute("number", number);
        Element nameEl = (Element) seq.getElementsByTagName("Name").item(0);
        nameEl.setTextContent(name);
        seqs.appendChild(seq);
    }
}
