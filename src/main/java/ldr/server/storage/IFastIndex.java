package ldr.server.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import ldr.client.domen.Embedding;

public interface IFastIndex {

    List<Long> getNearest(double[] vector) throws IOException;

    private Map<Integer, List<Long>> load(Path location) {
        return null;
    }

    private void close(Map<Integer, List<Long>> data, Path location) throws IOException {

    }

    void add(Embedding embedding) throws IOException;

    void add(List<Embedding> embeddings) throws IOException;

    void remove(Long element) throws IOException;

}
