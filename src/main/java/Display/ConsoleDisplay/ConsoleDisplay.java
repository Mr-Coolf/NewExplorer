package Display.ConsoleDisplay;
import Constants.Constants;
import Display.ConsoleDisplay.Commands.CommandType;
import Display.ConsoleDisplay.Commands.Command;
import Display.ConsoleDisplay.Commands.IO.Printer;
import Display.ConsoleDisplay.Commands.IO.Reader;
import ExplorerLogic.Explorer;

public class ConsoleDisplay implements Runnable{
    public static final String RUN_ARGUMENT = "console";
    public static Printer printer;
    public static Reader reader;
    public static Explorer explorer = new Explorer();

    @Override
    public void run() {
        reader = new Reader();
        printer = new Printer();
        introduction();

        while(true){
            var s = reader.readLine("Write your command");
            var command = Command.getCommand(s);
            command.run(s);
        }
    }

    public void introduction() {
        printer.println( CommandType.info, String.format("Welcome to Explorer v%s!",  Constants.ID), String.format("Enter \"%s\" to see the available commands\n", Command.help.name()) );
    }
}
