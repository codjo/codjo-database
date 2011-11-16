package net.codjo.database.common.util;
/**
 * @deprecated {@link net.codjo.database.common.api.structure.SqlView}
 */
@Deprecated
public class SqlView extends net.codjo.database.common.api.structure.SqlView {

    public static net.codjo.database.common.api.structure.SqlView viewName(String viewName) {
        return net.codjo.database.common.api.structure.SqlView.viewName(viewName);
    }


    public static net.codjo.database.common.api.structure.SqlView view(String viewName, String body) {
        return net.codjo.database.common.api.structure.SqlView.view(viewName, body);
    }
}
