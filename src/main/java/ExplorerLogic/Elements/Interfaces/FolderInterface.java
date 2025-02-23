package ExplorerLogic.Elements.Interfaces;

import ExplorerLogic.Elements.Element;
import ExplorerLogic.Elements.File;
import ExplorerLogic.Elements.Folder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface FolderInterface {

    /**
     *
     * @return <code>List &lt;Folder&gt;</code> with all the first children folders of the current folder
     * @see #getChildrenFiles()
     * @see #getChildren()
     */
    List<Folder> getChildrenFolders();

    /**
     *
     * @return <code>List &lt;File&gt;</code> with all the first children folders of the current folder
     * @see #getChildrenFolders()
     * @see #getChildren()
     */
    List<File> getChildrenFiles();

    /**
     *
     * @return <code>List &lt;Element&gt;</code> with all the children of the current folder
     * @see #getChildrenFolders()
     * @see #getChildrenFiles()
     */
    List<Element> getAllChildren();

    /**
     *
     * @return <code>List &lt;Element&gt;</code> with all the first children folders of the current folder
     * @see #getChildrenFolders()
     * @see #getChildrenFiles()
     */
    List<Element> getChildren();

    /**
     *
     * @param name first child file/folder name of this folder
     * @return <code>Optional &lt;Element&gt;</code> returns an element if it exists and an empty optional if it does not
     *
     */
    Optional<Element> contains(String name);

    /**
     *
     * @param name first child file/folder name of this folder
     * @return <code>Optional &lt;Element&gt;</code> returns an element if it exists and an empty optional if it does not
     *
     */
    Optional<Element> contains(Path name);

    /**
     *
     * @param name first child folder or relative path to new folder from this folder
     * @return new <code>Folder</code> object by the
     * @throws IOException if path doesn't exist or path doesn't lead to a folder
     */
    Folder getChildrenFolder(String name) throws IOException;

    /**
     *
     * @param name first child folder or relative path to new folder from this folder
     * @return new <code>Folder</code> object by the
     * @throws IOException if path doesn't exist or path doesn't lead to a folder
     */
    Folder getChildrenFolder(Path name) throws IOException;

//    /**
//     *
//     * @return new parent <code>Folder</code>
//     */
//    Folder exitFolder();
}
