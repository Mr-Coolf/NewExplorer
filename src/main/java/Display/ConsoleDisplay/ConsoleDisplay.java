package Display.ConsoleDisplay;
import Constants.Constants;
import Display.ConsoleDisplay.Commands.CommandType;
import Display.ConsoleDisplay.Commands.Command;
import Display.ConsoleDisplay.Commands.IO.Printer;
import Display.ConsoleDisplay.Commands.IO.Reader;

public class ConsoleDisplay implements Runnable{
    public static final String RUN_ARGUMENT = "console";
    public static Printer printer;
    public static Reader reader;

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
        printer.println( String.format("Welcome to Explorer v%s!\n"
                          + "Enter \"%s\" to see the available commands\n", Constants.ID, Command.help.name()), CommandType.info);
    }
}
