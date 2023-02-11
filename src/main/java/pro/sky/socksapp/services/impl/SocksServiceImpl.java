package pro.sky.socksapp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pro.sky.socksapp.models.enums.Color;
import pro.sky.socksapp.models.Sock;
import pro.sky.socksapp.services.FilesService;
import pro.sky.socksapp.services.SocksService;

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class SocksServiceImpl implements SocksService {

    private static Set<Sock> storage = new LinkedHashSet<>();
    private final FilesService filesService;

    public SocksServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        readFromSocksFile();
    }

    @Override
    public boolean addSocks(Sock sock) {
        if (sock.getSize() < 20 || sock.getSize() > 65 || sock.getSize() % 0.5 != 0) {
            return false;
        } else if (sock.getCottonPart() < 0 || sock.getCottonPart() > 100) {
            return false;
        } else if (sock.getQuantity() < 1) {
            return false;
        } else {
            for (Sock sock1 : storage) {
                if (sock1.equals(sock)) {
                    sock1.setQuantity(sock1.getQuantity() + sock.getQuantity());
                    saveToSocksFile();
                    return true;
                }
            }
            storage.add(sock);
            saveToSocksFile();
            return true;
        }
    }

    @Override
    public boolean editSock(Sock sock) {
        readFromSocksFile();
        for (Sock sock1 : storage) {
            if (sock1.getColor().equals(sock.getColor()) && sock1.getSize() == sock.getSize() &&
            sock1.getCottonPart() == sock.getCottonPart() && sock1.getQuantity() >= sock.getQuantity() &&
            sock.getQuantity() > 0) {
                sock1.setQuantity(sock1.getQuantity() - sock.getQuantity());
                saveToSocksFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCountOfSocksByParamMin(Color color, double size, int cottonMin) {
        readFromSocksFile();
        if (cottonMin < 0 || cottonMin > 100) {
            throw new IllegalArgumentException("Соотношение хлопка должно быть указано в процентах от 0 до 100");
        }
        int counter = 0;
        for (Sock sock : storage) {
            if (sock.getColor().equals(color) && sock.getSize() == size && sock.getCottonPart() >= cottonMin) {
                counter += sock.getQuantity();
            }
        }
        return counter;
    }

    @Override
    public int getCountOfSocksByParamMax(Color color, double size, int cottonMax) {
        readFromSocksFile();
        if (cottonMax < 0 || cottonMax > 100) {
            throw new IllegalArgumentException("Соотношение хлопка должно быть указано в процентах от 0 до 100.");
        }
        int counter = 0;
        for (Sock sock : storage) {
            if (sock.getColor().equals(color) && sock.getSize() == size && sock.getCottonPart() <= cottonMax) {
                counter += sock.getQuantity();
            }
        }
        return counter;
    }

    @Override
    public int getCountOfSocksByParamMinAndMax(Color color, double size, int cottonMin, int cottonMax) {
        readFromSocksFile();
        if ((cottonMax < 0 || cottonMax > 100) || (cottonMin < 0 || cottonMin > 100)) {
            throw new IllegalArgumentException("Соотношение хлопка должно быть указано в процентах от 0 до 100.");
        }
        int counter = 0;
        for (Sock sock : storage) {
            if (sock.getColor().equals(color) && sock.getSize() == size && sock.getCottonPart() <= cottonMax &&
            sock.getCottonPart() >= cottonMin) {
                counter += sock.getQuantity();
            }
        }
        return counter;
    }

    @Override
    public int getCountOfSocksByParam(Color color, double size) {
        readFromSocksFile();
        int counter = 0;
        for (Sock sock : storage) {
            if (sock.getColor().equals(color) && sock.getSize() == size) {
                counter += sock.getQuantity();
            }
        }
        return counter;
    }

    @Override
    public void readFromSocksFile() {
        try {
            String json = filesService.readFromSocksFile();
            if (json == null) {
                return;
            }
            storage = new ObjectMapper().readValue(json, new TypeReference<LinkedHashSet<Sock>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToSocksFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(storage);
            filesService.saveToSocksFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
