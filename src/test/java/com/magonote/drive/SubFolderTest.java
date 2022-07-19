package com.magonote.drive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.magonote.Handler;
import com.magonote.Main;
import com.magonote.environment.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class SubFolderTest {
    static Drive service;

    @BeforeAll
    static void setup() {
        Main.initialize();

        try {
            service = GoogleDriveUtils.getDriveService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getGoogleRootFolders() {
        if (false) {
            try {
                List<File> files = SubFolder.getGoogleRootFolders(service);

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
            File targetFile = null;

            try {

                // dev
                String folderId = "1reNVgRl1XWLPgyOJSkGnY_UdN8O6UOxB";

                // prod
                if (true) {
                    folderId = "1LoAMM_A3mgP3FFG6IxhkIHUrPwFTW-D5";
                }

                List<File> files = SubFolder.getFilesInFolder(service, folderId);

                for (File file : files) {
                    System.out.println(file.getName() + ": " + file.getId());

                    if (file.getName().contains("cal_events_1")) {
                        targetFile = file;
                    }
                }

                final String outputFile = String.format("%s/%s/data/%s", System.getenv("MAGONOTE_PROGRAM_PATH"), Main.MODULE_NAME, targetFile.getName());

                boolean flag = com.magonote.drive.Helper.downloadFile(service, targetFile.getId(), outputFile);

                Assertions.assertEquals(flag, true, "downloadFile is success");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
