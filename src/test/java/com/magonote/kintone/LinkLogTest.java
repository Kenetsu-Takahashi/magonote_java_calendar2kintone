package com.magonote.kintone;

import com.magonote.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * LinkLog Test
 */
public class LinkLogTest {
    private LinkLog log = new LinkLog();

    @BeforeAll
    static void setup() {
        Main.initialize();
    }

    @Test
    void initialize() {
        if(false) {
            boolean flag = log.initialize();

            Assertions.assertTrue(flag, "LinkLog initialize success");
        }
    }

    @Test
    void write() {
        boolean flag = log.initialize();

        Assertions.assertTrue(flag, "LinkLog initialize success");

        LinkLog.Info info = new LinkLog.Info();

        info.office="吉祥寺事業所";
        info.count = 12345;

        log.write(info);

        info.office="中野事業所";
        info.count = 4444;

        log.write(info);
    }

}
