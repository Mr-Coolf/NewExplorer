package ExplorerLogic;

import ExplorerLogic.Elements.Element;
import ExplorerLogic.Elements.File;
import ExplorerLogic.Elements.Folder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Explorer {
    private Folder root;

    public Explorer() {
        root = new Folder();
    }

    public List<Folder> getChildrenFolders() {
        return root.getChildrenFolders();
    }

    public List<File> getChildrenFiles() {
        return root.getChildrenFiles();
    }

    public List<Element> getChildren() {
        return root.getChildren();
    }

    public boolean contains(String name) {
        return root.contains(name).isPresent();
    }

    public void openFolder(String name) throws IOException {
        root = root.getChildrenFolder(name);
    }

    public void exitFolder() {
        root = getParent();
    }

    public Path getPath() {
        return root.getPath();
    }

    public String getName() {
        return root.getName();
    }

    public Folder getRoot() {
        return root.getRootFolder();
    }

    public Folder getParent() {
        return root.getParentFolder();
    }

}
