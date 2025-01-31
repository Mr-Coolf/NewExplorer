package Display.ConsoleDisplay;
import Constants.Constants;
public class ConsoleDisplay implements Runnable{
    public static final String RUN_ARGUMENT = "console";

    @Override
    public void run() {

    }

    public void introduction() {
        System.out.println( String.format("Welcome to Explorer v%s!\n"
                          + "Enter \"%s\" to see the available commands\n", Constants.id, Command.help.getCommand()));
    }
}
