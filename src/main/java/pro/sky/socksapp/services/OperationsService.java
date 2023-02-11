package pro.sky.socksapp.services;


import pro.sky.socksapp.models.Operation;
import pro.sky.socksapp.models.Sock;

import java.util.LinkedHashSet;
import java.util.List;

public interface OperationsService {
    void addOperation(Operation operation);

    boolean deleteOperation(Operation operation);

    LinkedHashSet<Operation> getOperations();

    List<Operation> getOperationsByDate(int year, int month, int day);

    List<Operation> getOperationsBySock(Sock sock);
}
