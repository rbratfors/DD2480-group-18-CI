import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

        String repoDir = "repo/";
        
        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        /*
        try {
            Git git = Git.cloneRepository().setURI("https://github.com/eclipse/jgit.git").call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        */
        // 2nd set pending status on repo
        

        // 3rd compile the code
        RunBash.run(repoDir);

        // 4th set status depending on compilation success

        response.getWriter().println("CI job done.");
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {   
        //RunBash.run(repoDir);
    /*
        Server server = new Server(8018);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
     */
    }
}
