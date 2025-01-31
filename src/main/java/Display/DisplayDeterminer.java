package Display;

import Display.AppDisplay.AppDisplay;
import Display.ConsoleDisplay.ConsoleDisplay;

public class DisplayDeterminer {

    public static void determine (String[] args) {
        if (args.length != 1) {
            System.out.println("Write one argument that should be either:\n console\n app");
            System.exit(1);
        }
        if (AppDisplay.RUN_ARGUMENT.startsWith(args[0].toLowerCase()))
            (new Thread(new AppDisplay())).start();
        else if (ConsoleDisplay.RUN_ARGUMENT.startsWith(args[0].toLowerCase()))
            (new Thread(new ConsoleDisplay())).start();
        else {
            System.out.println("Write one argument that should be either:\n console\n app");
            System.exit(1);
        }
    }

}
