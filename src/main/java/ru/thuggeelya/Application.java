package ru.thuggeelya;

import org.apache.commons.cli.*;
import ru.thuggeelya.analyze.AccessLog;
import ru.thuggeelya.analyze.Analyzer;
import ru.thuggeelya.exceptions.AccessLogMatcherException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("u", true, "Min accessibility level");
        options.addOption("t", true, "Max request time");
        double u = 0, t = 0;
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(options, args);
            u = Double.parseDouble(commandLine.getOptionValue("u"));
            t = Double.parseDouble(commandLine.getOptionValue("t"));
        } catch (ParseException | NullPointerException | NumberFormatException e) {
            System.err.println("Failed parsing command line arguments");
            System.exit(1);
        }

        Analyzer analyzer = new Analyzer(t, u);

        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
            String log;

            while ((log = input.readLine()) != null) {
                analyzer.receive(new AccessLog(log));
            }
        } catch (IOException | AccessLogMatcherException e) {
            e.printStackTrace();
        }

        analyzer.getFailIntervals().forEach(System.out::println);
    }
}
