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
            //runCommand("javac src/main/java/Test.java");
            //runCommand("java -classpath src/main/java/ Test Print this sentence");
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

    private static void readThis(String path) throws Exception {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                if (exactMatch(line, "Build")){
                    //runCommand(line);
                    line = reader.readLine();
                    if(line != null) {
                        runCommand(line);
                    }

                    // read next line
                }
                line = reader.readLine();

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean exactMatch(String line, String match){
         String pattern = "\\b"+match+"\\b";
         Pattern p=Pattern.compile(pattern);
         Matcher m=p.matcher(line);
         return m.find();
    }


    private static void runCommand(String line) throws Exception {

        String[] arr = line.split(" ");

        /*
        Process p = Runtime.getRuntime().exec(command);
        output("stdout:", p.getInputStream());
        output("stderr:", p.getErrorStream());
        p.waitFor();
        */
        arr[0] = "./gradlew";
        arr[1] = "build";

        ProcessBuilder pb = new ProcessBuilder(arr);
        pb.inheritIO();
        pb.directory(new File("repo"));
        pb.start();
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {   

        try {
            readThis("repo/bash.txt");
            //runCommand("javac src/main/java/Test.java");
            //runCommand("java -classpath src/main/java/ Test Print this sentence");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        Server server = new Server(8018);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
        */
    }
}
