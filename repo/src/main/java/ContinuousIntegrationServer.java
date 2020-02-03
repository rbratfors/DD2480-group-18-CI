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

import java.io.File;

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

        // 2nd compile the code

        response.getWriter().println("CI job done.");
        try {
            runCommand("javac src/main/java/Test.java");
            runCommand("java -classpath src/main/java/ Test Print this sentence");
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.getWriter().println("CI job done.");
    }
    
    private static void output(String cmd, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
            new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
      }

    private static void runCommand(String command) throws Exception {
        /*
        Process p = Runtime.getRuntime().exec(command);
        output("stdout:", p.getInputStream());
        output("stderr:", p.getErrorStream());
        p.waitFor();
        */
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();
        pb.directory(new File("repo"));
        pb.start();
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {   

        try {
            runCommand("ls");
            //runCommand("javac src/main/java/Test.java");
            //runCommand("java -classpath src/main/java/ Test Print this sentence");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Server server = new Server(8018);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
}
