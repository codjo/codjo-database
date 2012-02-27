package net.codjo.database.common.impl.helper;
import java.util.HashMap;
import java.util.Map;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.DatabaseScriptHelper;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlFieldDefinition;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlTableDefinition;
import net.codjo.database.common.api.structure.SqlTrigger;
public abstract class AbstractDatabaseScriptHelper implements DatabaseScriptHelper {
    private final DatabaseQueryHelper queryHelper;
    private final DatabaseTranscoder databaseTranscoder;
    private final String queryDelimiter;
    private final Map<String, CustomScript> customScripts = new HashMap<String, CustomScript>();
    private boolean legacyMode;
    private String legacyPrefix;


    protected AbstractDatabaseScriptHelper(DatabaseQueryHelper databaseQueryHelper,
                                           DatabaseTranscoder databaseTranscoder,
                                           String queryDelimiter) {
        this.queryHelper = databaseQueryHelper;
        this.databaseTranscoder = databaseTranscoder;
        this.queryDelimiter = queryDelimiter;
    }


    protected DatabaseQueryHelper getQueryHelper() {
        return queryHelper;
    }


    protected DatabaseTranscoder getDatabaseTranscoder() {
        return databaseTranscoder;
    }


    public boolean isLegacyMode() {
        return legacyMode;
    }


    public void setLegacyMode(boolean legacyMode) {
        this.legacyMode = legacyMode;
    }


    public String getLegacyPrefix() {
        return legacyPrefix;
    }


    public void setLegacyPrefix(String legacyPrefix) {
        this.legacyPrefix = legacyPrefix;
    }


    public String getQueryDelimiter() {
        return queryDelimiter;
    }


    public String buildCreateTableScript(SqlTableDefinition tableDefinition) {
        return new StringBuilder()
              .append(buildCreateTableScriptBeginPart(tableDefinition))
              .append(buildCreateTableFieldDefinitionPart(tableDefinition))
              .append(buildCreateTableScriptEndPart(tableDefinition)).toString();
    }


    public String buildCreateIndexScript(SqlIndex index) {
        return new StringBuilder()
              .append(getQueryHelper().buildCreateIndexQuery(index))
              .append(getQueryDelimiter()).append("\n")
              .toString();
    }


    public String buildCreateConstraintScript(SqlConstraint constraint) {
        return new StringBuilder()
              .append(getQueryHelper().buildCreateConstraintQuery(constraint))
              .append(getQueryDelimiter()).append("\n")
              .toString();
    }


    public String buildCustomScript(String customScriptName, Object... parameters) {
        CustomScript customScript = customScripts.get(customScriptName);
        if (customScript == null) {
            return "";
        }
        return customScript.buildScript(parameters);
    }


    public String buildCreateTriggerScript(SqlTrigger trigger) {
        switch (trigger.getType()) {
            case INSERT:
                return createInsertTriggerScript(trigger);
            case UPDATE:
                return createUpdateTriggerScript(trigger);
            case DELETE:
                return createDeleteTriggerScript(trigger);
            case CHECK_RECORD:
                return createCheckRecordTriggerScript(trigger);
            default:
                throw new UnsupportedOperationException();
        }
    }


    protected String buildCreateTableScriptBeginPart(SqlTableDefinition tableDefinition) {
        return new StringBuilder()
              .append("create table ").append(tableDefinition.getName()).append("\n")
              .append("(").toString();
    }


    protected String buildCreateTableFieldDefinitionPart(SqlTableDefinition tableDefinition) {
        StringBuilder script = new StringBuilder();
        int idx = 0;
        for (SqlFieldDefinition fieldDefinition : tableDefinition.getSqlFieldDefinitions()) {
            script.append(buildFieldDefinition(tableDefinition, fieldDefinition));
            if (++idx < tableDefinition.getSqlFieldDefinitions().size()) {
                script.append(",");
            }
        }
        return script.toString();
    }


    protected String buildFieldDefinition(SqlTableDefinition tableDefinition,
                                          SqlFieldDefinition fieldDefinition) {
        StringBuilder script = new StringBuilder()
              .append("\n")
              .append("    ").append(fieldDefinition.getName()).append("      ")
              .append(getDatabaseTranscoder().transcodeSqlFieldType(fieldDefinition.getType()));
        if (fieldDefinition.getPrecision() != null) {
            script.append("(").append(fieldDefinition.getPrecision()).append(")");
        }
        if (tableDefinition.isPkGenerator() && fieldDefinition.isIdentity()) {
            handleFieldIsIdentity(script);
        }
        if (fieldDefinition.getDefault() != null) {
            script.append(" default ")
                  .append(getDatabaseTranscoder().transcodeSqlFieldDefault(fieldDefinition.getDefault()));
        }
        else if ("bit".equals(fieldDefinition.getType())) {
            script.append(" default 0");
        }
        script.append("  ").append(fieldDefinition.isRequired() ? "not null" : "null");
        if (fieldDefinition.getCheck() != null) {
            handleFieldIsCheck(script, fieldDefinition);
        }
        return script.toString();
    }


    protected String buildCreateTableScriptEndPart(SqlTableDefinition tableDefinition) {
        return new StringBuilder()
              .append("\n")
              .append(")").append(getQueryDelimiter()).append("\n")
              .toString();
    }


    protected String createInsertTriggerScript(SqlTrigger trigger) {
        String centralPart = new StringBuilder()
              .append("\n")
              .append(trigger.getSqlContent()).append("\n")
              .append("\n").toString();
        return buildTriggerScript(trigger, centralPart);
    }


    protected String createUpdateTriggerScript(SqlTrigger trigger) {
        String centralPart = new StringBuilder()
              .append("\n")
              .append(trigger.getSqlContent()).append("\n")
              .append("\n").toString();
        return buildTriggerScript(trigger, centralPart);
    }


    protected String createDeleteTriggerScript(SqlTrigger trigger) {
        String centralPart = new StringBuilder()
              .append(buildTriggerScriptDeleteCascadePart(trigger))
              .append("\n")
              .append(trigger.getSqlContent()).append("\n")
              .append("\n").toString();
        return buildTriggerScript(trigger, centralPart);
    }


    protected String createCheckRecordTriggerScript(SqlTrigger trigger) {
        return buildTriggerScript(trigger, buildTriggerScriptCheckRecordPart(trigger));
    }


    protected void registerCustomScript(String customScriptName, CustomScript customScript) {
        customScripts.put(customScriptName, customScript);
    }


    protected abstract String buildTriggerScriptBeginPart(SqlTrigger trigger);


    protected abstract String buildTriggerScriptEndPart(SqlTrigger trigger);


    protected abstract String buildTriggerScriptDeleteCascadePart(SqlTrigger trigger);


    protected abstract String buildTriggerScriptCheckRecordPart(SqlTrigger trigger);


    private String buildTriggerScript(SqlTrigger trigger, String centralPart) {
        return new StringBuilder()
              .append(buildTriggerScriptBeginPart(trigger))
              .append(centralPart)
              .append(buildTriggerScriptEndPart(trigger)).toString();
    }


    protected abstract void handleFieldIsIdentity(StringBuilder script);


    protected abstract void handleFieldIsCheck(StringBuilder script, SqlFieldDefinition fieldDefinition);


    protected interface CustomScript {
        String buildScript(Object... parameters);
    }
}
