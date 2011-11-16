package net.codjo.database.common.repository;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.DatabaseScriptHelper;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import net.codjo.database.common.repository.builder.DatabaseComparatorBuilder;
import net.codjo.database.common.repository.builder.ExecSqlScriptBuilder;
import net.codjo.database.common.repository.builder.JdbcFixtureBuilder;
import net.codjo.database.common.repository.builder.RelationshipBuilder;
import net.codjo.database.common.repository.builder.SQLFieldListBuilder;
import net.codjo.database.common.repository.builder.TableComparatorBuilder;
import org.picocontainer.defaults.DefaultPicoContainer;
public abstract class AbstractDatabaseRepository implements DatabaseRepository {
    private final DefaultPicoContainer picoContainer = new DefaultPicoContainer();


    protected AbstractDatabaseRepository() {
        registerImplementationClass(getDatabaseHelperImplementationClass());
        registerImplementationClass(getDatabaseQueryHelperImplementationClass());
        registerImplementationClass(getDatabaseScriptHelperImplementationClass());
        registerImplementationClass(getDatabaseTranscoderImplementationClass());
        registerImplementationClass(getRelationshipBuilderImplementationClass());
        registerImplementationClass(getSQLFIeldListBuilderImplementationClass());
        registerImplementationClass(getJdbcFixtureBuilderImplementationClass());
        registerImplementationClass(getDatabaseComparatorBuilderImplementationClass());
        registerImplementationClass(getExecSqlScriptBuilderImplementationClass());
        registerImplementationClass(getTableComparatorBuilderImplementationClass());
    }


    @SuppressWarnings("unchecked")
    public <T> T getImplementation(Class<T> aClass) {
        return (T)picoContainer.getComponentInstanceOfType(aClass);
    }


    public void registerImplementationClass(Class aClass) {
        try {
            picoContainer.registerComponentImplementation(aClass);
        }
        catch (Exception e) {
            throw new RuntimeException("Impossible d'enregistrer la classe " + aClass, e);
        }
    }


    protected abstract Class<? extends DatabaseHelper> getDatabaseHelperImplementationClass();


    protected abstract Class<? extends DatabaseQueryHelper> getDatabaseQueryHelperImplementationClass();


    protected abstract Class<? extends DatabaseScriptHelper> getDatabaseScriptHelperImplementationClass();


    protected abstract Class<? extends DatabaseTranscoder> getDatabaseTranscoderImplementationClass();


    protected abstract Class<? extends ExecSqlScriptBuilder> getExecSqlScriptBuilderImplementationClass();


    protected abstract Class<? extends RelationshipBuilder> getRelationshipBuilderImplementationClass();


    protected abstract Class<? extends SQLFieldListBuilder> getSQLFIeldListBuilderImplementationClass();


    protected abstract Class<? extends JdbcFixtureBuilder> getJdbcFixtureBuilderImplementationClass();


    protected abstract Class<? extends DatabaseComparatorBuilder> getDatabaseComparatorBuilderImplementationClass();


    protected abstract Class<? extends TableComparatorBuilder> getTableComparatorBuilderImplementationClass();
}
