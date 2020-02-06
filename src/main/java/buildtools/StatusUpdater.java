package buildtools;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class StatusUpdater {

    public static void updateStatus(String owner, String repo, String sha, String status) throws IOException {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/statuses/" + sha;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://www.example.com");

        File tokenFile = new File(".").getParentFile();
        System.out.println(tokenFile.getPath());
        /*
        JSONObject json = new JSONObject();
        json.append("state", status);
        json.append("target_url", "https://www.google.se");
        json.append("description", "In progress...");
        json.append("context", "mobergliuslefors");

        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        System.out.println(response.getStatusLine().getStatusCode());
        client.close();
        */

    }

    public static void main(String[] args) throws IOException {
        String owner = "adbjo";
        String repo = "DD2480-group-18-CI";
        String sha = "3e23c8d66264d1612095d6dd429976b1fd35073a";

        updateStatus(owner, repo, sha, "pending");

    }
}
