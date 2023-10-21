package ldr.server.storage.mem;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ldr.client.domen.Embedding;
import ldr.server.storage.IEmbeddingKeeper;

import static ldr.server.TestUtils.generateManyEmbeddings;
import static org.junit.jupiter.api.Assertions.*;

class MemoryEmbeddingsTest {
    private static List<Embedding> commonEmbeddings;

    @BeforeAll
    public static void setup() {
        commonEmbeddings = generateManyEmbeddings(100, 20, 10);
    }

    @Test
    public void testAddAngGetOneByOne() {
        // No flush in this test, so null for callback.
        IEmbeddingKeeper memoryEmbeddings = new MemoryEmbeddings(100_000, null);
        commonEmbeddings.forEach(memoryEmbeddings::add);
        for (Embedding embedding : commonEmbeddings) {
            Embedding fromMem = memoryEmbeddings.get(embedding.id());
            assertEquals(embedding, fromMem);
        }
    }

    @Test
    public void testAddManyAngGetMany() {
        IEmbeddingKeeper memoryEmbeddings = new MemoryEmbeddings(100_000, null);
        memoryEmbeddings.add(commonEmbeddings);
        List<Long> ids = commonEmbeddings.stream().map(Embedding::id).toList();
        List<Embedding> actual = memoryEmbeddings.get(ids);
        assertEquals(commonEmbeddings.size(), actual.size());
        assertEquals(commonEmbeddings, actual);
    }

    @Test
    public void testFlushCallback() {
        List<Embedding> toFlush = new ArrayList<>();
        // 100 - flush threshold bites. Small count, so we expect flush callback on some step.
        IMemoryEmbeddings memoryEmbeddings = new MemoryEmbeddings(100, toFlush::addAll);
        memoryEmbeddings.add(commonEmbeddings);

        assertTrue(memoryEmbeddings.isNeedFlush());
        assertFalse(toFlush.isEmpty());
    }
}