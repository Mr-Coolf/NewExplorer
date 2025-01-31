package ExplorerLogic.Elements;

import ExplorerLogic.Elements.Interfaces.ElementInterface;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Element implements ElementInterface {
    Path path;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;
        return path.equals(element.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String getName() {
        return path.getFileName().toString();
    }

    @Override
    public Path getPath() {
        return Paths.get(path.toString());
    }

    @Override
    public Folder getRootFolder() {
        return new Folder(path.getRoot());
    }

    @Override
    public Folder getParentFolder() {
        return new Folder(path.getParent());
    }
}
