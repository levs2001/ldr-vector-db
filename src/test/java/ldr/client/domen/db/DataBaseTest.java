package ldr.client.domen.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import ldr.client.domen.Embedding;
import ldr.client.domen.collection.IVectorCollection;

import static ldr.server.TestUtils.generateNearEmbeddings;
import static ldr.server.TestUtils.randomInt;
import static ldr.server.TestUtils.resourcesPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DataBaseTest {
    private static final Path databasePath = resourcesPath.resolve("database");

    @Test
    public void testCreateCollection() throws IOException {
        Path location = Files.createTempDirectory(databasePath, "testCreateCollection");
        IDataBase db = DataBase.load(location);
        createAndTestCollection(location, db, "coll1", 10);
        FileUtils.deleteDirectory(location.toFile());
    }

    private List<Embedding> createAndTestCollection(Path location, IDataBase db,
                                                    String collName, int vectorLen) throws IOException {
        db.createCollection(collName, vectorLen);
        IVectorCollection collection = db.getCollection(collName);
        var embeddings = generateNearEmbeddings(100, vectorLen, 10);
        collection.add(embeddings);
        double[] rand = embeddings.get(randomInt(embeddings.size())).vector();
        int maxResCount = 10;
        var res = collection.query(rand, maxResCount);
        assertFalse(res.isEmpty());
        db.close();

        db = DataBase.load(location);
        collection = db.getCollection(collName);
        assertEquals(collection.query(rand, maxResCount), res);

        return embeddings;
    }
}