package com.magonote.drive;

import com.google.api.services.drive.model.File;
import com.magonote.Main;
import com.magonote.environment.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SubFolderTest {
    @BeforeAll
    static void setup() {
        Main.initialize();
    }

    @Test
    void getGoogleRootFolders() {
        if(false) {
            try {
                List<File> files = SubFolder.getGoogleRootFolders();

                files.stream().forEach((x) -> {
                    System.out.println(x.getName() + ": " + x.getId());
                });
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Test
    void getFilesInFolder() {
        if (true) {
            try {
                final String folderId = "1reNVgRl1XWLPgyOJSkGnY_UdN8O6UOxB";

                List<File> files = SubFolder.getFilesInFolder(folderId);

                files.stream().forEach((x) -> {
                    System.out.println(x.getName() + ": " + x.getId());
                });
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
