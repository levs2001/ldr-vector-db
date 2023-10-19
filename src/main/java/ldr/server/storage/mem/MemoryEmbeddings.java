package ldr.server.storage.mem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import ldr.client.domen.Embedding;

/**
 * Embeddings in memory (in heap).
 * Class is thread safe.
 */
public class MemoryEmbeddings implements IMemoryEmbeddings {
    private final NavigableMap<Long, Embedding> embeddingsMap = new ConcurrentSkipListMap<>();
    private final AtomicInteger embeddingsByteSize = new AtomicInteger();

    private final int flushThresholdBytes;
    private final Consumer<List<Embedding>> flushCallback;

    /**
     * @param flushThresholdBytes - byte size after which we should call flushCallback.
     * @param flushCallback       - callback function which we call after overflowing the threshold.
     *                            We pass the embeddings that should make the overflow in that function.
     */
    public MemoryEmbeddings(int flushThresholdBytes, Consumer<List<Embedding>> flushCallback) {
        this.flushThresholdBytes = flushThresholdBytes;
        this.flushCallback = flushCallback;
    }

    private static int getEmbeddingsSize(Embedding embedding) {
        // TODO: Учитывать  мету, либо сюда передавать embeddings без меты (разделить классы)
        // TODO: Учитывать размер хедера
        return Long.BYTES + embedding.vector().length * Double.SIZE;
    }

    @Override
    public void add(Embedding embedding) {
        int size = getEmbeddingsSize(embedding);
        if (embeddingsByteSize.addAndGet(size) > flushThresholdBytes) {
            flushCallback.accept(List.of(embedding));
        } else {
            embeddingsMap.put(embedding.id(), embedding);
        }
    }

    @Override
    public void add(List<Embedding> embeddings) {
        // TODO: Add common add with lambda, not dupblicate code.
        for (int i = 0; i < embeddings.size(); i++) {
            Embedding embedding = embeddings.get(i);
            int size = getEmbeddingsSize(embedding);
            if (embeddingsByteSize.addAndGet(size) > flushThresholdBytes) {
                flushCallback.accept(embeddings.subList(i, embeddings.size()));
            } else {
                embeddingsMap.put(embedding.id(), embedding);
            }
        }
    }

    @Override
    public Embedding get(long id) {
        return embeddingsMap.get(id);
    }

    @Override
    public List<Embedding> get(List<Long> ids) {
        List<Embedding> result = new ArrayList<>(ids.size());
        for (long id : ids) {
            Embedding searched = embeddingsMap.get(id);
            if (searched != null) {
                result.add(searched);
            }
        }

        return result;
    }

    @Override
    public Iterator<Embedding> getAll() {
        return embeddingsMap.values().iterator();
    }

    @Override
    public boolean isNeedFlush() {
        return embeddingsByteSize.get() > flushThresholdBytes;
    }
}
