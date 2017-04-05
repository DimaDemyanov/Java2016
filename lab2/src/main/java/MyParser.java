import org.apache.commons.cli.*;

public class MyParser {
    private Options opts;
    private CommandLine cmd;

    public MyParser(){
        opts = new Options();
        Option opt;
        OptionGroup optGroupCode = new OptionGroup();
        OptionGroup optGroupMode = new OptionGroup();

        optGroupCode.addOption(new Option("e","encode", false, "encode"));
        optGroupCode.addOption(new Option("d","decode", false, "decode"));
        opts.addOptionGroup(optGroupCode);

        opt = new Option("f","file", true, "file");
        opt.setArgs(2);
        optGroupMode.addOption(opt);
        optGroupMode.addOption(new Option("p","pipe", false, "pipe"));
        opts.addOptionGroup(optGroupMode);

        opts.addOption("h", "help");
    }

    public void parse(String [] args) throws ParseException{
        Parser parser = new PosixParser();
        cmd = parser.parse(opts, args);
        int optsLength = cmd.getOptions().length;
        if (optsLength == 1 && cmd.hasOption('h')){
            return;
        }
        if (optsLength < 2 || optsLength > 2){
            throw new ParseException("Incorrect arguments");
        }
    }

    public CommandLine getCommandLine(){
        return cmd;
    }
}
