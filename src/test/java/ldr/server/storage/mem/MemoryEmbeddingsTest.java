package ldr.server.storage.mem;

import java.util.List;

import org.junit.jupiter.api.Test;

import ldr.client.domen.Embedding;
import ldr.server.serialization.protobuf.EmbeddingOuterClass;
import ldr.server.storage.IEmbeddingKeeper;

import static ldr.server.TestUtils.generateManyEmbeddings;
import static org.junit.jupiter.api.Assertions.*;

class MemoryEmbeddingsTest {
    @Test
    public void testAddAngGet() {
        // No flush in this test, so null for callback.
        IEmbeddingKeeper memoryEmbeddings = new MemoryEmbeddings(100_000, null);
        List<Embedding> embeddings = generateManyEmbeddings(100, 20, 10);
        embeddings.forEach(memoryEmbeddings::add);
        for (Embedding embedding : embeddings) {
            Embedding fromMem = memoryEmbeddings.get(embedding.id());
            assertEquals(embedding, fromMem);
        }
    }

    @Test
    public void testAddManyAngGetMany() {
        // TODO
    }

    @Test
    public void testFlushCallback() {
        // TODO
    }
}