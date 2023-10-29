package ldr.server.storage.index;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import ldr.client.domen.Embedding;

public interface IFastIndex {
    Set<Long> getNearest(double[] vector);

    void add(Embedding embedding);

    void add(List<Embedding> embeddings);

    void remove(Long id);

    void remove(List<Long> ids);

    void close() throws IOException;
}
