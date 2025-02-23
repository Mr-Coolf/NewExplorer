package Display.ConsoleDisplay.Commands.IO;

import Display.ConsoleDisplay.Commands.CommandType;
import Display.ConsoleDisplay.Commands.Exceptions.IllegalInputException;

import static Display.ConsoleDisplay.ConsoleDisplay.printer;

import java.io.InputStream;
import java.util.Scanner;

public class Reader {
    Scanner scanner;

    public Reader () {
        scanner = new Scanner(System.in);
    }

    public Reader (InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }
    public String readLine(String s) {
        printer.print(s + ": ", CommandType.input);
        var res = scanner.nextLine();
        printer.reset();
        return res;
    }

    public boolean getYesOrNo(String s) {
        var input = readLine(s);
        if ("yes".startsWith(input.toLowerCase())) {
            return true;
        }
        if ("no".startsWith(input.toLowerCase())) {
            return false;
        }
        throw new IllegalInputException("Input should be \"yes\" or \"no\".");
    }
}
