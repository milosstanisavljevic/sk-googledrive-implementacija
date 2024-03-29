package sk;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.gson.Gson;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GoogleDriveSkladiste extends SpecifikacijaSkladista {

    private int br = 0;
    private int brojac = 0;
    private String root;
    private String name;
    private Drive service;
    private String connectedUser;

    static {

        Manager.registerImpl(new GoogleDriveSkladiste());
    }
    private Drive cRoot(){
        try {
            Drive drive = getDriveService();
            return drive;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean createRoot(String pathId, String name, String username, String password) {
        service = cRoot();
        this.name = name;
        File file = new File();
        file.setName(name);
        file.setMimeType("application/vnd.google-apps.folder");

        try {
            makeCopy(pathId);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try {
            file =service.files().create(file).setFields("id").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        root = file.getId();
        makeDefaultConfig(pathId, username);
        makeDefaultUser(pathId, username, password);
        System.out.println("Folder ID: " + root);

        return true;
    }

    private void makeCopy(String id) throws IOException {
        java.io.File f = new java.io.File("MyRoots");
        if(!f.exists()){
            f.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(f, true);

        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(id + "\\" + name + "\n");
        bufferedWriter.close();
    }

    public boolean checkIfRootExists(String id, String name) {
        try {
            java.io.File file = new java.io.File("MyRoots");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                if((line + "\\" + name).equalsIgnoreCase(id)){
                    System.out.println(line);
                    System.out.println(id);
                    return true;
                }
            }
            fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createFile(String id, String name) {

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
        return true;

    }

    @Override
    public String getPath() {
        return root;
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
    public boolean createFolder(String s, String s1) {

        File f = null;
        File file = new File();
        file.setName(s1);
        file.setMimeType("application/vnd.google-apps.folder");
        try {
             f = getDriveService().files().create(file).setFields("id").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Folder ID: " + f.getId());
        //dodati throws
        return true;
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
    public boolean deleteFile(String fileId, String s1) {
        try {
            getDriveService().files().delete(fileId).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return true;
    }

    @Override
    public boolean deleteFolder(String fileId, String s1) {

        try {
            getDriveService().files().delete(fileId).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return true;
    }

    @Override
    public boolean moveFromTo(String s, String s1, String s2) {
        return false;
    }

    public boolean downloadFile(String s, String s1) {
        return false;
    }

    public boolean copyPasteFiles(String s, String s1, String s2) {
        return false;
    }

    public int countFiles() {
        return 0;
    }

    public long countFilesMemory() {
        return 0;
    }

    public boolean downloadFile(String fileId) {
        //String fileId = "0BwwA4oUTeiV1UVNwOHItT0xfa2M";
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            getDriveService().files().get(fileId)
                    .executeMediaAndDownloadTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void makeConfig(String path, Map<String, Object> map) {
        try {
            java.io.File f  = new java.io.File(path + "/" + "config.json");
            Writer writer = new FileWriter(f.getAbsolutePath());
            new Gson().toJson(map, writer);
            writer.close();
            uploadFile(f, "config.json");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void makeDefaultConfig(String id, String username) {
        Map<String, Object> map = mapConfig(1000000, ".json", 10, username);
        makeConfig(id,map);
    }

    @Override
    public boolean updateConfig(String s, int i, String s1, int i1) {
        return false;
    }

    @Override
    public void makeUser(String path, List<Korisnik> korisnici) {
        try {
            java.io.File f  = new java.io.File(path + "/" + "config.json");
            Writer writer = new FileWriter(f.getAbsolutePath());
            new Gson().toJson(korisnici,writer);
            writer.close();
            uploadFile(f, "users.json");

        }catch (Exception e){
            e.printStackTrace();
        }

       // new Gson().toJson(korisnici,writer);
    }

    @Override
    public void makeDefaultUser(String path, String username, String password) {
        try {
            List<Korisnik> korisnici = loadUsers(username, password, true, true, true, true);
            makeUser(path, korisnici);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addUser(String s, String s1, String s2, String s3) {
        return true;
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
    public boolean checkUser(String id, String username1, String password1) {
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
    private void uploadFile(java.io.File filePath, String name)throws Exception{
        AbstractInputStreamContent aisc = new FileContent(null,filePath);
        File fileMetadata = new File();

        fileMetadata.setName(name);
        fileMetadata.setParents(Collections.singletonList(root));

        getDriveService().files().create(fileMetadata,aisc).setFields("id, webContentLink, webViewLink, parents").execute();

    }

}
