package ExplorerLogic.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class File extends Element {

    public File(Path path) {
        this.path = path.toAbsolutePath();
    }



}
