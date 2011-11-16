/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import java.util.ArrayList;
import java.util.List;
/**
 */
public class ColumnStructure implements ObjectInfo {
    private String columnName;
    private Integer columnPosition;
    private String columnType;
    private Integer length;
    private String precision;
    private String scale;
    private Boolean allowNulls;
    private String defaultValue;
    private String ruleName;
    private Boolean identity;
    private String identityGap;


    public List<String> getColumnInfos() {
        List<String> infos = new ArrayList<String>();
        infos.add(Integer.toString(columnPosition));
        infos.add(columnName);
        infos.add(columnType);
        infos.add(Integer.toString(length));
        infos.add(precision);
        infos.add(scale);
        infos.add(Boolean.toString(allowNulls));
        infos.add(defaultValue);
        infos.add(ruleName);
        infos.add(Boolean.toString(identity));
        infos.add(identityGap);
        return infos;
    }


    public String getColumnName() {
        return columnName;
    }


    public Integer getColumnPosition() {
        return columnPosition;
    }


    public String getColumnType() {
        return columnType;
    }


    public Integer getLength() {
        return length;
    }


    public String getPrecision() {
        return precision;
    }


    public String getScale() {
        return scale;
    }


    public Boolean getAllowNulls() {
        return allowNulls;
    }


    public String getDefaultValue() {
        return defaultValue;
    }


    public String getRuleName() {
        return ruleName;
    }


    public Boolean getIdentity() {
        return identity;
    }


    public String getIdentityGap() {
        return identityGap;
    }


    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }


    public void setColumnPosition(Integer columnPosition) {
        this.columnPosition = columnPosition;
    }


    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }


    public void setLength(Integer length) {
        this.length = length;
    }


    public void setPrecision(String precision) {
        this.precision = precision;
    }


    public void setScale(String scale) {
        this.scale = scale;
    }


    public void setAllowNulls(Boolean allowNulls) {
        this.allowNulls = allowNulls;
    }


    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }


    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }


    public void setIdentity(Boolean identity) {
        this.identity = identity;
    }


    public void setIdentityGap(String identityGap) {
        this.identityGap = identityGap;
    }
}
