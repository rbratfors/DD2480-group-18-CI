package buildtools;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;


public class BuildJob {
  public static String BUILD_CONFIG_FILE_NAME = ".dd.yml";
  public static void run(String jobID, String cloneURL, String branchRef) {
    // TODO: update database that status is "started"

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
    }

    Repository repository = git.getRepository();
    File root = repository.getWorkTree();
    File[] rootFiles = root.listFiles();

    Boolean hasBuildFile = false;
    //String pathToFile;
    for (File f : rootFiles) {
        hasBuildFile |= f.getPath().equals("./" + jobID + "/" + BUILD_CONFIG_FILE_NAME);
        //pathToFile = f.getPath();
    }

    // TODO:
    // if hasBuildFile:
    //   [status, log] = call resources.RunBash.run(f.getPath())

    // Mock, should be a call to resources.RunBash.run
    String repoDir = "repo/";
    ArrayList<ArrayList<String>> commands = RunBash.run(repoDir);

    if (hasBuildFile) {
      BuildJob.success(jobID, "Found build file.");
      //RunBash.run(pathToFile);
    } else {
      BuildJob.fail(jobID, "Failed to find a build file.");
    }
  }

  public static void fail(String jobID, String log) {
    // TODO:
    // Update database with status and log
    // Call notification engine
    
    // Mock:
    System.out.println("Failed job " + jobID + " with log ");
    System.out.println(log);

  }

  public static void success(String jobID, String log) {
    // TODO:
    // Update database with status and log
    // Call notification engine

    // Mock:
    System.out.println("Succeeded job " + jobID + " with log ");
    System.out.println(log);
  }
}