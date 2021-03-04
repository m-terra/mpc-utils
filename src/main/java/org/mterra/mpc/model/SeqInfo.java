package org.mterra.mpc.model;

import java.util.ArrayList;
import java.util.List;

public class SeqInfo {

    private final String name;
    private final String seqNumber;
    private final List<Integer> posInSong = new ArrayList<>();

    public SeqInfo(String name, String seqNumber) {
        this.name = name;
        this.seqNumber = seqNumber;
    }

    public String getName() {
        return name;
    }

    public String getSeqNumber() {
        return seqNumber;
    }

    public List<Integer> getPosInSong() {
        return posInSong;
    }

    public SeqInfo cloneWithSongPosition(Integer pos) {
        SeqInfo clone = new SeqInfo(name, seqNumber);
        clone.posInSong.add(pos);
        return clone;
    }

}
