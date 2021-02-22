package org.mterra.mpc.seq;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeqInfo {

    private final String name;
    private final String currentIdx;
    private final List<Integer> posInSong = new ArrayList<>();

    public SeqInfo(String name, String currentIdx) {
        this.name = name;
        this.currentIdx = currentIdx;
    }

    public String getName() {
        return name;
    }

    public String getCurrentIdx() {
        return currentIdx;
    }

    public List<Integer> getPosInSong() {
        return posInSong;
    }

    public boolean needsMoving(Integer newIdx) {
        return !Objects.equals(currentIdx, newIdx.toString());
    }

}
