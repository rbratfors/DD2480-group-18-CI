/**
 * Stores and fetches data from a json file regarding build info
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
 
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Storage {
    String fileName = "db.json";
    /**
     * Creates a new json data object if none exists already
     */
    public Storage() {
        // Check if database already exists
        File db = new File("db.json");
        if (!db.exists()) {
            JSONObject data = new JSONObject();

            // Write to JSON file
            try (FileWriter file = new FileWriter(fileName)) {
                file.write(data.toString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Taken from https://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
     * @param file name of file name
     * @return json object of the file given
     * @throws JSONException
     * @throws IOException
     */
    private JSONObject parseJSON(String file) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(file)));
        return new JSONObject(content);
    }

    /**
     * Adds a new commit to the data object
     * @param id id of the commit
     * @param passed checks passed
     * @param header commit header
     * @param body commit message
     */
    public void post(String id, boolean passed, String header, String body) throws IOException {
        File db = new File(fileName);
        if (!db.exists()) {
            throw new IOException("Database file not found");
        } else {
            JSONObject dbJSON = parseJSON(fileName);
            JSONObject data = new JSONObject();
            data.put("Passed", passed);
            data.put("Header", header);
            data.put("Body", body);
            dbJSON.put(id, data);

            // Write to JSON file
            try (FileWriter file = new FileWriter(fileName)) {
                file.write(dbJSON.toString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fetches a specific commit from the data object
     */
    public void get() {

    }
}