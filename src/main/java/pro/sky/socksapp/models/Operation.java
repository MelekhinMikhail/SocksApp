package pro.sky.socksapp.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.socksapp.models.enums.OperationType;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность операции")
public class Operation {
    @Schema(description = "Тип")
    private OperationType operationType;
    @Schema(description = "Дата")
    private final Calendar date = new GregorianCalendar();
    @Schema(description = "Носки")
    private Sock sock;
}
