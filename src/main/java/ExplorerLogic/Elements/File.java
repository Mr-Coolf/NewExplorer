package ExplorerLogic.Elements;

import java.nio.file.Path;

public class File extends Element {
    Path path;

    File(Path path) {
        this.path = path;
    }

}
