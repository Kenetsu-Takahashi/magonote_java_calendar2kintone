package com.magonote.kintone;

import com.magonote.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class OperatorMasterTest {
    @BeforeAll
    static void setup() {
        Main.initialize();
    }

    @Test
    void initialize() {
        OperatorMaster master = new OperatorMaster();

        Map<String, String> map = master.getIdMap();

        Assertions.assertEquals(map.size(), 60, "Operator Master size =  60");
    }
}
