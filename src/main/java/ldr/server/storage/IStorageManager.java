package ldr.server.storage;

import java.util.Collection;

public interface IStorageManager extends IEmbeddingKeeper {
    void delete(long id);
    void delete(Collection<Long> ids);
}
