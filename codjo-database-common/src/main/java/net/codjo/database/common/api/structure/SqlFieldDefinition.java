package net.codjo.database.common.api.structure;
public class SqlFieldDefinition extends AbstractSqlObject {
    private String type;
    private String precision;
    private boolean required;
    private String sqlDefault;
    private boolean identity;
    private String check;


    public SqlFieldDefinition(String name) {
        super(name);
    }


    public void setType(String type) {
        this.type = type;
    }


    public void setPrecision(String precision) {
        this.precision = precision;
    }


    public void setRequired(boolean required) {
        this.required = required;
    }


    public void setDefault(String sqlDefault) {
        this.sqlDefault = sqlDefault;
    }


    public void setIdentity(boolean identity) {
        this.identity = identity;
    }


    public void setCheck(String check) {
        this.check = check;
    }


    public String getType() {
        return type;
    }


    public String getPrecision() {
        return precision;
    }


    public boolean isRequired() {
        return required;
    }


    public String getDefault() {
        return sqlDefault;
    }


    public boolean isIdentity() {
        return identity;
    }


    public String getCheck() {
        return check;
    }
}
