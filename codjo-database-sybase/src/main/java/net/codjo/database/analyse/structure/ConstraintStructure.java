/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import java.util.ArrayList;
import java.util.List;
/**
 *
 */
public class ConstraintStructure implements ObjectInfo {
    private final String name;
    private final String definition;


    public ConstraintStructure(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }


    public List<String> getCheckInfos() {
        List<String> infos = new ArrayList<String>();
        infos.add(name);
        infos.add(definition);
        return infos;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final ConstraintStructure that = (ConstraintStructure)object;

        return (name.equals(that.name) && definition.equals(that.definition));
    }


    @Override
    public int hashCode() {
        return name.hashCode();
    }


    public String getName() {
        return name;
    }


    public String getDefinition() {
        return definition;
    }


    @Override
    public String toString() {
        return "Constraint{name='" + name + "', definition='" + definition + "'}";
    }
}
