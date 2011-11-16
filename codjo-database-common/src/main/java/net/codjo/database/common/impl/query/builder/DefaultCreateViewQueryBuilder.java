package net.codjo.database.common.impl.query.builder;
public class DefaultCreateViewQueryBuilder extends AbstractCreateViewQueryBuilder {

    @Override
    public String get() {
        return new StringBuilder()
              .append("create view ").append(view.getName()).append("\n")
              .append("    as\n")
              .append(view.getBody()).toString();
    }
}
