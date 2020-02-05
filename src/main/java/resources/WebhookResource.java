package resources;

import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;
import java.util.ArrayList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("ci")
public class WebhookResource {
    Storage storage = new Storage();
    public enum Result {
        pending,
        success,
        failure,
        error
    }

    // Create "build" class for storing and sending
    public class Build {
        public String log;
        public String jobID;
        public String commitSha;
        public String url;
        public Result status;

        public Build(String log, String jobID, String commitSha, String url, Result status) {
            this.log = log;
            this.jobID = jobID;
            this.commitSha = commitSha;
            this.url = url;
            this.status = status;
        }
    }

    // EXAMPLE GET REQUEST HANDLER
    // A list of of builds is sent by returning List<Build> instead.
    // TODO
    // load a previous build from storage
    // load multiple previous builds from storage
    @GET
    @Path("get")
    @Produces("application/json")
    public ArrayList<Build> getBuilds() throws IOException {
        JSONObject dbJSON = storage.getAll();
        ArrayList<WebhookResource.Build> l = new ArrayList<WebhookResource.Build>();

        // go through every job
        for (String key : dbJSON.keySet()) {
            JSONObject current = dbJSON.getJSONObject(key);
            String jobID = key;
            String commitSha = current.getString("commitSha");
            String url = current.getString("url");
            String log = current.getString("log");

            // fix status
            String tempStatus = current.getString("status");
            Result status;
            switch (tempStatus) {
                case "success":
                    status = Result.success;
                    break;
                case "pending":
                    status = Result.pending;
                    break;
                case "failure":
                    status = Result.failure;
                    break;
                case "error":
                    status = Result.error;
                    break;
                default:
                    throw new IOException("Invalid 'status' in database");
            }

            Build b = new Build(log, jobID, commitSha, url, status);
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
        // 3. build
        // 4. run tests
        // 5. store build data
        // 6. set commit status to success/fail/error

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository

        String jobID = UUID.randomUUID().toString();

        JSONObject json = new JSONObject(payload);
        JSONObject repository = json.getJSONObject("repository");

        String branchRef = json.getString("ref");
        String cloneURL = repository.getString("clone_url");

        // Run job, should be done as an ExecutorService
        BuildJob.run(jobID, cloneURL, branchRef);

        return Response.status(200).build();
    }

}
