package buildtools;

import org.json.JSONArray;

public class Build {
    public enum Result {
        pending,
        success,
        failure,
        error
    }

    private String jobID;
    private Result status;
    private String commitSha;
    private String url;
    private JSONArray log;

    public Build(String jobID, Result status, String commitSha, String url, JSONArray log) {
        this.jobID = jobID;
        this.status = status;
        this.commitSha = commitSha;
        this.url = url;
        this.log = log;
    }

    public String getJobID() {
        return jobID;
    }

    public Result getStatus() {
        return status;
    }

    public String getCommitSha() {
        return commitSha;
    }

    public String getUrl() {
        return url;
    }

    public JSONArray getLog() {
        return log;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public void setStatus(Result status) {
        this.status = status;
    }

    public void setCommitSha(String commitSha) {
        this.commitSha = commitSha;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLog(JSONArray log) {
        this.log = log;
    }

}
