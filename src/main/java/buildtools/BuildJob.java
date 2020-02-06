package buildtools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import resources.Storage;

public class BuildJob {
  public static String BUILD_CONFIG_FILE_NAME = ".dd.yml";
  private static Storage storage = new Storage();
  public static void run(String jobID, String cloneURL, String branchRef) {
    List<ArrayList<String>> log = new ArrayList<ArrayList<String>>();
    ArrayList<String> logEntry = new ArrayList<String>();
    logEntry.add("Running build job with id " + jobID);
    log.add(logEntry);


    System.out.println("Running build job with id " + jobID);
    Build pendingBuild = new Build(jobID, Build.Result.pending, "", "", log);

    try {
      BuildJob.storage.post(pendingBuild);

    } catch (IOException e) {
      e.printStackTrace();
      logEntry.clear();
      logEntry.add("Internal issue. Contact support.");
      log.add(logEntry);
      BuildJob.fail(jobID, log);
      return;
    }

    logEntry.clear();
    logEntry.add("Cloning repository.");
    log.add(logEntry);


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
      log.add(logEntry);
      BuildJob.fail(jobID, log);
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
      for (ArrayList<String> command : commands) {
        for (String ln : command) {
          System.out.println(ln);
        }
      }

      logEntry.clear();
      logEntry.add("Found build file.");
      log.add(logEntry);
      log.addAll(commands);

      BuildJob.success(jobID, log);
    } else {

      logEntry.clear();
      logEntry.add("Failed to find a build file.");
      log.add(logEntry);

      BuildJob.fail(jobID, log);
    }

    System.out.println("Finished build job with id " + jobID);
  }

  public static void fail(String jobID, List<ArrayList<String>> log) {
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

  public static void success(String jobID, List<ArrayList<String>> log) {
    // TODO:
    // Call notification engine

    Build succeededBuild = new Build(jobID, Build.Result.success, "", "", log);
    try {
      BuildJob.storage.post(succeededBuild);

    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    System.out.println("Succeeded job " + jobID + " with log ");
    System.out.println(log);
  }
}