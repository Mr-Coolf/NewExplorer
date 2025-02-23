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
//        supportsANSI = true;
    }

    public void println (String s) {
        outputStream.println(s);
    }

    public void print (String s) {
        outputStream.print(s);
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

    public void println (CommandType type, Object... objects) {
        var res = new StringBuilder();
        if (supportsANSI) {
            for (Object o : objects) {
                res.append( ansi().fg(type.color).a(type.s).reset().a( o.toString() ) ).append('\n');
            }
        }
        else {
            for (Object o : objects) {
                res.append( o.toString() ).append("\n");
            }
        }
        outputStream.print( res );
    }

    public void println (List<String> list, CommandType type) {
        var res = new StringBuilder();
        for (String str : list) {
            if (supportsANSI) {
                if (!str.equals("\n"))
                    res.append( ansi().fg(type.color).a(type.s).reset().a(str) );
            } else {
                if (!str.equals("\n"))
                    res.append( String.format("%s%s", type.s, str) );
            }
            res.append("\n");
        }
        outputStream.println( res );

    }

    public void reset () {
        if (supportsANSI) {
            outputStream.print(ansi().reset());
        }
    }

}
