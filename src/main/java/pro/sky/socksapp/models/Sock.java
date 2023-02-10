package pro.sky.socksapp.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.socksapp.models.enums.Color;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность носков")
public class Sock {
    @Schema(description = "Цвет")
    private Color color;
    @Schema(description = "Размер")
    private double size;
    @Schema(description = "Соотношение хлопка в процентах")
    private int cottonPart;
    @Schema(description = "Количество пар")
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sock sock = (Sock) o;
        return Double.compare(sock.size, size) == 0 && cottonPart == sock.cottonPart && color == sock.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, size, cottonPart);
    }
}
