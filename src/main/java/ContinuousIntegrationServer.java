import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Skeleton of a ContinuousIntegrationServer which acts as webhook See the Jetty
 * documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler {
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);

        JSONObject json = new JSONObject(request.getParameter("payload"));
        // for (String key : json.keySet()) {
        // System.out.println(key);
        // }

        JSONObject repo = json.getJSONObject("repository");
        for (String key : repo.keySet()) {
            System.out.println(key);
        }

        String cloneUrl = repo.getString("clone_url");
        System.out.println(cloneUrl);

        String branchRef = json.getString("ref");
        System.out.println(branchRef);

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository

        // yaya

        String folderName = UUID.randomUUID().toString();
        String buildFileName = ".dd.yml";

        try {
            Git git = Git.cloneRepository().setURI(cloneUrl).setDirectory(new File("./" + folderName)).setBranch(branchRef).call();
            Repository repository = git.getRepository();
            System.out.println(repository.getBranch());
            System.out.println(repository.getFullBranch());

            File root = repository.getWorkTree();
            File[] rootFiles = root.listFiles();

            for (File f : rootFiles) {
                System.out.println(f.getPath());
            }

            Boolean hasBuildFile = false;
            for (File f : rootFiles) {
                hasBuildFile |= f.getPath().equals("./" + folderName + "/" + buildFileName);
            }

            System.out.println("Has build file: " + hasBuildFile);

            String desc = git.describe().call();
            System.out.println("bla " + desc);
            List<Ref> branches = git.branchList().setListMode(ListMode.ALL).call();
            for (Ref branch : branches) {
                System.out.println(branch.getName());
            }

            ObjectId head  = repository.resolve(Constants.HEAD);

            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(head);
    
                System.out.println("Time of commit (seconds since epoch): " + commit.getCommitTime());
    
                // and using commit's tree find the path
                RevTree tree = commit.getTree();
                System.out.println("Having tree: " + tree);
            }

            root.delete();
            
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        

        // 2nd compile the code

        response.getWriter().println("CI job done.");
    }
 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8018);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
}
