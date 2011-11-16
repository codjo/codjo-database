package net.codjo.database.common.repository;
public interface DatabaseRepository {
    <T> T getImplementation(Class<T> aClass);
}
