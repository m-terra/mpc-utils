package org.mterra.mpc.seq;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public boolean needsMoving(Integer newIdx) {
        return !Objects.equals(seqNumber, newIdx.toString());
    }

}
