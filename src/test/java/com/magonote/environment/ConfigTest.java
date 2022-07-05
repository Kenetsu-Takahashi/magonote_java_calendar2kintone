package com.magonote.environment;

import com.magonote.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ConfigTest {
    @BeforeAll
    static void setup() {
        Main.initialize();
    }

    @Test
    void getExecuteMode() {
        String mode = Config.getInstance().getExecuteMode();
        Assertions.assertEquals("dev", mode, "getExecuteMode:mode = dev");
    }

    @Test
    void getAuth() {
        Map<String, String> map = Config.getInstance().getAuth();

        Assertions.assertEquals(3, map.size(), "getAuth: map.size = 3");

        Assertions.assertEquals("ictl-demo", map.get("domain"), "getAuth: domain = ictl-demo");
    }

    @Test
    void getOutputPath() {
        String path = Config.getInstance().getOutputPath();

        String expectedPath = "data";

        Assertions.assertEquals(expectedPath, path, "getOutputPath: path =  data");
    }

    @Test
    void getApps() {
        Map<String, Integer> map = Config.getInstance().getApps();

        Assertions.assertEquals((int) map.get("patient"), 885, "Patient Master Id  =  885");
        Assertions.assertEquals((int) map.get("operator"), 891, "Patient Master Id  =  891");
    }
}
