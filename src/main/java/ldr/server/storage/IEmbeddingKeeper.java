package ldr.server.storage;

import ldr.client.domen.Embedding;

/**
 * Умеет хранить Embedding-и.
 */
public interface IEmbeddingKeeper {
    Embedding get(long id);

    void add(Embedding embedding);
}
