package ldr.server.storage;

import java.util.Iterator;
import java.util.List;

import ldr.client.domen.Embedding;

/**
 * Умеет хранить Embedding-и.
 */
public interface IStorageEmbeddings {
    Embedding get(long id);
    List<Embedding> get(List<Long> ids);

    /**
     * @return Sorted by id embeddings.
     */
    Iterator<Embedding> getAll();
}
