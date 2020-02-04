
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
    static void run() {
        try {
            // path to file, needs to be changed accordingly
            readThis("repo/bash.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void readThis(String path) throws Exception {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                if (exactMatch(line, "Build") || exactMatch(line, "Test") ) {
                    //runCommand(line);
                    line = reader.readLine();
                    if (line != null) {
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

    private static boolean exactMatch(String line, String match) {
        String pattern = "\\b" + match + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        return m.find();
    }


    private static void runCommand(String line) throws Exception {

        String[] arr = line.split(" ");


        ProcessBuilder pb = new ProcessBuilder(arr);
        pb.inheritIO();
        // Change "repo" the the repo where the file is located
        pb.directory(new File("repo"));
        pb.start();
    }
}
