package pro.sky.socksapp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pro.sky.socksapp.models.Operation;
import pro.sky.socksapp.models.Sock;
import pro.sky.socksapp.services.FilesService;
import pro.sky.socksapp.services.OperationsService;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@Service
public class OperationsServiceImpl implements OperationsService {

    private static LinkedHashSet<Operation> operations = new LinkedHashSet<>();

    private final FilesService filesService;

    public OperationsServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        readFromOperationsFile();
    }

    @Override
    public void addOperation(Operation operation) {
        operations.add(operation);
        saveToOperationsFile();
    }

    @Override
    public boolean deleteOperation(Operation operation) {
        if (operations.contains(operation)) {
            operations.remove(operation);
            saveToOperationsFile();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public LinkedHashSet<Operation> getOperations() {
        readFromOperationsFile();
        return operations;
    }

    @Override
    public List<Operation> getOperationsByDate(int year, int month, int day) {
        List<Operation> list = new LinkedList<>();
        Calendar date;
        for (Operation operation : operations) {
            date = operation.getDate();
            if (date.get(Calendar.YEAR) == year && date.get(Calendar.MONTH)-1 == month && date.get(Calendar.DAY_OF_MONTH) == day) {
                list.add(operation);
            }
        }
        return list;
    }

    @Override
    public List<Operation> getOperationsBySock(Sock sock) {
        List<Operation> list = new LinkedList<>();
        for (Operation operation : operations) {
            if (operation.getSock().equals(sock)) {
                list.add(operation);
            }
        }
        return list;
    }

    private void readFromOperationsFile() {
        try {
            String json = filesService.readFromOperationsFile();
            if (json == null) {
                return;
            }
            operations = new ObjectMapper().readValue(json, new TypeReference<LinkedHashSet<Operation>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToOperationsFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(operations);
            filesService.saveToOperationsFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
