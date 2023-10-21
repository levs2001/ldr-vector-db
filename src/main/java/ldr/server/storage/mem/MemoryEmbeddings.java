package ldr.server.storage.mem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    @Override
    public void add(Embedding embedding) {
        add(embedding, () -> List.of(embedding));
    }

    @Override
    public void add(List<Embedding> embeddings) {
        for (int i = 0; i < embeddings.size(); i++) {
            // Final for lambda.
            final int current = i;
            add(embeddings.get(i), () -> embeddings.subList(current, embeddings.size()));
        }
    }

    private void add(Embedding embedding, Supplier<List<Embedding>> subListLambda) {
        int size = getEmbeddingsSize(embedding);
        if (embeddingsByteSize.addAndGet(size) > flushThresholdBytes) {
            flushCallback.accept(subListLambda.get());
        } else {
            embeddingsMap.put(embedding.id(), embedding);
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

    private static int getEmbeddingsSize(Embedding embedding) {
        // TODO: Учитывать  мету, либо сюда передавать embeddings без меты (разделить классы)
        // TODO: Учитывать размер хедера
        return Long.BYTES + embedding.vector().length * Double.SIZE;
    }
}
