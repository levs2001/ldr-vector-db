package ldr.server.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import ldr.client.domen.Embedding;
import ldr.server.storage.drive.HardDriveEmbeddings;
import ldr.server.storage.drive.IHardDriveEmbeddings;
import ldr.server.storage.mem.IMemoryEmbeddings;
import ldr.server.storage.mem.MemoryEmbeddings;


public class StorageManager implements IStorageManager {
    private static final Logger log = LoggerFactory.getLogger(StorageManager.class);
    private final IHardDriveEmbeddings inDrive;
    private final MemoryEmbeddings.Config memConfig;
    private IMemoryEmbeddings inMem;

    public static IStorageManager load(Config config) throws IOException {
        var hardDriveConfig = new HardDriveEmbeddings.Config(config.location);
        IHardDriveEmbeddings inDrive = HardDriveEmbeddings.load(hardDriveConfig);
        return new StorageManager(config.flushThresholdBytes, config.metaEntrySize, inDrive);
    }

    // Use load.
    private StorageManager(int flushThresholdBytes, int metaEntrySize, IHardDriveEmbeddings inDrive) {
        this.inDrive = inDrive;
        this.memConfig = new MemoryEmbeddings.Config(flushThresholdBytes, metaEntrySize);
        this.inMem = new MemoryEmbeddings(memConfig, this::flush);
    }

    @Override
    public Embedding get(long id) {
        Embedding result = inMem.get(id);
        if (result == null) {
            return inDrive.get(id);
        } else  if (isGrave(result)) {
            return null;
        }

        return result;
    }

    @Override
    public List<Embedding> get(List<Long> ids) {
        List<Embedding> result = new ArrayList<>(ids.size());
        for (long id : ids) {
            Embedding embedding = get(id);
            if (embedding != null) {
                result.add(embedding);
            }
        }

        return result;
    }

    @Override
    public void add(Embedding embedding) {
        inMem.add(embedding);
    }

    @Override
    public void add(List<Embedding> embeddings) {
        embeddings.forEach(this::add);
    }

    @Override
    public void delete(long id) {
        add(createGrave(id));
    }

    @Override
    public void delete(Collection<Long> ids) {
        ids.forEach(this::delete);
    }

    private synchronized void flush(List<Embedding> embeddings) {
        // У нас есть небольшая гонка перед тем, как вызвать этот метод внутри add в StorageEmbeddings,
        // Сейчас мы в последовательном коде и можем еще раз все проверить.
        if (inMem.isNeedFlush()) {
            boolean flushed = true;
            try {
                inDrive.save(inMem.getAll());
            } catch (IOException e) {
                log.error("Exception during flush. Flush is aborted, if flush won't be successful later, " +
                        "then you face flush loop, and add operation won't be work correctly. " +
                        "So get operations could be inconsistent.", e);
                flushed = false;
            }

            if (flushed) {
                inMem = new MemoryEmbeddings(memConfig, (embs) -> handleFlushException(() -> flush(embs)));
            }
        }

        // Кладем Embedding-и, из-за которых мы до этого выходили за границы размера. После флаша он должен влезть.
        inMem.add(embeddings);
    }

    private void handleFlushException(WriteOperation writeOperation) {
        try {
            writeOperation.write();
        } catch (IOException e) {
            log.error("Error during flush.", e);
        }
    }

    @VisibleForTesting
    static Embedding createGrave(long id) {
        return new Embedding(id, null, null);
    }

    public static boolean isGrave(Embedding embedding) {
        return embedding.vector() == null && embedding.metas() == null;
    }

    @FunctionalInterface
    interface WriteOperation {
        void write() throws IOException;
    }

    public record Config(Path location, int flushThresholdBytes, int metaEntrySize) {
    }
}