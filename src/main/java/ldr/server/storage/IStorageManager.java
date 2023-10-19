package ldr.server.storage;

import java.util.Collection;

import ldr.client.domen.Embedding;
import ldr.client.domen.collection.CollectionException;

public interface IStorageManager extends IEmbeddingKeeper {
    void delete(long id) throws CollectionException;
    void delete(Collection<Long> ids) throws CollectionException;
}
