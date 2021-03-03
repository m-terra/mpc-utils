package org.mterra.mpc;

import org.junit.jupiter.api.Test;

public class MpcUtilsTest extends BaseTest {

    @Test
    public void printHelp() {
        String[] args = new String[]{"-h"};
        MpcUtils.main(args);
    }

    @Test
    public void missingOption() {
        String[] args = new String[]{"-c", "reorder"};
        MpcUtils.main(args);
    }

    @Test
    public void missingArg() {
        String[] args = new String[]{"-c", "reorder", "-i"};
        MpcUtils.main(args);
    }

}
