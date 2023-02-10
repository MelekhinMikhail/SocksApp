package pro.sky.socksapp.models.enums;

public enum Color {
    RED("Красный"), BLUE("Синий"), YELLOW("Жёлтый"), GREEN("Зелёный"), BLACK("Чёрный"),
    WHITE("Белый"), PINK("Розовый"), PURPLE("Фиолетовый"), ORANGE("Оранжевый");

    private final String name;

    Color(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
