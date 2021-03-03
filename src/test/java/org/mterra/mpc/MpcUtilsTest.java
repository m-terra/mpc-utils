package org.mterra.mpc;

import org.junit.jupiter.api.Test;

public class MpcUtilsTest extends BaseTest {

    @Test
    public void printHelp() throws Exception {
        String[] args = new String[]{"-h"};
        MpcUtils.main(args);
    }

}
