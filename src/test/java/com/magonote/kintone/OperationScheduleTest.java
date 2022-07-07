package com.magonote.kintone;

import com.magonote.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OperationSchedule Test
 */
public class OperationScheduleTest {
    @BeforeAll
    static void setup() {
        Main.initialize();
    }

    @Test
    void write() {
        OperationSchedule operationSchedule = new OperationSchedule();

        List<OperationSchedule.Schedule> schedules = new ArrayList<>();

        OperationSchedule.Schedule schedule = new OperationSchedule.Schedule();

        schedule.startTime = "2022-07-08T06:00:00.000Z";
        schedule.endTime = "2022-07-08T07:00:00.000Z";
        schedule.operatorLookupId = "000067 髙橋 健悦";
        schedule.patientLookupId= "014199 會田　武夫";
        schedule.address = "岩手県花巻市";
        schedule.allDay = "終日";
        schedule.content = "123456 xxxxxxy uuuuu";
        schedule.status = "仮押さえ";

        schedules.add(schedule);

        boolean flag = operationSchedule.write(schedules);

        Assertions.assertTrue(flag,"OperationScheduleTest write true");

    }
}
