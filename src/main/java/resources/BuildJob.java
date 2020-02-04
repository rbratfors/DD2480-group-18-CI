package resources;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

public class BuildJob {
  public static String BUILD_CONFIG_FILE_NAME = ".dd.yml";
  public static void run(String jobID, String cloneURL, String branchRef) {
    // TODO: update database that status is "started"

    try {
    Git git = Git.cloneRepository()
      .setURI(cloneURL)
      .setDirectory(new File("./" + jobID))
      .setBranch(branchRef)
      .call();

    Repository repository = git.getRepository();

    File root = repository.getWorkTree();
    File[] rootFiles = root.listFiles();

    Boolean hasBuildFile = false;
    for (File f : rootFiles) {
        hasBuildFile |= f.getPath().equals("./" + jobID + "/" + BUILD_CONFIG_FILE_NAME);
    }

    // TODO:
    // if hasBuildFile:
    //   [status, log] = call RunBash.run(f.getPath())
    //   Update database with status and log
    //   Call notification engine

  } catch (GitAPIException e) {
    e.printStackTrace();
  }
  }
}