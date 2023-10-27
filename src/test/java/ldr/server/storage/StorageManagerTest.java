package ldr.server.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import ldr.client.domen.Embedding;

import static ldr.server.TestUtils.generateManyEmbeddings;
import static ldr.server.TestUtils.resourcesPath;
import static org.junit.jupiter.api.Assertions.*;

class StorageManagerTest {
    private static final Path storagePath = resourcesPath.resolve("storage");
    private static final int metaEntrySize = 15;

    @Test
    public void testAddAndGet() throws IOException {
        testAddAndGet(100_000, "testAddAndGet");
    }

    @Test
    public void testWithFlush() throws IOException {
        testAddAndGet(5000, "testWithFlush");
    }

    private void testAddAndGet(int flushThresholdBytes, String testId) throws IOException {
        Path location = Files.createTempDirectory(storagePath, testId);
        IStorageManager storage = StorageManager.load(new StorageManager.Config(location, flushThresholdBytes, metaEntrySize));
        var embeddings = generateManyEmbeddings();
        storage.add(embeddings);
        var result = storage.get(embeddings.stream().map(Embedding::id).toList());
        assertEquals(embeddings.size(), result.size());
        for (int i = 0; i < embeddings.size(); i++) {
            assertEquals(embeddings.get(i), result.get(i));
        }
        embeddings.forEach(e -> assertEquals(e, storage.get(e.id())));

        FileUtils.deleteDirectory(location.toFile());
    }


}