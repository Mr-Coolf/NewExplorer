package ExplorerLogic.Elements.Interfaces;

import ExplorerLogic.Elements.Folder;

import java.nio.file.Path;

public interface ElementInterface {
    /**
     *
     * @return
     */
    public String getName();

    public Path getPath();

    public Folder getRootFolder();

    public Folder getParentFolder();

    /**
     *
     * @return int - finds element size
     */
    long getSize();

    /**
     * Deletes current element
     */
    void delete();
}
