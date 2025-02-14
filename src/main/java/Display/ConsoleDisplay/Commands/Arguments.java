package Display.ConsoleDisplay.Commands;

import Display.ConsoleDisplay.Commands.Exceptions.IllegalArgumentException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Constants.Constants.ARGUMENT_PREFIX;

public enum Arguments {
    argumentsHelp(
            "args",
            "Shows list of all arguments.",
            Command.help
    ),
    doPrint(
            "ep",
            "Enables printing a folder tree for this command if it is not enabled by default.",
            Command.opdir, Command.exdir, Command.copy, Command.paste
    ),
    dontPrint (
            "dp",
            "Disables printing a folder tree for this command if it is enabled by default.",
            Command.help, Command.tree, Command.del, Command.cut, Command.rename, Command.size
    ),
//    showTree ( //Implemented by doPrint
//            "tree",
//            "Prints a folder/file tree after the command has been executed"
//    ),
    questionAnswerYes (
            "y",
            "If somewhere during the command runtime there will be an Y/n question, Y option will be automatically selected.",
            Command.del, Command.cut, Command.rename, Command.paste
    ),
    questionAnswerNo (
            "n",
            "If somewhere during the command runtime there will be an Y/n question, Y option will be automatically selected.",
            Command.del, Command.cut, Command.rename, Command.paste
    ),
    showSize (
            "size",
            "Shows size of all the folders/files that are being printed.",
            Command.opdir, Command.exdir, Command.del, Command.copy, Command.cut, Command.paste, Command.tree, Command.rename
    ),
    path (
            "path",
            "After this argument you can enter a path to a certain directory or file.",
            Command.opdir, Command.exdir, Command.del, Command.copy, Command.cut, Command.paste, Command.info, Command.run, Command.tree, Command.rename, Command.size
    ) {

    };

    public static PathArgument pathArgument = null;

    public final String command;
    public final String description;
    public final Command[] canBeUsedWith;

    Arguments(String command, String description, Command... canBeUsedWith) {
        this.command = ARGUMENT_PREFIX + command;
        this.description = description;
        this.canBeUsedWith = canBeUsedWith;
    }

    public static Arguments getArgument(String s) {
        var args = Arguments.values();
        for (var arg : args) {
            if (s.toLowerCase().startsWith(arg.command)){

                if (arg.equals(Arguments.path)) {
                    pathArgument = new PathArgument(s.substring(s.indexOf(path.command) + path.command.length()));
                }

                return arg;

            }
        }
        throw new IllegalArgumentException(s);
    }

    public static List<Arguments> getArguments(String s) {
        var res = new ArrayList<Arguments>();

        if (s.contains(ARGUMENT_PREFIX)) {
            var s1 = s.substring(s.indexOf(ARGUMENT_PREFIX)).toLowerCase();
            do {
                var s2 = s1.substring(s1.indexOf(ARGUMENT_PREFIX) + 1);

                if (s2.contains(ARGUMENT_PREFIX)) {
                    s1 = s1.substring(0, s2.indexOf(ARGUMENT_PREFIX));
                    s2 = s2.substring(s2.indexOf(ARGUMENT_PREFIX));
                }

                res.add(getArgument(s1));

                s1 = s2;

            } while (s1.contains(ARGUMENT_PREFIX));
        }

        return res;
    }

}

class PathArgument {
    Path path;

    public PathArgument(String path) {
        this.path = Paths.get(path);
    }
}
