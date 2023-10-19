package ldr.server.storage;

import java.util.Collection;
import java.util.List;

import ldr.client.domen.Embedding;
import ldr.client.domen.collection.CollectionException;

/**
 * Умеет хранить Embedding-и.
 * Не умеет удалять, за это отвечает IStorageManager, поскольку процесс удаление нетривиален,
 * используются могилы, как в LSM.
 */
public interface IEmbeddingKeeper {
    Embedding get(long id);
    List<Embedding> get(List<Long> ids);


    void add(Embedding embedding);
    void add(List<Embedding> embeddings);
}
