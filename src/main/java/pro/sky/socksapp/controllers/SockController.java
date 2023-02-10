package pro.sky.socksapp.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.socksapp.models.enums.Color;
import pro.sky.socksapp.models.Operation;
import pro.sky.socksapp.models.Sock;
import pro.sky.socksapp.services.OperationsService;
import pro.sky.socksapp.services.SocksService;

import static pro.sky.socksapp.models.enums.OperationType.*;

@RestController
@Tag(name = "Носки", description = "CRUD-операции для работы с носками.")
@RequestMapping("/socks")
public class SockController {

    private final SocksService socksService;
    private final OperationsService operationsService;

    public SockController(SocksService socksService, OperationsService operationsService) {
        this.socksService = socksService;
        this.operationsService = operationsService;
    }


    @io.swagger.v3.oas.annotations.Operation(
            summary = "Добавить носки",
            description = "Эта операция позволяет добавить носки на склад."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Носки успешно добавлены на склад."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."
            )
    })
    @PostMapping("/")
    public ResponseEntity<Void> addSocks(@RequestBody Sock sock) {
        Sock sock1 = new Sock(sock.getColor(), sock.getSize(), sock.getCottonPart(), sock.getQuantity());
        if (socksService.addSocks(sock)) {
            operationsService.addOperation(new Operation(SUPPLY, sock1));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @io.swagger.v3.oas.annotations.Operation(
            summary = "Зарегистрировать отпуск носков",
            description = "Эта операция позволяет зарегистрировать отпуск носков со склада."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Удалось произвести отпуск носков со склада."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Товара нет на складе в нужном количестве или параметры запроса имеют некорректный формат."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."
            )
    })
    @PutMapping("/")
    public ResponseEntity<Void> editSocks(@RequestBody Sock sock) {
        if (socksService.editSock(sock)) {
            operationsService.addOperation(new Operation(DELIVERY, sock));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @io.swagger.v3.oas.annotations.Operation(
            summary = "Списать испорченные носки",
            description = "Эта операция позволяет зарегистрировать списание испорченных(бракованных) носков."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен, товар списан со склада."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."
            )
    })
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteSocks(@RequestBody Sock sock) {
        if (socksService.editSock(sock)) {
            operationsService.addOperation(new Operation(WRITE_OFF, sock));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @io.swagger.v3.oas.annotations.Operation(
            summary = "Получить общее количество носков",
            description = "Эта операция возвращает общее количество носков на складе по указанным параметрам."
    )
    @Parameters(value = {
            @Parameter(name = "color", description = "Цвет"),
            @Parameter(name = "size", example = "37.5", description = "Размер"),
            @Parameter(name = "cottonMin", example = "0", description = "Минимальный процент хлопка"),
            @Parameter(name = "cottonMax", example = "100", description = "Максимальный процент хлопка")
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."
            )
    })
    @GetMapping("/")
    public ResponseEntity<Object> getCountOfSocksByParamMin(@RequestParam Color color, @RequestParam String size,
                                                             @RequestParam(required = false) String cottonMin,
                                                             @RequestParam(required = false) String cottonMax) {
        try {
            if (cottonMin != null && cottonMax != null) {
                return ResponseEntity.ok(socksService.getCountOfSocksByParamMinAndMax(color, Double.parseDouble(size),
                        Integer.parseInt(cottonMin), Integer.parseInt(cottonMax)));
            } else if (cottonMin != null) {
                return ResponseEntity.ok(socksService.getCountOfSocksByParamMin(color, Double.parseDouble(size),
                        Integer.parseInt(cottonMin)));
            } else if (cottonMax != null) {
                return ResponseEntity.ok(socksService.getCountOfSocksByParamMax(color, Double.parseDouble(size),
                        Integer.parseInt(cottonMax)));
            } else {
                return ResponseEntity.ok(socksService.getCountOfSocksByParam(color, Double.parseDouble(size)));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
}
