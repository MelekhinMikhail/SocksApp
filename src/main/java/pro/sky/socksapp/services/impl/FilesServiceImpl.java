package pro.sky.socksapp.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.socksapp.models.Sock;
import pro.sky.socksapp.services.FilesService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;

@Service
public class FilesServiceImpl implements FilesService {
    @Value("${path.to.data.file}")
    private String dataFilePath;

    @Value("${name.of.socks.data.file}")
    private String socksFileName;

    @Value("${name.of.operations.data.file}")
    private String operationsFileName;

    @Override
    public boolean cleanSocksFile() {
        try {
            Path path = Path.of(dataFilePath, socksFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cleanOperationsFile() {
        try {
            Path path = Path.of(dataFilePath, operationsFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveToSocksFile(String json) {
        try {
            cleanSocksFile();
            Files.writeString(Path.of(dataFilePath, socksFileName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveToOperationsFile(String json) {
        try {
            cleanOperationsFile();
            Files.writeString(Path.of(dataFilePath, operationsFileName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String readFromSocksFile() {
        try {
            if (Files.exists(Path.of(dataFilePath, socksFileName))) {
                return Files.readString(Path.of(dataFilePath, socksFileName));
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readFromOperationsFile() {
        try {
            if (Files.exists(Path.of(dataFilePath, operationsFileName))) {
                return Files.readString(Path.of(dataFilePath, operationsFileName));
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File getSocksFile() {
        return new File(dataFilePath+"/"+socksFileName);
    }

    @Override
    public File getOperationsFile() {
        return new File(dataFilePath+"/"+operationsFileName);
    }

    @Override
    public Path createTempFile(String suffix) {
        try {
            return Files.createTempFile(Path.of(dataFilePath), "tempFile", suffix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkJSONSocksFile(Path path) {
        try {
            String json = Files.readString(path);
            LinkedHashSet<Sock> socks = new ObjectMapper().readValue(json, new TypeReference<LinkedHashSet<Sock>>() {
            });
            for (Sock sock : socks) {
                if (sock.getSize() < 20 || sock.getSize() > 65 || sock.getSize() % 0.5 != 0) {
                    return false;
                } else if (sock.getCottonPart() < 0 || sock.getCottonPart() > 100) {
                    return false;
                } else if (sock.getQuantity() < 1) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
