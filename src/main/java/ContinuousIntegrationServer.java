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
import org.eclipse.jgit.lib.Ref;
import org.json.JSONObject;


/** 
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class ContinuousIntegrationServer extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);

        JSONObject json = new JSONObject(request.getParameter("payload"));
        for (String key : json.keySet()) {
            System.out.println(key);
        }

        JSONObject repo = json.getJSONObject("repository");
        for (String key : repo.keySet()) {
            System.out.println(key);
        }

        String cloneUrl = repo.getString("clone_url");
        System.out.println(cloneUrl);

        String branchRef = json.getString("ref");




        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        
        try {
            Git git = Git.cloneRepository().setURI(cloneUrl).setBranch(branchRef).call();
            String desc = git.describe().call();
            System.out.println(desc);
            List<Ref> branches = git.branchList().setListMode(ListMode.ALL).call();
            for (Ref branch : branches) {
                System.out.println(branch.getName());
            }
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
