package ldr.server.storage;

import java.util.List;

import ldr.client.domen.Embedding;

public interface IEmbeddingGetter {
    Embedding get(long id);
    List<Embedding> get(List<Long> ids);
}
