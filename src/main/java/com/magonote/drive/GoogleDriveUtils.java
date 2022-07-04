package com.magonote.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.magonote.Main;
import com.magonote.environment.Config;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * Google Drive API Utilities
 */
public class GoogleDriveUtils {

    private static final String APPLICATION_NAME = "Google Drive API For Java";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // credentials  file root path
    private static final String CREDENTIALS_ROOT_FOLDER_PATH = String.format("%s/%s/%s", System.getenv("MAGONOTE_PROGRAM_PATH"), Main.MODULE_NAME, "config");

    // Directory to store user credentials for this application.
    private static java.io.File CREDENTIALS_FOLDER;

    private static final String CLIENT_SECRET_FILE_NAME = "credential.json";

    /**
     * Directory to store authorization tokens for this application.
     */
    private static java.io.File TOKENS_DIRECTORY_PATH;

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    // Global instance of the {@link FileDataStoreFactory}.
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    // Global instance of the HTTP transport.
    private static HttpTransport HTTP_TRANSPORT;

    private static Drive _driveService;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 認証情報取得
     *
     * @return 証情報
     * @throws IOException
     */
    public static Credential getCredentials() throws IOException {
        String mode = Config.getInstance().getExecuteMode();

        CREDENTIALS_FOLDER
                = new java.io.File(String.format("%s/%s", CREDENTIALS_ROOT_FOLDER_PATH, mode), "credentials");

        TOKENS_DIRECTORY_PATH = new java.io.File(CREDENTIALS_FOLDER, "tokens");

        DATA_STORE_FACTORY = new FileDataStoreFactory(TOKENS_DIRECTORY_PATH);

        java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);

        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME
                    + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
        }

        InputStream in = new FileInputStream(clientSecretFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

        return credential;
    }

    /**
     * Google Drive Service取得
     *
     * @return Google Drive Service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        if (_driveService != null) {
            return _driveService;
        }
        Credential credential = getCredentials();
        //
        _driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
                .setApplicationName(APPLICATION_NAME).build();
        return _driveService;
    }

}