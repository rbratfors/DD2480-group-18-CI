import org.eclipse.jetty.server.Server;

import java.util.*;

/** 
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class ContinuousIntegrationServer {

    // prints output, errors and exit value of bash executions
    private void printBash(ArrayList<ArrayList<String>> commands) {
        int exitValue;
        ArrayList<Integer> exitValues = new ArrayList<Integer>();
        int i = 1;
        for(ArrayList<String> cmd : commands) {
            int ev = Integer.parseInt(cmd.get(cmd.size()-1));
            exitValues.add(ev);
            System.out.println("**************************");
            System.out.println("Command " + i);
            cmd.remove(cmd.size()-1);
            for(String s : cmd) {
                System.out.println(s);
            }
            System.out.println("Exit value: " + ev);
            System.out.println("**************************");
            i++;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(ContinuousIntegrationServer.class);

    public static void main(String[] args) {

        Server server = new Server(8018);

        ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);

        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);

        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter("jersey.config.server.provider.packages", "resources");

        String repoDir = "repo/";
        ArrayList<ArrayList<String>> commands = RunBash.run(repoDir);
        //printBash(commands);

        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            logger.error("Error occurred while starting Jetty", ex);
            System.exit(1);
        }

        finally {
            server.destroy();
        }
    }
}