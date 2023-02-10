package pro.sky.socksapp.models.enums;

public enum OperationType {
    WRITE_OFF("Списание"), DELIVERY("Выдача"), SUPPLY("Поставка");

    private final String name;

    OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
