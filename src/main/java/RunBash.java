
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RunBash {
    static void run(String path) {
        try {
            readThis(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void readThis(String path) throws Exception {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path + "bash.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (exactMatch(line, "Build") || exactMatch(line, "Test") ) {
                    line = reader.readLine();
                    if (line != null) {
                        runCommand(line, path);
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

    private static boolean exactMatch(String line, String match) {
        String pattern = "\\b" + match + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        return m.find();
    }


    private static void runCommand(String line, String path) throws Exception {

        String[] arr = line.split(" ");


        ProcessBuilder pb = new ProcessBuilder(arr);
        pb.inheritIO();
        pb.directory(new File(path));
        pb.start();
    }
}
