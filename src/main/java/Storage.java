/**
 * Stores and fetches data from a json file regarding build info
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
 
import org.json.JSONArray;
import org.json.JSONObject;

public class Storage {
    /**
     * Creates a new json data object if none exists already
     */
    public Storage() {
        // Check if database already exists
        File db = new File("db.json");
        if (!db.exists()) {
            JSONObject data = new JSONObject();

            // Write to JSON file
            try (FileWriter file = new FileWriter("db.json")) {
                file.write(data.toString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a new commit to the data object
     */
    public void post() {
        
    }

    /**
     * Fetches a specific commit from the data object
     */
    public void get() {

    }
}