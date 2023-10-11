package ldr.client.domen.db;

public class DataBase implements IDataBase {
    private DataBase() {

    }

    public static IDataBase create() {
        return new DataBase();
    }

    // TODO: Exception if not exists.
    public static IDataBase load() throws DataBaseException {
        return new DataBase();
    }

    @Override
    public void getCollection(String name) throws DataBaseException {

    }

    @Override
    public void createCollection(String name, int dimension, Metric metric) throws DataBaseException {
        // Проверка на то, что такой коллекции нет, если есть выкидываем exception

        // В файловой системе создается новая папка с таким name
        // В этой папке будет мета файл
        // В мета файле прописываем размер векторов (dimension)
        // Метрику
        //
    }

    @Override
    public void deleteCollection(String name) throws DataBaseException {

    }

    @Override
    public void renameCollection(String oldName, String newName) throws DataBaseException {

    }

    @Override
    public void close() throws DataBaseException {

    }
}
