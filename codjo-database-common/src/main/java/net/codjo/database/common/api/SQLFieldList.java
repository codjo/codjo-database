package net.codjo.database.common.api;
import net.codjo.database.common.impl.sqlfield.SQLField;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
public interface SQLFieldList {
    void setFieldValue(String dbFieldName, Object obj);


    void setFieldValue(String dbFieldName, java.util.Date dateValue);


    int getFieldType(String dbFieldName);


    Object getFieldValue(String dbFieldName);


    List<String> getSortedDBFieldNameList();


    void addAll(SQLFieldList list);


    void addBitField(String dbFieldName);


    void addField(String dbFieldName, int sqlType);


    void addFloatField(String dbFieldName);


    void addIntegerField(String dbFieldName);


    void addStringField(String dbFieldName);


    void clearValues();


    Collection<SQLField> getValues();


    Iterator<String> fieldNames();


    Set<String> fieldNamesSet();


    void removeField(String dbFieldName);


    int size();
}
