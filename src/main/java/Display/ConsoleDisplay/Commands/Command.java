package Display.ConsoleDisplay.Commands;

import Display.ConsoleDisplay.Commands.Exceptions.IllegalArgumentException;
import Display.ConsoleDisplay.Commands.Exceptions.IllegalCommandException;
import ExplorerLogic.Elements.Element;
import ExplorerLogic.Elements.ElementFactory;
import ExplorerLogic.Elements.File;
import ExplorerLogic.Elements.Folder;

import java.awt.*;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.List;

import static Display.ConsoleDisplay.Commands.Arguments.*;
import static Display.ConsoleDisplay.ConsoleDisplay.*;

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

            var childrenFolders = explorer.getChildrenFolders();

            if (args.contains(path)) {
                if (childrenFolders.isEmpty()) throw new RuntimeException("This folder does not contain any children.");
                try {
                    if (pathArgument.path.isAbsolute())
                        explorer.openAbsolutePathFolder(pathArgument.path);
                    else
                        explorer.openChildrenFolder(pathArgument.path);
                }
                catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                if (childrenFolders.isEmpty()) throw new RuntimeException("This folder does not contain any children.");
                try {
                    explorer.openChildrenFolder(childrenFolders.getFirst().getPath());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to open children folder.");
                }
            }

            if (args.contains(doPrint)) {
                if (args.contains(showSize))
                    printer.println(Command.getTreeView(0, true), CommandType.result);
                else
                    printer.println(Command.getTreeView(0, false), CommandType.result);
            } else {
                if (args.contains(showSize)) {
                    printer.println(String.format("\"%s\" can be used only when \"%s\" is used", showSize.command, doPrint.command), CommandType.error);
                }
            }
        }
    },
    exdir (
        new String[] {
                String.format("%s -> exits opened directory, aka opens parent directory.", "exdir"),
        }, doPrint, showSize
    ) {
        @Override
        public void run(String s) {
            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            explorer.exitFolder();

            if (args.contains(doPrint)) {
                if (args.contains(showSize)) {
                    printer.println(Command.getTreeView(0, true), CommandType.result);
                }
                else {
                    printer.println(Command.getTreeView(0, false), CommandType.result);
                }
            } else {
                if (args.contains(showSize)) {
                    printer.println(String.format("\"%s\" can be used only when \"%s\" is used", showSize.command, doPrint.command), CommandType.error);
                }
            }

        }
    },
    del (
        new String[] {
                String.format("%s -> deletes the specified file.", "del"),
        }, path, doPrint, showSize, questionAnswerYes, questionAnswerNo
    ) {
        @Override
        public void run(String s) {

            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            Boolean doDelete = null;

            if (args.contains(questionAnswerYes) && args.contains(questionAnswerNo)) {
                throw new IllegalArgumentException(String.format("You can not place both \"%s\" and \"%s\" arguments together.", questionAnswerYes, questionAnswerNo));
            }

            if (args.contains(questionAnswerYes)) {
                doDelete = true;
            }
            if (args.contains(questionAnswerNo)) {
                doDelete = false;
            }

            if (!args.contains(path)) {
                pathArgument = new PathArgument(explorer.getChildren().get(0).getPath().toAbsolutePath().toString());
            }

            if (doDelete == null) {
                doDelete = reader.getYesOrNo(String.format("Do you want to delete %s at path \"%s\" [Y/n]", Files.isDirectory(pathArgument.path) ? "folder": "file",pathArgument.path.toString()));
            }

            if (doDelete){
                try {
                    ElementFactory.get(pathArgument.path).delete();
                    printer.println(String.format("Element at path \"%s\" successfully deleted.", pathArgument.path), CommandType.result);
                    printer.println("");
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

            if (args.contains(doPrint)) {
                if (args.contains(showSize))
                    printer.println(Command.getTreeView(0, true), CommandType.result);
                else
                    printer.println(Command.getTreeView(0, false), CommandType.result);
            } else {
                if (args.contains(showSize)) {
                    printer.println(String.format("\"%s\" can be used only when \"%s\" is used", showSize.command, doPrint.command), CommandType.error);
                }
            }

        }
    },
    copy (
        new String[] {
                String.format("%s -> copies the specified file and puts it into the buffer.", "copy"),
        }, path, doPrint, showSize
    ) {
        @Override
        public void run(String s) {
            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            if (args.contains(path)) {
                copiedPath = new PathArgument(pathArgument.path.toString());
            }
            else {
                if (!explorer.getChildren().isEmpty())
                    copiedPath = new PathArgument(explorer.getChildren().get(0).getPath().toAbsolutePath().toString());
            }
            printer.println(String.format("Successfully copied element at path \"%s\"", copiedPath.path.toString()), CommandType.result);
            printer.println("");

            deleteAfterPaste = false;

            if (args.contains(doPrint)) {
                if (args.contains(showSize))
                    printer.println(Command.getTreeView(0, true), CommandType.result);
                else
                    printer.println(Command.getTreeView(0, false), CommandType.result);
            } else {
                if (args.contains(showSize)) {
                    printer.println(String.format("\"%s\" can be used only when \"%s\" is used", showSize.command, doPrint.command), CommandType.error);
                }
            }

        }
    },
    cut (
        new String[] {
                String.format("%s -> copies the specified file and removes it as soon as it is pasted.", "cut"),
        }, path, doPrint, showSize
    ) {
        @Override
        public void run(String s) {

            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            if (args.contains(path)) {
                copiedPath = new PathArgument(pathArgument.path.toString());
            }
            else {
                if (!explorer.getChildren().isEmpty())
                    copiedPath = new PathArgument(explorer.getChildren().get(0).getPath().toAbsolutePath().toString());
            }
            printer.println(String.format("Successfully copied element at path \"%s\"", copiedPath.path.toString()), CommandType.result);
            printer.println("");

            deleteAfterPaste = true;

            if (args.contains(doPrint)) {
                if (args.contains(showSize))
                    printer.println(Command.getTreeView(0, true), CommandType.result);
                else
                    printer.println(Command.getTreeView(0, false), CommandType.result);
            } else {
                if (args.contains(showSize)) {
                    printer.println(String.format("\"%s\" can be used only when \"%s\" is used", showSize.command, doPrint.command), CommandType.error);
                }
            }

        }
    },
    paste (
        new String[] {
                String.format("%s -> pastes the file that has been copied.", "paste"),
        }, path, doPrint, showSize, questionAnswerYes, questionAnswerNo
    ) {
        @Override
        public void run(String s) {
            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            if (args.contains(path)) {
                if (Files.isRegularFile(pathArgument.path)) {
                    pathArgument = new PathArgument(pathArgument.path.getParent().toString());
                }
            }
            else {
                pathArgument = new PathArgument(explorer.getPath().toAbsolutePath().toString());
            }

            var pastePath = pathArgument.path.resolve(copiedPath.path.getFileName());
            var canPlace = !Files.exists(pastePath);
            Boolean predefinedAnswer = null;

            if (args.contains(questionAnswerYes) && args.contains(questionAnswerNo)) {
                throw new IllegalArgumentException(String.format("You can not place both \"%s\" and \"%s\" arguments together.", questionAnswerYes, questionAnswerNo));
            }
            if(args.contains(questionAnswerYes)) {
                predefinedAnswer = true;
            }
            if (args.contains(questionAnswerNo)) {
                predefinedAnswer = false;
            }

            if (!canPlace && predefinedAnswer == null) {
                canPlace = reader.getYesOrNo(String.format("\"%s\" already exists. Do you want to replace it? [Y/n]", pastePath.toString() ));


                if (canPlace && deleteAfterPaste) {

                    canPlace = reader.getYesOrNo(String.format("Are you sure that you want to delete the file at \"%s\"? [Y/n]", copiedPath.path.toString() ));
                }
            }

            if (canPlace || (predefinedAnswer != null && predefinedAnswer.equals(Boolean.TRUE))) {

                try {

                    Files.walk(copiedPath.path).forEach(path -> {
                        var ppath = pastePath.resolve(copiedPath.path.relativize(path));
                        try {
                            if (Files.isDirectory(path)) {
                                if (!Files.exists(ppath)) {
                                    Files.createDirectory(ppath);
                                }
                            } else {
                                Files.copy(path, ppath, StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });

                    if (deleteAfterPaste) {
                        ElementFactory.get(copiedPath.path).delete();
                        printer.println(String.format("Successfully deleted the file \"%s\".", copiedPath.path.toString()), CommandType.result);
                    }
                    printer.println(String.format("Successfully pasted a file to \"%s\".", pastePath.toString()), CommandType.result);

                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            else {
                printer.println("Didn't paste contents.", CommandType.error);
                return;
            }

            if (args.contains(doPrint)) {
                if (args.contains(showSize))
                    printer.println(Command.getTreeView(0, true), CommandType.result);
                else
                    printer.println(Command.getTreeView(0, false), CommandType.result);
            } else {
                if (args.contains(showSize)) {
                    printer.println(String.format("\"%s\" can be used only when \"%s\" is used", showSize.command, doPrint.command), CommandType.error);
                }
            }

        }
    },
    info (
        new String[] {
                String.format("%s -> shows the folder/file info.", "info"),
        }, path
    ) {
        @Override
        public void run(String s) {
            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            if (!args.contains(path)) {
                pathArgument = new PathArgument("");
            }

            var res = new ArrayList<String>();
            var type = "";
            if (Files.isRegularFile(pathArgument.path)) {
                type = "file";
            } else if (Files.isDirectory(pathArgument.path)) {
                type = "directory";
            }

            try {
                var len = 20;
                var loc = "Location";
                var Type = "Type";
                var size = "Size";
                var folderCount = 0l;
                var fileCount = 0l;
                var created = "Created";
                var modified = "Last modified";
                var access = "Last accessed";
                var attributes = Files.readAttributes(pathArgument.path, BasicFileAttributes.class);

                res.add(String.format("%s%s: %s", Type, " ".repeat(len - Type.length()), type));
                res.add(String.format("%s%s: %s",loc, " ".repeat(len - loc.length()), pathArgument.path.toString()));
                res.add(String.format("%s%s: %s bits", size, " ".repeat(len - size.length()), ElementFactory.get(pathArgument.path).getSize()));
                if (type == "directory") {
                    var children = ((Folder) ElementFactory.get(pathArgument.path)).getChildren();
                    for (var child : children) {
                        if (child instanceof File) {
                            fileCount++;
                        }
                        if (child instanceof Folder) {
                            folderCount++;
                        }
                    }
                    var contains = "Contains";
                    res.add(String.format("%s%s: %d Files, %d Folders", contains, " ".repeat(len - contains.length()), fileCount, folderCount));
                }
                res.add("-----------------------------------------------------");
                res.add(String.format("%s%s: %s", created, " ".repeat(len - created.length()), attributes.creationTime().toString().replace("T", " ")));
                res.add(String.format("%s%s: %s", modified, " ".repeat(len - modified.length()), attributes.lastModifiedTime().toString().replace("T", " ")));
                res.add(String.format("%s%s: %s", access, " ".repeat(len - access.length()), attributes.lastAccessTime().toString().replace("T", " ")));

                printer.println(res, CommandType.result);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }


        }
    },
    run (
        new String[] {
                String.format("%s -> runs a file.", "run"),
        }, path
    ) {
        @Override
        public void run(String s) {

            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            if (args.contains(path)) {
                copiedPath = new PathArgument(pathArgument.path.toString());
            }
            else {
                if (!explorer.getChildren().isEmpty())
                    copiedPath = new PathArgument(explorer.getChildren().get(0).getPath().toAbsolutePath().toString());
            }

            var thread = new Thread(() -> {
                var desktop = Desktop.getDesktop();
                try {
                    desktop.open(pathArgument.path.toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
            thread.start();
        }
    },
    tree (
        new String[] {
                String.format("%s -> shows the folder and file tree of the specified folder.", "tree"),
        }, path, dontPrint, showSize
    ) {
        @Override
        public void run(String s) {

            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            if (args.contains(dontPrint)) {
                return;
            }

            if (args.contains(path)) {
                if (Files.isRegularFile(pathArgument.path)) {
                    pathArgument = new PathArgument(pathArgument.path.getParent().toString());
                }
            } else {
                pathArgument = new PathArgument("");
            }

            printer.println(Command.getTreeView(0, args.contains(showSize)), CommandType.result);

        }
    },
    rename (
        new String[] {
                String.format("%s -> renames the specified folder/file.", "rename"),
        }, path, doPrint, showSize
    ) {
        @Override
        public void run(String s) {
            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            if (args.contains(path)) {
                pathArgument = new PathArgument(pathArgument.path.toString());
            }
            else {
                if (!explorer.getChildren().isEmpty())
                    pathArgument = new PathArgument(explorer.getChildren().get(0).getPath().toAbsolutePath().toString());
            }

            var newName = reader.readLine("Enter new name");
            var arr = pathArgument.path.getFileName().toString().split("\\.");
            var extension = arr.length > 1 ? "." + arr[1] : "";
            var newLocation = pathArgument.path.getParent().resolve(newName + extension);


            try{


                Files.move(pathArgument.path, newLocation);

//                Files.walk(pathArgument.path)
//                        .sorted(Comparator.reverseOrder())
//                        .forEach(child -> {
//                            var relative = pathArgument.path.relativize(child);
//                            try {
//                                Files.copy(newLocation.resolve(child), newLocation.resolve(relative));
//                                Files.delete(child);
//                            } catch (IOException e) {
//                                throw new RuntimeException(e.getMessage());
//                            }
//                        });

            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

            if (args.contains(doPrint)) {
                if (args.contains(showSize))
                    printer.println(Command.getTreeView(0, true), CommandType.result);
                else
                    printer.println(Command.getTreeView(0, false), CommandType.result);
            } else {
                if (args.contains(showSize)) {
                    printer.println(String.format("\"%s\" can be used only when \"%s\" is used", showSize.command, doPrint.command), CommandType.error);
                }
            }

        }
    },
    size (
        new String[] {
                String.format("%s -> shows the size of the specified folder/file.", "size"),
        }, path
    ) {
        @Override
        public void run(String s) {
            var args = Arguments.getArguments(s);
            areArgumentsSupported(args);

            if (args.contains(path)) {
                pathArgument = new PathArgument(pathArgument.path.toString());
            }
            else {
                pathArgument = new PathArgument("");
            }

            try {
                printer.println(String.format("Size of \"%s\" is %d", pathArgument.path.getFileName(), ElementFactory.get(pathArgument.path).getSize()), CommandType.result);

            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

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

    private static final String continueAddStr = "├─ "; // continue, add string
    private static final String continueStr = "│  "; // continue string
    private static final String closeStr = "└─ "; // close string

    private static List<String> getTreeView (int depth, boolean showSize) {

        if (showSize) return getSizedTreeView(depth, 0, explorer.getCurrentFolder());

        return getTreeView(depth, 0, explorer.getCurrentFolder());
    }

    private static List<String> getSizedTreeView (int depth, int currentDepth, Folder folder) {
        if (currentDepth > depth) {return null;}

        var res = new ArrayList<String>();

        res.add(folder.getName());
        var children = folder.getChildren();
        children.sort(Comparator.comparing(Element::getName));
        var size = children.size();

        for (int i = 0; i < size; i++) {

            var child = children.get(i);

            if (child instanceof Folder) {
                child = (Folder) child;
                if (currentDepth == depth && i == size - 1) {
                    res.add(closeStr + child.getName() + " " + child.getSize());
                } else if (currentDepth == depth) {
                    res.add(continueAddStr + child.getName() + " " + child.getSize());
                } else {
                    var folderChildren = getSizedTreeView(depth, currentDepth + 1, (Folder) child);
                    if (folderChildren != null) {
                        var folderChildrenSize = folderChildren.size();

                        for (int j = 0; j < folderChildrenSize; j++) {
                            if (j == 0)
                                res.add(continueStr.repeat(currentDepth) + continueAddStr + folderChildren.get(j));
                            else res.add(continueStr.repeat(currentDepth+1) + folderChildren.get(j));
                        }
                    }
                }
            }
            else if (child instanceof File) {
                if (i == size - 1) {
                    res.add(closeStr + child.getName() + " " + child.getSize());
                } else {
                    res.add(continueAddStr + child.getName() + " " + child.getSize());
                }
            }
            else {
                throw new UnsupportedOperationException(child.getClass().getName() + " is not supported.");
            }

        }
        return res;
    }

    private static List<String> getTreeView (int depth, int currentDepth, Folder folder) {
        if (currentDepth > depth) {return null;}

        var res = new ArrayList<String>();

        res.add(folder.getName());
        var children = folder.getChildren();
        children.sort(Comparator.comparing(Element::getName));
        var size = children.size();

        for (int i = 0; i < size; i++) {

            var child = children.get(i);

            if (child instanceof Folder) {
                if (currentDepth == depth && i == size - 1) {
                    res.add(closeStr + child.getName());
                } else if (currentDepth == depth) {
                    res.add(continueAddStr + child.getName());
                } else {
                    var folderChildren = getTreeView(depth, currentDepth + 1, (Folder) child);
                    if (folderChildren != null) {
                        var folderChildrenSize = folderChildren.size();

                        for (int j = 0; j < folderChildrenSize; j++) {
                            if (j == 0)
                                res.add(continueStr.repeat(currentDepth) + continueAddStr + folderChildren.get(j));
                            else res.add(continueStr.repeat(currentDepth+1) + folderChildren.get(j));
                        }
                    }
                }
            }
            else if (child instanceof File) {
                if (i == size - 1) {
                    res.add(closeStr + child.getName());
                } else {
                    res.add(continueAddStr + child.getName());
                }
            }
            else {
                throw new UnsupportedOperationException(child.getClass().getName() + " is not supported.");
            }

        }
        return res;
    }

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

