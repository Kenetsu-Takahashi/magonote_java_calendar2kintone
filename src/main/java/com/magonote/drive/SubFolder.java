package com.magonote.drive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SubFolder class
 */
public class SubFolder {
    /**
     * get folder list in root folder
     *
     * @param googleFolderIdParent
     * @return
     * @throws IOException
     */
    public static final List<File> getGoogleSubFolders(String googleFolderIdParent) throws IOException {
        Drive driveService = GoogleDriveUtils.getDriveService();

        String pageToken = null;
        List<File> list = new ArrayList<>();

        String query;
        if (googleFolderIdParent == null) {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + googleFolderIdParent + "' in parents";
        }

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime
                    .setFields("nextPageToken, files(id, name, createdTime)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

    /**
     * get folder list in root folder
     *
     * @return
     * @throws IOException
     */
    public static final List<File> getGoogleRootFolders() throws IOException {
        return getGoogleSubFolders(null);
    }

    /**
     * get csv file list in folder
     *
     * @param folderId
     * @return csv file list
     * @throws IOException
     */
    public static final List<File> getFilesInFolder(String folderId) throws IOException {
        Drive driveService = GoogleDriveUtils.getDriveService();

        List<File> files = new ArrayList<>();

        String query;

        if (folderId == null) {
            query = String.format("mimeType='%s' and 'root' in parents", "text/csv");
        } else {
            query = String.format("mimeType='%s' and '%s' in parents", "text/csv", folderId);
        }

        FileList result = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("nextPageToken, files(id, name)")
                .setPageToken(null)
                .execute();

        files.addAll(result.getFiles());

        return files;
    }
}
