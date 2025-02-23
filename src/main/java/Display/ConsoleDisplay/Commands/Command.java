package Display.ConsoleDisplay.Commands;

import Display.ConsoleDisplay.Commands.Exceptions.IllegalArgumentException;
import Display.ConsoleDisplay.Commands.Exceptions.IllegalCommandException;

import java.io.IOException;
import java.util.*;

import static Display.ConsoleDisplay.Commands.Arguments.*;
import static Display.ConsoleDisplay.ConsoleDisplay.printer;
import static Display.ConsoleDisplay.ConsoleDisplay.explorer;

public enum Command {
    help (
            new String[]{
                    String.format("%s -> writes commands info.", "help"),
                    String.format("%s %s -> writes arguments info.", "help", argumentsHelp.command),
            },
            argumentsHelp, dontPrint, doPrint
    ) {

        @Override
        public void run(String s) {
            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            printer.println("");

            if (args.contains(argumentsHelp)) {
                var arguments = Arguments.values();
                var list = new ArrayList<String>();
                for (var arg : arguments) {
                    list.add(String.format("%s: %s", arg.command, arg.description));
                }
                printer.println(list, CommandType.info);
            } else {
                var commands = Command.values();
                var list = new ArrayList<String>();
                for (var command : commands) {
                    list.addAll(List.of(command.commandInfo));
//                    list.add("\n");
                }
                printer.println(list, CommandType.info);
            }

        }
    },
    opdir (
            new String[] {
                    String.format("%s -> opens a directory.", "opdir"),
            },
            doPrint, showSize, path
    ) {
        @Override
        public void run(String s) {
            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            if (args.contains(path)) {
                try {
                    if (pathArgument.path.isAbsolute())
                        explorer.openAbsolutePathFolder(pathArgument.path);
                    else
                        explorer.openChildrenFolder(pathArgument.path);
                }
                catch (IOException e) {
                    printer.println(e.getMessage(), CommandType.error);
                    return;
                }
            }

            if (args.contains(doPrint)) {
                
            }
        }
    },
    exdir (
        new String[] {
                String.format("%s -> exits opened directory, aka opens parent directory.", "exdir"),
        }
    ) {
        @Override
        public void run(String s) {

        }
    },
    del (
        new String[] {
                String.format("%s -> deletes the specified file.", "del"),
        }
    ) {
        @Override
        public void run(String s) {

        }
    },
    copy (
        new String[] {
                String.format("%s -> copies the specified file and puts it into the buffer.", "copy"),
        }
    ) {
        @Override
        public void run(String s) {

        }
    },
    cut (
        new String[] {
                String.format("%s -> copies the specified file and removes it as soon as it is pasted.", "cut"),
        }
    ) {
        @Override
        public void run(String s) {

        }
    },
    paste (
        new String[] {
                String.format("%s -> pastes the file that has been copied.", "paste"),
        }
    ) {
        @Override
        public void run(String s) {

        }
    },
    info (
        new String[] {
                String.format("%s -> shows the folder/file info.", "info"),
        }
    ) {
        @Override
        public void run(String s) {

        }
    },
    run (
        new String[] {
                String.format("%s -> runs a file.", "run"),
        }
    ) {
        @Override
        public void run(String s) {

        }
    },
    tree (
        new String[] {
                String.format("%s -> shows the folder and file tree of the specified folder.", "tree"),
        }
    ) {
        @Override
        public void run(String s) {

        }
    },
    rename (
        new String[] {
                String.format("%s -> renames the specified folder/file.", "rename"),
        }
    ) {
        @Override
        public void run(String s) {

        }
    },
    size (
        new String[] {
                String.format("%s -> shows the size of the specified folder/file.", "size"),
        }
    ) {
        @Override
        public void run(String s) {

        }
    },
    stop (
            new String[] {
                    String.format("%s -> stops the program.", "stop"),
            },
            dontPrint
    ) {
        @Override
        public void run(String s) {
            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);
            if (!args.contains(dontPrint)) {
                printer.println("May the 4th be with you!", CommandType.info);
            }
            System.exit(0);
        }
    };

    public final String[] commandInfo;
    public final Set<Arguments> supportedArguments;

    Command(String[] commandInfo, Arguments... supportedArguments) {
        this.commandInfo = commandInfo;
        this.supportedArguments = new HashSet<>();
        Collections.addAll(this.supportedArguments, supportedArguments);
    }



    public List<Arguments> getArguments(String s) {
//        var args = s.split(" ");
//        var len = args.length;
//        var res = new ArrayList<Arguments>();
//
//        for (int i = 1; i < len; i++) {
//            res.add(Arguments.getArgument(args[i]));
//        }

        return Arguments.getArguments(s);
    }

    public void areArgumentsSupported(List<Arguments> arguments) {
        for (var arg : arguments) {
            if (!supportedArguments.contains(arg)) {
                throw new IllegalArgumentException(arg.name());
            }
        }
    }

    abstract public void run(String s);

    public static Command getCommand(String s) {
        var commands = Command.values();
        for (var command: commands) {
            if (s.toLowerCase().startsWith(command.name())) {
                return command;
            }
        }
        throw new IllegalCommandException(s);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
