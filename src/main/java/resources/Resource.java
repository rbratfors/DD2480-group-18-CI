package resources;

import buildtools.Build;

import org.json.JSONArray;
import buildtools.BuildJob;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("ci")
public class Resource {
    Storage storage = new Storage();

    /**
     * Creates a Build object from a given json object and its key (jobID)
     * @param key jobID
     * @param o rest of keys
     * @return Build object created from given json
     */
    private Build json2Build(String key, JSONObject o) throws IOException {
        String jobID = key;
        String commitSha = o.getString("commitSha");
        String url = o.getString("url");
        JSONArray log = o.getJSONArray("log");

        // fix status
        String tempStatus = o.getString("status");
        Build.Result status;
        switch (tempStatus) {
            case "success":
                status = Build.Result.success;
                break;
            case "pending":
                status = Build.Result.pending;
                break;
            case "failure":
                status = Build.Result.failure;
                break;
            case "error":
                status = Build.Result.error;
                break;
            default:
                throw new IOException("Invalid 'status' in database");
        }

        Build b = new Build(jobID, status, commitSha, url, log);

        return b;
    }

    /**
     * Fetches all builds from local database
     * path: /ci/get
     * @return
     * @throws IOException
     */

    ExecutorService jobsQueue = Executors.newFixedThreadPool(10);

    // A list of of builds is sent by returning List<Build> instead.
    // TODO
    // load a previous build from storage
    // load multiple previous builds from storage
    @GET
    @Path("get")
    @Produces("application/json")
    public ArrayList<Build> getBuilds() throws IOException {
        JSONObject dbJSON = storage.getAll();
        ArrayList<Build> l = new ArrayList<Build>();

        // go through every job
        for (String key : dbJSON.keySet()) {
            Build b = json2Build(key, dbJSON.getJSONObject(key));
            l.add(b);
        }

        return l;
    }

    // EXAMPLE POST REQUEST HANDLER
    @POST
    @Path("push")
    @Consumes("application/x-www-form-urlencoded")
    public Response push(@FormParam("payload") String payload) {

        // TODO
        // 1. set commit status to pending
        // 2. clone repository
        /*
        try {
            Git git = Git.cloneRepository().setURI("https://github.com/eclipse/jgit.git").call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        */
        // 3. build
        // 4. run tests
        // 5. store build data
        // 6. set commit status to success/fail/error

        String jobID = UUID.randomUUID().toString();

        JSONObject json = new JSONObject(payload);
        JSONObject repository = json.getJSONObject("repository");
        String branchRef = json.getString("ref");
        String cloneUrl = repository.getString("clone_url");

        // Run build jobs asynchronously
        Runnable job = () -> BuildJob.run(jobID, cloneUrl, branchRef);
        jobsQueue.execute(job);

        return Response.status(200).build();
    }

}
