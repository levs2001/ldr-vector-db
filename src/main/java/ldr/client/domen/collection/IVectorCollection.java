package ldr.client.domen.collection;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;

import ldr.client.domen.Embedding;
import ldr.client.domen.VectorCollectionResult;

public interface IVectorCollection extends Closeable {
    void add(Embedding embedding);

    void add(List<Embedding> embeddings);

    void update(long id, Embedding newEmbedding);

    void update(List<Long> ids, List<Long> newEmbeddings);

    VectorCollectionResult query(List<Double> vector, long maxNeighborsCount);

    /**
     * Own rules for filter, for ex meta1Key:eq:meta1Val
     */
    VectorCollectionResult query(List<Double> vector, long maxNeighborsCount, String filter);

    void delete(long id);

    void delete(Collection<Long> ids);

    void delete(String filter);
}
