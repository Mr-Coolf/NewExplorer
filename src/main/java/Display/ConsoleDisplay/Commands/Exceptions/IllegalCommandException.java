package Display.ConsoleDisplay.Commands.Exceptions;

import Display.ConsoleDisplay.Commands.Command;

public class IllegalCommandException extends IllegalArgumentException {
    public IllegalCommandException(String s) {
        super(String.format("No such command: %s. Type \"%s\" for list of commands.", s, Command.help.name()));
    }
}
