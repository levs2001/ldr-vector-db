package ldr.client.domen.collection;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ldr.client.domen.Embedding;
import ldr.client.domen.VectorCollectionResult;
import ldr.server.storage.IStorageManager;
import ldr.server.storage.StorageManager;
import ldr.server.storage.index.FastIndex;
import ldr.server.storage.index.IFastIndex;

public class VectorCollection implements IVectorCollection {
    private static final Logger log = LoggerFactory.getLogger(VectorCollection.class);
    private static final String STORAGE_FOLDER = "storage";
    private static final String INDEX_FILENAME = "index.json";
    private static final int FLUSH_THRESHOLD_BYTES = 100_000;
    private static final int META_ENTRY_SIZE = 15;

    private final int vectorLen;
    private final Closeable[] toClose;
    private final IStorageManager storage;
    private final IFastIndex index;

    public static VectorCollection load(Config config) throws IOException {
        Path location = config.location();
        if (!Files.exists(location)) {
            log.info("Can't find snapshot. New collection will be created.");
            Files.createDirectory(location);
        } else {
            log.info("Snapshot found, will be loaded.");
        }

        var storageConfig =
                new StorageManager.Config(location.resolve(STORAGE_FOLDER), FLUSH_THRESHOLD_BYTES, META_ENTRY_SIZE);

        IStorageManager storage = StorageManager.load(storageConfig);
        IFastIndex index = FastIndex.load(new FastIndex.Config(location.resolve(INDEX_FILENAME), config.vectorLen()));
        Closeable[] toClose = new Closeable[]{storage, index};

        return new VectorCollection(
                config.vectorLen(),
                storage,
                index,
                toClose
        );
    }

    private VectorCollection(int vectorLen, IStorageManager storage, IFastIndex index, Closeable[] toClose) {
        this.vectorLen = vectorLen;
        this.storage = storage;
        this.index = index;
        this.toClose = toClose;
    }

    @Override
    public void add(Embedding embedding) {

    }

    @Override
    public void add(List<Embedding> embeddings) {

    }

    @Override
    public void update(long id, Embedding newEmbedding) {

    }

    @Override
    public void update(List<Long> ids, List<Long> newEmbeddings) {

    }

    @Override
    public VectorCollectionResult query(List<Double> vector, long maxNeighborsCount) {
//        List<Long> nearest = fastIndex.getNearest(vector);
//
//        List<Embedding> memIds = inMem.get(nearest);
//        if (memIds.size() == nearest.size()) {
//            // Все нашли.
////            return //
//        }
////        nearest.remove(memIds);
//        List<Embedding> driveIds = inDrive.get();
//
        // Мержим 2 списка с преоритетом у inMem

        return null;
    }

    @Override
    public VectorCollectionResult query(List<Double> vector, long maxNeighborsCount, String filter) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void delete(Collection<Long> ids) {

    }

    @Override
    public void delete(String filter) {

    }

    @Override
    public void close() throws IOException {
        for (Closeable cl : toClose) {
            cl.close();
        }
    }

    /**
     * @param location of collection. Will be created if not presented.
     */
    public record Config(Path location, int vectorLen){
    }
}
