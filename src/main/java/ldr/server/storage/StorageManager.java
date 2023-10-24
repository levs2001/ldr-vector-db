package ldr.server.storage;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ldr.client.domen.Embedding;
import ldr.client.domen.collection.CollectionException;
import ldr.server.storage.mem.IMemoryEmbeddings;
import ldr.server.storage.mem.MemoryEmbeddings;

public class StorageManager implements IEmbeddingKeeper {
    private final Config config;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private IMemoryEmbeddings inMem;

    // TODO.
//    private final IHardDriveEmbeddings inDrive;

    public StorageManager(Config config) {
        this.config = config;
        this.inMem = new MemoryEmbeddings(config.flushThresholdBytes, this::flush);
    }

    public synchronized void flush(List<Embedding> embeddings) {
        rwLock.writeLock().lock();
        try {
            if (inMem.isNeedFlush()) {
                // У нас есть небольшая гонка перед тем, как вызвать этот метод внутри put в StorageEmbeddings,
                // Сейчас мы в последовательном коде и можем еще раз все проверить.
                // TODO: oldInMem надо скинуть на диск
                IMemoryEmbeddings oldInMem = inMem;
                inMem = new MemoryEmbeddings(config.flushThresholdBytes, this::flush);
            }
        } finally {
            rwLock.writeLock().unlock();
        }

        // Кладем Embedding, из-за которого мы до этого выходили за границы размера. После флаша он должен влезть.
        inMem.add(embeddings);
    }


    @Override
    public Embedding get(long id) {
        return inMem.get(id);
    }

    @Override
    public List<Embedding> get(List<Long> ids) {
        // TODO
        return null;
    }

    @Override
    public void add(Embedding embedding) {
        rwLock.readLock().lock();
        try {
            inMem.add(embedding);
        } finally {
            rwLock.readLock().unlock();
        }

    }

    @Override
    public void add(List<Embedding> embeddings) {
        // TODO
    }

    public record Config(int flushThresholdBytes) {
    }

    public static Embedding createGrave(long id) {
        return new Embedding(id, null, null);
    }

    public static boolean isGrave(Embedding embedding) {
        return embedding.vector() == null && embedding.metas() == null;
    }
}
