package net.codjo.database.common.impl.sqlfield;
import net.codjo.database.common.api.SQLFieldList;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class DefaultSQLFieldList implements SQLFieldList {
    private Map<String, SQLField> hashTable = new HashMap<String, SQLField>();


    public DefaultSQLFieldList() {
    }


    /**
     * Sets the FieldValue attribute of the SQLFieldList structure
     *
     * @param dbFieldName nom physique du champs
     * @param obj         La nouvelle valeur du champs.
     */
    public void setFieldValue(String dbFieldName, Object obj) {
        getField(dbFieldName).setValue(obj);
    }


    /**
     * Sets the FieldValue attribute of the SQLFieldList structure (for Date value). Converts a java.util.Date
     * into java.sql.Date .
     *
     * @param dbFieldName nom physique du champs
     * @param dateValue   La nouvelle date du champs.
     */
    public void setFieldValue(String dbFieldName, Date dateValue) {
        getField(dbFieldName).setValue(dateValue);
    }


    /**
     * Gets the FieldType attribute of the SQLFieldList structure
     *
     * @param dbFieldName nom physique du champs
     *
     * @return Le type SQL du champs (tel que definie dans Types)
     *
     * @see java.sql.Types
     */
    public int getFieldType(String dbFieldName) {
        return getField(dbFieldName).getSQLType();
    }


    /**
     * Gets the FieldValue attribute of the SQLFieldList structure
     *
     * @param dbFieldName nom physique du champs
     *
     * @return La valeur du champs
     */
    public Object getFieldValue(String dbFieldName) {
        return getField(dbFieldName).getValue();
    }


    /**
     * Gets the SortedDBFieldNameList attribute of the SQLFieldList structure
     *
     * @return La liste triée des noms physiques de la table courante.
     */
    public List<String> getSortedDBFieldNameList() {
        List<String> listField = new ArrayList<String>(hashTable.keySet());
        Collections.sort(listField);
        return listField;
    }


    /**
     * Fusionne les éléments de list dans cette SQLFieldList.
     *
     * @param list La liste à ajouter
     */
    public void addAll(SQLFieldList list) {
        for (SQLField sqlField : list.getValues()) {
            hashTable.put(sqlField.getName(), sqlField);
        }
    }


    /**
     * Ajoute un SQLField de type Bit.
     *
     * @param dbFieldName nom physique du champs
     */
    public void addBitField(String dbFieldName) {
        hashTable.put(dbFieldName, new SQLField(Types.BIT, dbFieldName));
    }


    /**
     * Ajoute un SQLField de type specifie.
     *
     * @param dbFieldName nom physique du champs
     * @param sqlType     Le type SQL du champs.
     *
     * @see java.sql.Types
     */
    public void addField(String dbFieldName, int sqlType) {
        if (sqlType == java.sql.Types.DATE
            || sqlType == java.sql.Types.TIME
            || sqlType == java.sql.Types.TIMESTAMP) {
            hashTable.put(dbFieldName, new SQLDateField(sqlType, dbFieldName));
        }
        else {
            hashTable.put(dbFieldName, new SQLField(sqlType, dbFieldName));
        }
    }


    /**
     * Ajoute un SQLField de type Float.
     *
     * @param dbFieldName nom physique du champs
     */
    public void addFloatField(String dbFieldName) {
        hashTable.put(dbFieldName, new SQLField(Types.FLOAT, dbFieldName));
    }


    /**
     * Ajoute un SQLField de type Integer.
     *
     * @param dbFieldName nom physique du champs
     */
    public void addIntegerField(String dbFieldName) {
        hashTable.put(dbFieldName, new SQLField(Types.INTEGER, dbFieldName));
    }


    /**
     * Ajoute un SQLField de type String.
     *
     * @param dbFieldName nom physique du champs
     */
    public void addStringField(String dbFieldName) {
        hashTable.put(dbFieldName, new SQLField(Types.VARCHAR, dbFieldName));
    }


    /**
     * Efface toutes les valeurs contenue dans les SQLField.
     */
    public void clearValues() {
        for (SQLField field : hashTable.values()) {
            field.setValue(null);
        }
    }


    public Collection<SQLField> getValues() {
        return hashTable.values();
    }


    /**
     * Retourne un iterator sur les noms de champs.
     *
     * @return Description of the Returned Value
     */
    public Iterator<String> fieldNames() {
        return hashTable.keySet().iterator();
    }


    /**
     * Retourne un ensemble contenant les noms de champs.
     *
     * @return Description of the Returned Value
     */
    public Set<String> fieldNamesSet() {
        return hashTable.keySet();
    }


    /**
     * Enleve un SQLField de la liste. Si la colonne n'existe pas, la liste n'est pas modifiee.
     *
     * @param dbFieldName nom physique du champs
     */
    public void removeField(String dbFieldName) {
        hashTable.remove(dbFieldName);
    }


    /**
     * Retourne le nombre de champs.
     *
     * @return Description of the Returned Value
     */
    public int size() {
        return hashTable.size();
    }


    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("SQLFieldList(");

        for (Iterator<String> iter = fieldNames(); iter.hasNext();) {
            String obj = iter.next();
            buffer.append(obj);
            if (iter.hasNext()) {
                buffer.append(", ");
            }
        }

        return buffer.append(")").toString();
    }


    private SQLField getField(String dbFieldName) {
        SQLField field = hashTable.get(dbFieldName);
        if (field == null) {
            throw new java.util.NoSuchElementException(dbFieldName);
        }
        return field;
    }
}
