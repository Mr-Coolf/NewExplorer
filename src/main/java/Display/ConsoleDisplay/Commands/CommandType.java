package Display.ConsoleDisplay.Commands;

import Constants.Constants;
import org.fusesource.jansi.Ansi;

public enum CommandType {
    warning ("[WARNING]", Ansi.Color.YELLOW),
    error ("[ERROR]", Ansi.Color.RED),
    info ("[INFO]", Ansi.Color.GREEN),
    progress ("[PROGRESS]", Ansi.Color.CYAN),
    input ("[INPUT]", Ansi.Color.MAGENTA);

    public String s;
    public Ansi.Color color;


    CommandType(String s, Ansi.Color color) {
        var result = new StringBuilder();

        var sLen = s.length();
        result.append(s)
                .append(" ".repeat(Constants.TYPE_INFO_LENGTH - sLen));

        this.s = result.toString();
        this.color = color;
    }
}
