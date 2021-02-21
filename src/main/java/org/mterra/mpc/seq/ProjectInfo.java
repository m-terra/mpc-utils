package org.mterra.mpc.seq;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class ProjectInfo {

    final Map<String, SeqInfo> seqInfoMap = new HashMap<>();
    Document document;
    NodeList seqNodeList;
    NodeList songSeqIdxNodeList;

    public void loadSeqsAndSong() {
        document.getDocumentElement().normalize();

        Element all = (Element) document.getDocumentElement().getElementsByTagName("AllSeqSamps").item(0);
        Element songs = (Element) all.getElementsByTagName("Songs").item(0);
        Element song = (Element) songs.getElementsByTagName("Song").item(0);
        Element name = (Element) song.getElementsByTagName("Name").item(0);
        songSeqIdxNodeList = song.getElementsByTagName("SeqIndex");
        String songName = name.getTextContent();

        Element seqs = (Element) all.getElementsByTagName("Sequences").item(0);
        seqNodeList = seqs.getElementsByTagName("Sequence");

        for (int i = 0; i < seqNodeList.getLength(); i++) {
            Element seqNode = (Element) seqNodeList.item(i);
            String seqNo = seqNode.getAttribute("number");
            Element nameEl = (Element) seqNode.getElementsByTagName("Name").item(0);
            seqInfoMap.put(seqNo, new SeqInfo(nameEl.getTextContent(), seqNo));
        }

        for (int i = 0; i < songSeqIdxNodeList.getLength(); i++) {
            Element songEl = (Element) songSeqIdxNodeList.item(i);
            String seqNo = songEl.getTextContent();
            seqInfoMap.get(String.valueOf(Integer.parseInt(seqNo) + 1)).posInSong.add(i);
        }

        System.out.printf("Found song '%s' with '%s' entries, total sequences '%s'%n",
                songName, songSeqIdxNodeList.getLength(), seqNodeList.getLength());
    }


}
