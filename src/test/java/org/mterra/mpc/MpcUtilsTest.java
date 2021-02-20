package org.mterra.mpc;

import org.mterra.mpc.MpcUtils;
import org.junit.jupiter.api.Test;


public class MpcUtilsTest {

    @Test
    public void reorderSeqs() throws Exception {
        String[] args = new String[]{"reorderSeqs", "./src/test/resources", "./target"};
        MpcUtils.main(args);
    }

}
