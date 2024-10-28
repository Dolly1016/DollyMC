package jp.dreamingpig.dollyMC.utils;

import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import jp.dreamingpig.dollyMC.utils.serialization.DebugSerialization;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathHelperTest {
    @Test
    void integerOperationWorks() {
        //負数を考慮した整数の除算・剰余のテスト

        assertEquals(2, MathHelper.intDivide(6,3), "6 / 3 = 2");
        assertEquals(1, MathHelper.intDivide(5,3), "5 / 3 = 1");
        assertEquals(-1, MathHelper.intDivide(-1,3), "-1 / 3 = -1");
        assertEquals(-1, MathHelper.intDivide(-3,3), "-3 / 3 = -1");

        assertEquals(0, MathHelper.intSurplus(6,3), "6 % 3 = 0");
        assertEquals(2, MathHelper.intSurplus(5,3), "5 % 3 = 2");
        assertEquals(2, MathHelper.intSurplus(-1,3), "-1 % 3 = 2");
        assertEquals(0, MathHelper.intSurplus(-3,3), "-3 % 3 = 0");
    }
}
