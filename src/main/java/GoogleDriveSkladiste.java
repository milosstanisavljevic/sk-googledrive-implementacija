import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GoogleDriveSkladiste extends SpecifikacijaSkladista {

    private int br = 0;
    private int brojac = 0;
    static {

        Manager.registerImpl(new GoogleDriveSkladiste());
    }

    public boolean createRoot(String s, String s1, String s2) {
        return false;
    }

    public boolean checkIfRootExists(String s) {
        //file.exist ne postoji
        return false;
    }

    @Override
    public void createFile(String id, String name) {

        //String folderId = "0BwwA4oUTeiV1TGRPeTVjaWRDY1E";
        File fileMetadata = new File();
        fileMetadata.setName("photo.jpg");
        fileMetadata.setParents(Collections.singletonList(id));
        java.io.File filePath = new java.io.File("files/photo.jpg");
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = null;
        try {
            file = getDriveService().files().create(fileMetadata, mediaContent)
                    .setFields("id, parents")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File ID: " + file.getId());

    }

    @Override
    public void createMoreFiles(String s, int i, String s1) {
        //moze u specifikaciju
    }


    @Override
    public void createMoreFolders(String s, int i) {
        //isto kao lokoalnom, moze u spec
    }

    @Override
    public void createFolder(String s, String s1) {

        File f = null;
        File file = new File();
        file.setName(s1);
        file.setMimeType("application/vnd.google-apps.folder");
        try {
             f = getDriveService().files().create(file).setFields(s).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Folder ID: " + f.getId());
        //dodati throws
    }

    /** POMERANJE FAJLA IZ FOLDERA U FOLDER
     *
     *
     * String fileId = "1sTWaJ_j7PkjzaBWtNc3IzovK5hQf21FbOw9yLeeLPNQ";
     * String folderId = "0BwwA4oUTeiV1TGRPeTVjaWRDY1E";
     * // Retrieve the existing parents to remove
     * File file = driveService.files().get(fileId)
     *     .setFields("parents")
     *     .execute();
     * StringBuilder previousParents = new StringBuilder();
     * for (String parent : file.getParents()) {
     *   previousParents.append(parent);
     *   previousParents.append(',');
     * }
     * // Move the file to the new folder
     * file = driveService.files().update(fileId, null)
     *     .setAddParents(folderId)
     *     .setRemoveParents(previousParents.toString())
     *     .setFields("id, parents")
     *     .execute();
     *
     *
     */

    @Override
    public void deleteFile(String s, String s1) {

    }

    @Override
    public void deleteFolder(String s, String s1) {

    }

    @Override
    public void makeConfig(String s, Map<String, Object> map) {

    }

    @Override
    public void makeDefaultConfig(String s, String s1) {

    }

    @Override
    public void updateConfig(String s, int i, String s1, int i1) {

    }

    @Override
    public void makeUser(String s, List<Korisnik> list) {

    }

    @Override
    public void makeDefaultUser(String s, String s1, String s2) {

    }

    @Override
    public void addUser(String s, String s1, String s2, String s3) {

    }

    @Override
    public String checkAdmin(String s) {
        return null;
    }

    @Override
    public Object checkConfigType(String s, String s1) {
        return null;
    }

    @Override
    public boolean checkUser(String s, String s1, String s2) {
        return false;
    }

    public void loadUsers() {

    }
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "My project";

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = GoogleDriveSkladiste.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     *
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws IOException {

        Drive service = getDriveService();

//        FileList result = service.files().list()
//                .setPageSize(10)
//                .setFields("nextPageToken, files(id, name)")
//                .execute();
////        List<File> files = result.getFiles();
//        if (files == null || files.isEmpty()) {
//            System.out.println("No files found.");
//        } else {
//            System.out.println("Files:");
//            for (File file : files) {
//                System.out.printf("%s (%s)\n", file.getName(), file.getId());
//            }
//        }
    }
}
