package com.magonote.drive;

import com.google.api.services.drive.Drive;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Helper {

    /**
     * donwload form Google Drive and write to local file
     * @param service Google Drive Service
     * @param fileId fileId
     * @param localFile output file
     * @throws IOException
     */
    public static boolean downloadFile(final Drive service, final String fileId, final String localFile) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        service.files().get(fileId).executeMediaAndDownloadTo(outputStream);

        byte[] content = outputStream.toByteArray();

        try (FileOutputStream fileOutputStream = new FileOutputStream(localFile)) {
            fileOutputStream.write(content);
         }

        return Files.exists(Paths.get(localFile));
    }
}
