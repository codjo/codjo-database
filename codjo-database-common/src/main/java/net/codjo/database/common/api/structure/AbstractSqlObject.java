package net.codjo.database.common.api.structure;
public abstract class AbstractSqlObject implements SqlObject {
    private String name;


    protected AbstractSqlObject() {
    }


    protected AbstractSqlObject(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
