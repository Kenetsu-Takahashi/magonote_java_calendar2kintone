package com.magonote;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Handler Test
 */
public class HandlerTest {
    @BeforeAll
    static void setup() {
        Main.initialize();
    }

    @Test
    void execute() {
        if(true) {
            Handler handler = new Handler();

            boolean flag = handler.execute();

            Assertions.assertTrue(flag, "Handler execute success");
        }
    }
}
