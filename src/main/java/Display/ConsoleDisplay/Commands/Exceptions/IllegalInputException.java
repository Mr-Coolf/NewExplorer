package Display.ConsoleDisplay.Commands.Exceptions;

public class IllegalInputException extends RuntimeException {
    public IllegalInputException(String message) {
        super(message);
    }
}
