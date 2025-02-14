package Display.ConsoleDisplay.Commands.IO;

import org.fusesource.jansi.AnsiConsole;

public class ANSI {
    public static boolean isTerminalSupported () {
        AnsiConsole.systemInstall();
//        return System.console() != null && AnsiConsole.isInstalled();
        return AnsiConsole.isInstalled();
    }
}
