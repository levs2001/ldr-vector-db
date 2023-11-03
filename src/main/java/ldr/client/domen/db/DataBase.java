package ldr.client.domen.db;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ldr.client.domen.collection.IVectorCollection;
import ldr.client.domen.collection.VectorCollection;

public class DataBase implements IDataBase {
    private static final Logger log = LoggerFactory.getLogger(DataBase.class);
    private static final String META_FILENAME = "meta.json";
    private final Path location;
    private final Map<String, Path> collectionLocations;
    private final Map<String, IVectorCollection> collections;

    private volatile boolean closed;

    public static DataBase load(Path location) throws IOException {
        Map<String, Path> collectionLocations = loadCollectionLocations(location.resolve(META_FILENAME));
        Map<String, IVectorCollection> collections = new ConcurrentHashMap<>(collectionLocations.size());

        for (var entry : collectionLocations.entrySet()) {
            collections.put(entry.getKey(), VectorCollection.load(new VectorCollection.Config(entry.getValue())));
        }

        return new DataBase(location, collectionLocations, collections);
    }

    private DataBase(Path location, Map<String, Path> collectionLocations, Map<String, IVectorCollection> collections) {
        this.location = location;
        this.collectionLocations = collectionLocations;
        this.collections = collections;
    }

    @Override
    public IVectorCollection getCollection(String name) {
        return collections.get(name);
    }

    @Override
    public void createCollection(String name, int vectorLen) throws IOException {
        // Проверка на то, что такой коллекции нет, если есть выкидываем exception
        if (collections.containsKey(name)) {
            throw new KeyAlreadyExistsException("Collection with name " + name + " already exists");
        }

        Path collectionLocation = location.resolve(name);
        IVectorCollection collection = VectorCollection.load(new VectorCollection.Config(collectionLocation, vectorLen));
        collectionLocations.put(name, collectionLocation);
        collections.put(name, collection);
        log.info("Collection: {} with vector length: {} created.", name, vectorLen);
    }

    // TODO: test it, hardDriveEmbeddings не чистят замапленную память, надо понять проблема ли это.
    @Override
    public void removeCollection(String name) throws IOException {
        var collection = removeFromMap(name);
        collection.close();
        FileUtils.deleteDirectory(collectionLocations.remove(name).toFile());
        log.info("Removed collection {}.", name);
    }

    @Override
    public void renameCollection(String oldName, String newName) {
        var collection = removeFromMap(oldName);
        collections.put(newName, collection);
        log.info("Collection {} renamed to {}.", oldName, newName);
    }

    private IVectorCollection removeFromMap(String name) {
        IVectorCollection collection = collections.remove(name);
        if (collection == null) {
            throw new NoSuchElementException("Can't find collection with name: " + name);
        }
        return collection;
    }

    @Override
    public void close() throws IOException {
        checkClose();
        closed = true;
        for (var collection : collections.values()) {
            collection.close();
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(location.resolve(META_FILENAME).toFile(), collectionLocations);

        log.info("Database successfully closed.");
    }

    private static Map<String, Path> loadCollectionLocations(Path metaFile) {
        if (Files.exists(metaFile)) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ConcurrentHashMap<String, Path> index =  mapper.readValue(metaFile.toFile(), new TypeReference<>() {
                });
                log.info("Index found. Initialization from file.");
                return index;
            } catch (IOException e) {
                log.info("Can't find index file, it will be created.");
            }
        }

        return new ConcurrentHashMap<>();
    }

    private void checkClose() {
        if (closed) {
            throw new RuntimeException("Database already closed");
        }
    }
}
