package resources;

import buildtools.Build;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("ci")
public class Resource {
    Storage storage = new Storage();

    /**
     * Fetches all builds from local database
     * path: /ci/get
     * @return
     * @throws IOException
     */
    @GET
    @Path("get")
    @Produces("application/json")
    public ArrayList<Build> getBuilds() throws IOException {
        JSONObject dbJSON = storage.getAll();
        ArrayList<Build> l = new ArrayList<Build>();

        // go through every job
        for (String key : dbJSON.keySet()) {
            JSONObject current = dbJSON.getJSONObject(key);
            String jobID = key;
            String commitSha = current.getString("commitSha");
            String url = current.getString("url");
            String log = current.getString("log");

            // fix status
            String tempStatus = current.getString("status");
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

        System.out.println();
        JSONObject json = new JSONObject(payload);
        //String cloneURL = json.getJSONObject("repository").getString("clone_url");
        System.out.println(payload);

        return Response.status(200).build();
    }

}
