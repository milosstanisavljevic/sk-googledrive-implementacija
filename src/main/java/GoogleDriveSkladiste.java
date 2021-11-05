import java.util.List;
import java.util.Map;

public class GoogleDriveSkladiste extends SpecifikacijaSkladista {

    static {

        Manager.registerImpl(new GoogleDriveSkladiste());
    }

    public boolean createRoot(String s, String s1, String s2) {
        return false;
    }

    public boolean checkIfRootExists(String s) {
        return false;
    }

    @Override
    public void createFile(String s, String s1) {

    }

    @Override
    public void createMoreFiles(String s, int i) {

    }

    @Override
    public void createMoreFolders(String s, int i) {

    }

    @Override
    public void createFolder(String s, String s1) {

    }

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

    public void createFile(String s) {

    }

    public void createMoreFiles(int i) {

    }

    public void createMoreFolders(int i) {

    }

    public void createFolder(String s) {

    }

    public void deleteFile(String s) {

    }

    public void deleteFolder(String s) {

    }

    public void loadUsers() {

    }
}
