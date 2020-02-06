package buildtools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import resources.Storage;

// JobId, CloneUrl, Branch, sha ref ska skickar med
public class BuildJob {

    public static String BUILD_CONFIG_FILE_NAME = ".dd.yml";
    private static Storage storage = new Storage();

    public static void run(String jobID, String cloneURL, String branchRef, String sha, String owner, String repo) throws IOException {
        List<ArrayList<String>> log = new ArrayList<ArrayList<String>>();
        ArrayList<String> logEntry = new ArrayList<String>();
        logEntry.add("Running build job with id " + jobID);
        log.add(new ArrayList(logEntry));


        System.out.println("Running build job with id " + jobID);
        Build pendingBuild = new Build(jobID, Build.Result.pending, "", "",  log);
        StatusUpdater.updateStatus(owner, repo, sha, Build.Result.pending);

        try {
            BuildJob.storage.post(pendingBuild);

        } catch (IOException e) {
            e.printStackTrace();
            logEntry.clear();
            logEntry.add("Internal issue. Contact support.");
            log.add(logEntry);
            BuildJob.fail(jobID, log, sha, owner, repo, Build.Result.error);
            return;
        }

        logEntry.clear();
        logEntry.add("Cloning repository.");
        log.add(new ArrayList(logEntry));

        Git git = null;
        try {
            git = Git.cloneRepository()
                    .setURI(cloneURL)
                    .setDirectory(new File("./" + jobID))
                    .setBranch(branchRef)
                    .call();

        } catch (GitAPIException e) {
            e.printStackTrace();
            logEntry.clear();
            logEntry.add("Failed to clone repository " + cloneURL);
            log.add(new ArrayList(logEntry));
            BuildJob.fail(jobID, log, sha, owner, repo, Build.Result.error);
            return;
        }


        Repository repository = git.getRepository();
        File root = repository.getWorkTree();
        File[] rootFiles = root.listFiles();

        String buildDirectory = "./" + jobID;
        String buildConfig = buildDirectory + "/" + BUILD_CONFIG_FILE_NAME;

        boolean hasBuildConfig = false;
        for (File f : rootFiles) {
            hasBuildConfig |= f.getPath().equals(buildConfig);
        }

        if (hasBuildConfig) {
            ArrayList<ArrayList<String>> commands = RunBash.run(buildDirectory, buildConfig);
            ArrayList<Integer> exitValues = new ArrayList<Integer>();
            for (ArrayList<String> command : commands) {
                int ev = Integer.parseInt(command.get(command.size() - 1));
                exitValues.add(ev);
                command.remove(command.size() - 1);
                for (String ln : command) {
                    System.out.println(ln);
                }
            }
            for (int ev : exitValues) {
                if (ev != 0) {
                    logEntry.clear();
                    logEntry.add("Failed, exit value non-zero.");
                    log.add(new ArrayList(logEntry));
                    log.addAll(commands);
                    BuildJob.fail(jobID, log, sha, owner, repo, Build.Result.failure);
                    break;
                }
            }
            logEntry.clear();
            logEntry.add("Found build file.");
            log.add(new ArrayList(logEntry));
            log.addAll(commands);

            BuildJob.success(jobID, log, sha, owner, repo);

        } else {
            logEntry.clear();
            logEntry.add("Failed to find a build file.");
            log.add(new ArrayList(logEntry));

            BuildJob.fail(jobID, log, sha, owner, repo, Build.Result.error);
        }

    }

    private static void success(String jobID, List<ArrayList<String>> log, String sha, String owner, String repo) throws IOException {
        // TODO:
        // Call notification engine

        Build succeededBuild = new Build(jobID, Build.Result.success, "", "", log);
        StatusUpdater.updateStatus(owner, repo, sha, Build.Result.success);
        try {
            BuildJob.storage.post(succeededBuild);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Succeeded job " + jobID + " with log ");
        System.out.println(log);
    }

    public static void fail(String jobID, List<ArrayList<String>> log, String sha, String owner, String repo, Build.Result status) throws IOException {
        // TODO:
        // Call notification engine

        Build failedBuild = new Build(jobID, Build.Result.failure, "", "", log);
        try {
            BuildJob.storage.post(failedBuild);


        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Failed job " + jobID + " with log ");
        System.out.println(log);



    }
}