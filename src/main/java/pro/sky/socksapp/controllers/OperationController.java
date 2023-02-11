package pro.sky.socksapp.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.socksapp.models.Operation;
import pro.sky.socksapp.models.Sock;
import pro.sky.socksapp.services.OperationsService;

import java.time.Month;
import java.util.List;

@RestController
@Tag(name = "Операции", description = "Операции для работы с операциями.")
@RequestMapping("/operation")
public class OperationController {

    private final OperationsService operationsService;

    public OperationController(OperationsService operationsService) {
        this.operationsService = operationsService;
    }


    @io.swagger.v3.oas.annotations.Operation(
            summary = "Получить все операции по дате",
            description = "Эта операция возвращает все операции, которые были по указанной дате."
    )
    @Parameters(value = {
            @Parameter(name = "year", example = "2023", description = "Год"),
            @Parameter(name = "month", description = "Месяц"),
            @Parameter(name = "day", example = "1", description = "Число(день)")
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен, операции найдены."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "По указанной дате операций нет."
            )
    })
    @GetMapping("/date")
    public ResponseEntity<Object> getOperationsByDate(@RequestParam String year, @RequestParam Month month, @RequestParam String day) {
        try {
            List<Operation> list = operationsService.getOperationsByDate(Integer.parseInt(year), month.getValue(), Integer.parseInt(day));
            if (list.size() == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(list);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.toString());
        }
    }


    @io.swagger.v3.oas.annotations.Operation(
            summary = "Получить операции, связанные с указанным носком",
            description = "Эта операция возвращает все операции, которые были связанны с этим типом носком."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен, операции найдены."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "С такими носками операций нет."
            )
    })
    @GetMapping("/sock")
    public ResponseEntity<Object> getOperationsBySock(@RequestBody Sock sock) {
        List<Operation> list = operationsService.getOperationsBySock(sock);
        if (list.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(list);
    }
}
