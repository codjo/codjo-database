package net.codjo.database.common.util;
/**
 * @deprecated {@link net.codjo.database.common.api.structure.SqlTrigger}
 */
@Deprecated
public class SqlTrigger extends net.codjo.database.common.api.structure.SqlTrigger {

    public static net.codjo.database.common.api.structure.SqlTrigger triggerName(String triggerName) {
        return net.codjo.database.common.api.structure.SqlTrigger.triggerName(triggerName);
    }


    public static net.codjo.database.common.api.structure.SqlTrigger insertTrigger(String triggerName,
                                                                                 SqlTable table,
                                                                                 String sqlContent) {
        return net.codjo.database.common.api.structure.SqlTrigger.insertTrigger(triggerName, table, sqlContent);
    }


    public static net.codjo.database.common.api.structure.SqlTrigger updateTrigger(String triggerName,
                                                                                 SqlTable table) {
        return net.codjo.database.common.api.structure.SqlTrigger.updateTrigger(triggerName, table);
    }


    public static net.codjo.database.common.api.structure.SqlTrigger updateTrigger(String triggerName,
                                                                                 SqlTable table,
                                                                                 String sqlContent) {
        return net.codjo.database.common.api.structure.SqlTrigger.updateTrigger(triggerName, table, sqlContent);
    }


    public static net.codjo.database.common.api.structure.SqlTrigger deleteTrigger(String triggerName,
                                                                                 SqlTable table) {
        return net.codjo.database.common.api.structure.SqlTrigger.deleteTrigger(triggerName, table);
    }


    public static net.codjo.database.common.api.structure.SqlTrigger deleteTrigger(String triggerName,
                                                                                 SqlTable table,
                                                                                 String sqlContent) {
        return net.codjo.database.common.api.structure.SqlTrigger.deleteTrigger(triggerName, table, sqlContent);
    }


    public static net.codjo.database.common.api.structure.SqlTrigger checkRecordTrigger(String triggerName,
                                                                                      SqlTable table) {
        return net.codjo.database.common.api.structure.SqlTrigger.checkRecordTrigger(triggerName, table);
    }
}
