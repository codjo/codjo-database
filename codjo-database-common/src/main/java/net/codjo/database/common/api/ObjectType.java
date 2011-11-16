package net.codjo.database.common.api;
public final class ObjectType {
    public static final ObjectType VIEW = new ObjectType("V", "view");
    public static final ObjectType TABLE = new ObjectType("U", "table");
    public static final ObjectType STORED_PROCEDURE = new ObjectType("P", "procedure");
    public static final ObjectType DEFAULT = new ObjectType("D", "default");
    public static final ObjectType RULE = new ObjectType("R", "rule");
    public static final ObjectType TRIGGER = new ObjectType("TR", "trigger");
    private final String type;
    private String name;


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
