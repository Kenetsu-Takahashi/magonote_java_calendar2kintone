package com.magonote.csv;

import com.magonote.Main;
import com.magonote.kintone.OperationSchedule;
import com.magonote.kintone.OperatorMaster;
import com.magonote.kintone.PatientMaster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ScheduleFileTest {
    @BeforeAll
    static void setup() {
        Main.initialize();
    }

    @Test
    void getOperator() {
        final String path = "E:\\Magonote\\program\\schedule\\data\\cal_events_4.csv";

        OperatorMaster operatorMaster = new OperatorMaster();

        Map<String, String> operatorIdMap = operatorMaster.getIdMap();

        PatientMaster patientMaster = new PatientMaster();

        Map<String, String> patientIdMap = patientMaster.getIdMap();

        ScheduleFile scheduleFile = new ScheduleFile(path, operatorIdMap, patientIdMap);

        String firstOperatorId = scheduleFile.getFirstOperator();

        Assertions.assertEquals(firstOperatorId, "000066", "firstOperatorId = 000066");

        List<OperationSchedule.Schedule> schedules = scheduleFile.getScheduleList();

        Assertions.assertEquals(schedules.size(), 3, "getScheduleList size = 3");
    }
}
