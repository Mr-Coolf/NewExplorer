package ExplorerLogic.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ElementFactory {
    public static Element get(Path path) throws IOException {
        if (Files.isDirectory(path)) return new Folder(path);
        if (Files.isRegularFile(path)) return new File(path);
        throw new IOException("File not supported");
    }
}
