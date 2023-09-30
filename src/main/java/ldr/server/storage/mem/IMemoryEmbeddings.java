package ldr.server.storage.mem;

import java.util.List;

import ldr.client.domen.Embedding;
import ldr.server.storage.IStorageEmbeddings;

public interface IMemoryEmbeddings extends IStorageEmbeddings {
    void put(Embedding embedding);
    void put(List<Embedding> embeddings);
}
