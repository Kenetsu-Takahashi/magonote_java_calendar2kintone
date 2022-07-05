package com.magonote.kintone;

import com.magonote.Main;
import com.magonote.environment.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * PatientMaster Test
 */
public class PatientMasterTest {
    @BeforeAll
    static void setup() {
        Main.initialize();
    }

    @Test
    void initialize() {
        PatientMaster master = new PatientMaster();

        Map<String, String> map = master.getIdMap();

        Assertions.assertEquals(map.size(), 138, "Patient Master size =  138");
    }
}
