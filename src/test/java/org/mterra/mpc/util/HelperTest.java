package org.mterra.mpc.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.BaseTest;

public class HelperTest extends BaseTest {

    @Test
    public void nonExistingDir() {
        Assertions.assertThrows(RuntimeException.class, () -> Helper.getProjectsInDirectory("./src/test/resources/non-existing"));
    }

}
