package ldr.server.storage;

import java.io.Closeable;
import java.util.Collection;

public interface IStorageManager extends IEmbeddingKeeper, Closeable {
    void delete(long id);
    void delete(Collection<Long> ids);
}
