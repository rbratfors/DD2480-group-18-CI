package buildtools;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StatusUpdater {

    /**
     * Sets commit status. OAuth authorization using /token file in root folder.
     * @param owner - name of repo owners account
     * @param repo - name of repo
     * @param sha - sha value of commit
     * @param status - pending, success, failure, error
     * @throws IOException
     */
    public static void updateStatus(String owner, String repo, String sha, Build.Result status) throws IOException {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/statuses/" + sha;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        BufferedReader br = new BufferedReader(new FileReader("token"));
        String token = br.readLine();

        String description;
        switch (status) {
            case pending:
                description = "Pending";
                break;
            case success:
                description = "Success";
                break;
            case failure:
                description = "Failure";
                break;
            default:
                description = "Error";

        }

        JSONObject json = new JSONObject();
        json.put("state", status);
        json.put("target_url", "https://www.google.se");
        json.put("description", description);
        json.put("context", "mobergliuslefors");

        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Authorization", "token " + token);

        CloseableHttpResponse response = client.execute(httpPost);
        client.close();
    }

}
