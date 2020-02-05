package buildtools;

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
import java.util.*;

public class RunBash {

    // tmp desc: "main" file, returns the output of all commands
    // buildDirectoryPath - the relative path to the directory which commands are run in
    // buildConfigPath - the relative path to the build configuration file
    static ArrayList<ArrayList<String>> run(String buildDirectoryPath, String buildConfigPath) {
        ArrayList<ArrayList<String>> commands = new ArrayList<ArrayList<String>>();
        try {
            commands = readThis(buildDirectoryPath, buildConfigPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commands;
    }

    // tmp desc: reads through a file and searches for key words, then executes command line 
    // below match and returns output of them all
    private static ArrayList<ArrayList<String>> readThis(String buildDirectoryPath, String buildConfigPath) throws Exception {
        BufferedReader reader;
        ArrayList<ArrayList<String>> commands = new ArrayList<ArrayList<String>>();
        try {
            reader = new BufferedReader(new FileReader(buildConfigPath));
            String line = reader.readLine();
            while (line != null) {
                if (exactMatch(line, "Build") || exactMatch(line, "Test") ) {
                    line = reader.readLine();
                    if (line != null) {
                        commands.add(runCommand(line, buildDirectoryPath));
                    }
                    // read next line
                }
                line = reader.readLine();

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commands;
    }

    // tmp desc: returns true if String match is found in String line.
    private static boolean exactMatch(String line, String match) {
        String pattern = "\\b" + match + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        return m.find();
    }

    // tmp desc: reads output and errors of the command execution
    private static ArrayList<String> output(String prefix, InputStream in) throws Exception {
        String ln;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ArrayList<String> output = new ArrayList<String>();
        output.add(prefix);
        while ((ln = reader.readLine()) != null) {
            output.add(ln);
        }
        output.add("\n");
        return output;
    }

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



    // tmp desc: runs the String as a bash command and returns the outputs, errors and exit value
    private static ArrayList<String> runCommand(String line, String buildDirectoryPath) throws Exception {

        String[] arr = line.split(" ");

        ProcessBuilder pb = new ProcessBuilder(arr);
        pb.redirectErrorStream(true);
        pb.directory(new File(buildDirectoryPath));
        Process p = pb.start();

        ArrayList<String> cmdOutput = output("Command output:", p.getInputStream());
        cmdOutput.addAll(output("Command errors:", p.getErrorStream()));
        p.waitFor();
        int eValue = p.exitValue();
        cmdOutput.add(Integer.toString(eValue));

        return cmdOutput;

    }
}
