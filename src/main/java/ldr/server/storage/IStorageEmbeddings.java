package ldr.server.storage;

import java.util.Iterator;
import java.util.List;

import ldr.client.domen.Embedding;

public interface IStorageEmbeddings extends IEmbeddingKeeper {
    /**
     * @return Sorted by id embeddings.
     */
    Iterator<Embedding> getAll();
}
