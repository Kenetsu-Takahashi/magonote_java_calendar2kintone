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
        OperationSchedule schedule = new OperationSchedule();

        List<OperationSchedule.Info> infoList = new ArrayList<>();

        OperationSchedule.Info info = new OperationSchedule.Info();

        info.startTime = "2022-07-08T06:00:00.000Z";
        info.endTime = "2022-07-08T07:00:00.000Z";
        info.operatorId = "000067 髙橋 健悦";
        info.patientId = "14199 會田　武夫";
        info.address = "岩手県花巻市";
        info.allDay = "終日";
        info.content = "123456 xxxxxxy uuuuu";
        info.status = "仮押さえ";

        infoList.add(info);

        schedule.write(infoList);
    }
}
