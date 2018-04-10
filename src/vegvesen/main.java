package vegvesen;

import org.apache.commons.cli.*;
import vegvesen.command_arguments.SplitLargeDataSet;

import java.util.Arrays;


public class main {

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(SplitLargeDataSet.Option());

        CommandLineParser parser = new DefaultParser();
        CommandLine line;
        try {
            line = parser.parse(options, args);
            if (line.getOptions().length == 0) {
                (new HelpFormatter()).printHelp("dsr", options);
            }

            if (line.hasOption(SplitLargeDataSet.OPTION))
                SplitLargeDataSet.Execute(line);

        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
