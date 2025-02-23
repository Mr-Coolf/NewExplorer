package ExplorerLogic.Elements;

import ExplorerLogic.Elements.Interfaces.FolderInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Folder extends Element implements FolderInterface {

    public Folder() {
        path = Paths.get("").toAbsolutePath();
    }
    public Folder(Path path) {
        if (path == null) {
            throw new NullPointerException("Path cannot be null");
        }
        this.path = path;
    }

    @Override
    public List<Folder> getChildrenFolders() {
        var folders = new ArrayList<Folder>();
        
        // Populating folders variable with children folders
        
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    folders.add(new Folder(dir));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return folders;
    }

    @Override
    public List<File> getChildrenFiles() {
        var files = new ArrayList<File>();

        // Populating files variable with children files

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    files.add(new File(file));
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return files;
    }

    @Override
    public List<Element> getChildren() {
        var children = new ArrayList<Element>();

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs){
                    children.add(new Folder(dir));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    children.add(new File(file));
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return children;
    }

    @Override
    public Optional<Element> contains(String name) {
        var newPath = path.resolve(name);
        try {
            return Files.exists(newPath) ? Optional.of(ElementFactory.get(newPath)) : Optional.empty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Element> contains(Path name) {
        var newPath = path.resolve(name);
        try {
            return Files.exists(newPath) ? Optional.of(ElementFactory.get(newPath)) : Optional.empty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Folder getChildrenFolder(String name) throws IOException {
        var newPath = path.resolve(name);
        var element = contains(name);
        if (element.isPresent() && element.get().getClass().equals(Folder.class)) {
            return new Folder(newPath);
        }
        if (element.isPresent() && element.get().getClass().equals(File.class)){
            throw new IOException("Can not open a file.");
        }
        throw new FileNotFoundException(newPath + " not found");
    }

    @Override
    public Folder getChildrenFolder(Path name) throws IOException {
        var newPath = path.resolve(name);
        var element = contains(name);
        if (element.isPresent() && element.get().getClass().equals(Folder.class)) {
            return new Folder(newPath);
        }
        if (element.isPresent() && element.get().getClass().equals(File.class)){
            throw new IOException("Can not open a file.");
        }
        throw new FileNotFoundException(newPath + " not found");
    }
}
