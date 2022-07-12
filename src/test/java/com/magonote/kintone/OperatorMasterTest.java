package com.magonote.kintone;

import com.kintone.client.model.record.Record;
import com.magonote.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Test
    void getOffice(){
        OperatorMaster master = new OperatorMaster();

        String office = master.getOffice("000066");

        Assertions.assertEquals(office,"中野事業所","getOffice office = 中野事業所");
    }

    @Test
    void select(){
        OperatorMaster master = new OperatorMaster();

        Optional<List<Record>> records = master.select("");

        Assertions.assertTrue(records.isPresent());
    }
}
