package pro.sky.socksapp.services;

import java.io.File;
import java.nio.file.Path;

public interface FilesService {
    boolean cleanSocksFile();

    boolean cleanOperationsFile();

    boolean saveToSocksFile(String json);

    boolean saveToOperationsFile(String json);

    String readFromSocksFile();

    String readFromOperationsFile();

    File getSocksFile();

    File getOperationsFile();

    Path createTempFile(String suffix);

    boolean checkJSONSocksFile(Path path);
}
