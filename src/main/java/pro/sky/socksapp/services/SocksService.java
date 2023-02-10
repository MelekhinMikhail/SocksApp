package pro.sky.socksapp.services;

import pro.sky.socksapp.models.enums.Color;
import pro.sky.socksapp.models.Sock;

public interface SocksService {
    boolean addSocks(Sock sock);

    boolean editSock(Sock sock);

    int getCountOfSocksByParamMin(Color color, double size, int cottonMin);

    int getCountOfSocksByParamMax(Color color, double size, int cottonMax);

    int getCountOfSocksByParamMinAndMax(Color color, double size, int cottonMin, int cottonMax);

    int getCountOfSocksByParam(Color color, double size);

    void readFromSocksFile();
}
