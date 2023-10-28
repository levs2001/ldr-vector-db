package ldr.server.storage;

import java.io.IOException;
import java.util.List;

import ldr.client.domen.Embedding;

public interface IFastIndex {
    List<Long> getNearest(double[] vector) throws IOException;

    void add(Embedding embedding);

    void add(List<Embedding> embeddings);

    void remove(Long id);

    void remove(List<Long> ids);

    void close() throws IOException;
}
