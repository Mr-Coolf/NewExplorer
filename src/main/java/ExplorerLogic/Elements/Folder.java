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
    public List<Element> getAllChildren() {
        var children = new ArrayList<Element>();

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs){
                    if (dir.getParent().equals(path)) children.add(new Folder(dir));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.getParent().equals(path)) children.add(new File(file));
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return children;
    }

    //    @Override
//    public List<Folder> getChildrenFolders() {
//        var folders = new ArrayList<Folder>();
//
//        // Populating folders variable with children folders
//
//        try {
//            Files.walkFileTree(path, new SimpleFileVisitor<>() {
//                @Override
//                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
//                    if (dir.getParent().equals(path)) folders.add(new Folder(dir));
//                    return FileVisitResult.CONTINUE;
//                }
//
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
//                    return FileVisitResult.CONTINUE;
//                }
//            });
//        }
//        catch (IOException e) {
//            System.err.println(e.getMessage());
//        }
//
//        return folders;
//    }

//    @Override
//    public List<File> getChildrenFiles() {
//        var files = new ArrayList<File>();
//
//        // Populating files variable with children files
//
//        try {
//            Files.walkFileTree(path, new SimpleFileVisitor<>() {
//                @Override
//                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
//                    return FileVisitResult.CONTINUE;
//                }
//
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
//                    if (file.getParent().equals(path)) files.add(new File(file));
//                    return FileVisitResult.CONTINUE;
//                }
//            });
//        }
//        catch (IOException e) {
//            System.err.println(e.getMessage());
//        }
//
//        return files;
//    }

//    @Override
//    public List<Element> getChildren() {
//        var children = new ArrayList<Element>();
//
//        try {
//            Files.walkFileTree(path, new SimpleFileVisitor<>() {
//                @Override
//                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs){
//                    if (dir.getParent().equals(path)) children.add(new Folder(dir));
//                    return FileVisitResult.CONTINUE;
//                }
//
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
//                    if (file.getParent().equals(path)) children.add(new File(file));
//                    return FileVisitResult.CONTINUE;
//                }
//            });
//        }
//        catch (IOException e) {
//            System.err.println(e.getMessage());
//        }
//
//        return children;
//    }

    @Override
    public List<Folder> getChildrenFolders() {
        var res = new ArrayList<Folder>();

        try {
            Files.walk(path, 1)
                    .forEach(path1 -> {
                        try {
                            var elem = ElementFactory.get(path1);
                            if (elem instanceof Folder && !elem.path.equals(path)) { res.add((Folder)elem); }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            return res;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<File> getChildrenFiles() {
        var res = new ArrayList<File>();

        try {
            Files.walk(path, 1)
                    .forEach(path1 -> {
                        try {
                            var elem = ElementFactory.get(path1);
                            if (elem instanceof File) { res.add((File)elem); }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            return res;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Element> getChildren() {
        var res = new ArrayList<Element>();

        try {
            Files.walk(path, 1)
                    .forEach(path1 -> {
                        try {
                            if (!path1.equals(path)) res.add(ElementFactory.get(path1));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            return res;
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    @Override
    public long getSize() {
        var children = getChildren();
        var res = 0l;
        for (var child : children) {
            res += child.getSize();
        }
        return res;
    }

    @Override
    public void delete() {
        try {
            // Walk through the directory tree and delete all files and subdirectories
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // Delete the file
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    // After files in the directory are deleted, delete the directory itself
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("Folder and all its contents deleted successfully!");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
