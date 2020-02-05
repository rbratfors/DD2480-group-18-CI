package buildtools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import resources.Storage;

public class BuildJob {
  public static String BUILD_CONFIG_FILE_NAME = ".dd.yml";
  private static Storage storage = new Storage();
  public static void run(String jobID, String cloneURL, String branchRef) {
    // TODO: update database that status is "started"
    System.out.println("Running build job with id " + jobID);

    Build pendingBuild = new Build(jobID, Build.Result.pending, "", "", "");

    try {
      BuildJob.storage.post(pendingBuild);

    } catch (IOException e) {
      e.printStackTrace();
      BuildJob.fail(jobID, "Internal issue. Contact support.");
      return;
    }


    Git git = null;
    try {
    git = Git.cloneRepository()
      .setURI(cloneURL)
      .setDirectory(new File("./" + jobID))
      .setBranch(branchRef)
      .call();

    } catch (GitAPIException e) {
      e.printStackTrace();
      BuildJob.fail(jobID, "Failed to clone repository " + cloneURL);
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
      BuildJob.success(jobID, "Found build file.");
    } else {
      BuildJob.fail(jobID, "Failed to find a build file.");
    }

    System.out.println("Finished build job with id " + jobID);
  }

  public static void fail(String jobID, String log) {
    // TODO:
    // Call notification engine

    Build failedBuild = new Build(jobID, Build.Result.failure, "", "", "");
    try {
      BuildJob.storage.post(failedBuild);

    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    
    // Mock:
    System.out.println("Failed job " + jobID + " with log ");
    System.out.println(log);

  }

  public static void success(String jobID, String log) {
    // TODO:
    // Call notification engine

    Build succeededBuild = new Build(jobID, Build.Result.success, "", "", "");
    try {
      BuildJob.storage.post(succeededBuild);

    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    // Mock:
    System.out.println("Succeeded job " + jobID + " with log ");
    System.out.println(log);
  }
}