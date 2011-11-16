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
public class UdtStructure implements ObjectInfo {
    private String udtName;
    private String udtType;
    private Integer length;
    private String precision;
    private String scale;
    private Boolean allowNulls;
    private String defaultName;
    private String ruleName;
    private Boolean identity;


    public List<String> getColumnInfos() {
        List<String> infos = new ArrayList<String>();
        infos.add(udtName);
        infos.add(udtType);
        infos.add(Integer.toString(length));
        infos.add(precision);
        infos.add(scale);
        infos.add(Boolean.toString(allowNulls));
        infos.add(defaultName);
        infos.add(ruleName);
        infos.add(Boolean.toString(identity));
        return infos;
    }


    public String getUdtName() {
        return udtName;
    }


    public String getUdtType() {
        return udtType;
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


    public String getDefaultName() {
        return defaultName;
    }


    public String getRuleName() {
        return ruleName;
    }


    public Boolean getIdentity() {
        return identity;
    }


    public void setUdtName(String udtName) {
        this.udtName = udtName;
    }


    public void setUdtType(String udtType) {
        this.udtType = udtType;
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


    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }


    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }


    public void setIdentity(Boolean identity) {
        this.identity = identity;
    }
}
