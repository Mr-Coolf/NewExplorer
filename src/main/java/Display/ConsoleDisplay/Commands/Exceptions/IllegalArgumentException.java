package Display.ConsoleDisplay.Commands.Exceptions;

import Display.ConsoleDisplay.Commands.Arguments;
import Display.ConsoleDisplay.Commands.Command;

public class IllegalArgumentException extends java.lang.IllegalArgumentException {
    public IllegalArgumentException(String s) {
        super(String.format("No such argument: %s. Type \"%s %s\" for list of arguments.", s, Command.help.name(), Arguments.argumentsHelp.command));
    }
}
