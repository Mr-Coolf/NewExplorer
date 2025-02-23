package ExplorerLogic;

import ExplorerLogic.Elements.Element;
import ExplorerLogic.Elements.File;
import ExplorerLogic.Elements.Folder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Explorer {
    private Folder currentFolder;

    public Explorer() {
        currentFolder = new Folder();
    }

    public List<Folder> getChildrenFolders() {
        return currentFolder.getChildrenFolders();
    }

    public List<File> getChildrenFiles() {
        return currentFolder.getChildrenFiles();
    }

    public List<Element> getChildren() {
        return currentFolder.getChildren();
    }

    public boolean contains(String name) {
        return currentFolder.contains(name).isPresent();
    }

    public void openAbsolutePathFolder (Path path) throws IOException {
        if (Files.exists(path) && Files.isDirectory(path)) {
            currentFolder = new Folder(path);
        } else {
            throw new IOException(String.format("No folder exist at specified path: \"%s\"", path.toAbsolutePath()));
        }
    }

    public void openChildrenFolder(String name) throws IOException {
        currentFolder = currentFolder.getChildrenFolder(name);
    }

    public void openChildrenFolder(Path name) throws IOException {
        currentFolder = currentFolder.getChildrenFolder(name);
    }

    public void exitFolder() {
        currentFolder = getParent();
    }

    public Path getPath() {
        return currentFolder.getPath();
    }

    public String getName() {
        return currentFolder.getName();
    }

    public Folder getRoot() {
        return currentFolder.getRootFolder();
    }

    public Folder getParent() {
        return currentFolder.getParentFolder();
    }

    public Folder getCurrentFolder() {
        return new Folder(currentFolder.getPath());
    }

}
