package com.magonote.kintone;

import com.magonote.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * LinkLog Test
 */
public class LinkLogTest {
    private LinkLog linkLog = new LinkLog();

    @BeforeAll
    static void setup() {
        Main.initialize();
    }

    @Test
    void initialize() {
        if(false) {
            boolean flag = linkLog.initialize();

            Assertions.assertTrue(flag, "LinkLog initialize success");
        }
    }

    @Test
    void write() {
        boolean flag = linkLog.initialize();

        Assertions.assertTrue(flag, "LinkLog initialize success");

        LinkLog.Log log = new LinkLog.Log();

        log.office="吉祥寺事業所";
        log.count = 12345;

        linkLog.write(log);

        log.office="中野事業所";
        log.count = 4444;

        linkLog.write(log);
    }

}
