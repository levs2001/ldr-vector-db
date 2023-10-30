package ldr.server.storage.index;

import java.io.Closeable;
import java.util.List;
import java.util.Set;

import ldr.client.domen.Embedding;

public interface IFastIndex extends Closeable {
    Set<Long> getNearest(double[] vector);

    void add(Embedding embedding);

    void add(List<Embedding> embeddings);

    void remove(Long id);

    void remove(List<Long> ids);
}
