package ldr.client.domen.db;

public interface IDataBase {
    void getCollection(String name) throws DataBaseException;

    void createCollection(String name, long dimension, Metric metric) throws DataBaseException;

    void deleteCollection(String name) throws DataBaseException;

    void renameCollection(String oldName, String newName) throws DataBaseException;
}
