/**
 * Stores and fetches data from a json file regarding build info
 */
package resources;

import buildtools.Build;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import org.eclipse.jetty.websocket.api.WebSocketBehavior;
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
    public void post(Build build) throws IOException {
        File db = new File(fileName);
        if (!db.exists()) {
            throw new IOException("Database file not found");
        } else {
            JSONObject dbJSON = parseJSON(fileName);
            JSONObject data = new JSONObject();
            data.put("status", build.getStatus());
            data.put("commitSha", build.getCommitSha());
            data.put("url", build.getUrl());
            data.put("log", build.getLog());
            dbJSON.put(build.getJobID(), data);

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
     * Fetches a specific commit from the data object.
     * @param id id of commit to get
     * @return object with given id or empty object if not found
     */
    public JSONObject get(String jobID) throws IOException {
        File db = new File(fileName);
        if (!db.exists()) {
            throw new IOException("Database file not found");
        } else {
            JSONObject dbJSON = parseJSON(fileName);

            // Try to get object, if it doesn't exist then return empty
            JSONObject rv;
            try {
                rv = dbJSON.getJSONObject(jobID);
            } catch (JSONException e) {
                rv = new JSONObject();
            }

            return rv;
        }
    }

    /**
     * Gets all the commits in the database
     * @return all commits as a JSONObject
     * @throws IOException if database file was not found
     */
    public JSONObject getAll() throws IOException {
        File db = new File(fileName);
        if (!db.exists()) {
            throw new IOException("Database file not found");
        } else {
            JSONObject dbJSON = parseJSON(fileName);
            
            return dbJSON;
        }
    }
}