package org.mterra.mpc.seq;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeqInfo {

    String name;
    String currentIdx;
    String newIdx;
    List<Integer> posInSong = new ArrayList<>();

    public SeqInfo(String name, String currentIdx) {
        this.name = name;
        this.currentIdx = currentIdx;
    }

    public boolean needsMoving() {
        return !Objects.equals(currentIdx, newIdx);
    }

}
