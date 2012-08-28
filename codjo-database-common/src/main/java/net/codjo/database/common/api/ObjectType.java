package net.codjo.database.common.api;
public enum ObjectType {
    VIEW("V", "view"),
    TABLE("U", "table"),
    STORED_PROCEDURE("P", "procedure"),
    DEFAULT("D", "default"),
    RULE("R", "rule"),
    TRIGGER("TR", "trigger");

    private final String type;
    private final String name;


    private ObjectType(String type, String name) {
        this.name = name;
        this.type = type;
    }


    @Override
    public String toString() {
        return type;
    }


    public String getName() {
        return name;
    }
}
