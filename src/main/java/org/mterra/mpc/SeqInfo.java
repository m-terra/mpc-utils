package org.mterra.mpc;

import java.util.ArrayList;
import java.util.List;

public class SeqInfo {

    String name;
    String currentIdx;
    String newIdx;
    List<Integer> posInSong = new ArrayList<>();

    public SeqInfo(String name, String currentIdx) {
        this.name = name;
        this.currentIdx = currentIdx;
    }

}
