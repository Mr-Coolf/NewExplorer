package Display.ConsoleDisplay.Commands.IO;
import Display.ConsoleDisplay.Commands.CommandType;

import static org.fusesource.jansi.Ansi.*;

import java.io.PrintStream;
import java.util.List;

public class Printer {
    PrintStream outputStream;
    boolean supportsANSI;

    public Printer() {
        outputStream = System.out;

        supportsANSI = ANSI.isTerminalSupported();
        supportsANSI = true;
    }

    public void print (String s, CommandType type) {
        if (supportsANSI) {
            var res = ansi().fg(type.color).a(type.s).reset().a(s);
            if (type.equals(CommandType.input)) res.fg(type.color);
            outputStream.print( res );
        } else {
            outputStream.print( String.format("%s%s", type.s, s) );
        }
    }

    public void println (String s, CommandType type) {
        if (supportsANSI) {
            outputStream.println( ansi().fg(type.color).a(type.s).reset().a(s) );
        } else {
            outputStream.println( String.format("%s%s", type.s, s) );
        }
    }

    public void println (List<String> list, CommandType type) {
        var res = new StringBuilder();
        for (String str : list) {
            if (supportsANSI) {
                res.append( ansi().fg(type.color).a(type.s).reset().a(str) );
            } else {
                res.append( String.format("%s%s", type.s, str) );
            }
            res.append("\n");
        }
        outputStream.println( res.toString() );

    }

    public void reset () {
        if (supportsANSI) {
            outputStream.print(ansi().reset());
        }
    }

}
