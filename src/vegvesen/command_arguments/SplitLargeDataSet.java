package vegvesen.command_arguments;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

public class SplitLargeDataSet {
    public static final String OPTION = "s";
    public static void Execute(CommandLine line) {
        System.out.println("Split code comes here");

    }

    public static Option Option() {
        Option o = new Option(OPTION, "Splits the file into smaller components");
        //o.setOptionalArg(true);
        o.setArgs(0);
        return o;
    }
}
