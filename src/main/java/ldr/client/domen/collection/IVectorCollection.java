package ldr.client.domen.collection;

import java.util.Collection;
import java.util.List;

import ldr.client.domen.Embedding;
import ldr.client.domen.VectorCollectionResult;
import ldr.server.storage.IEmbeddingKeeper;

public interface IVectorCollection {
    void add(Embedding embedding) throws CollectionException;

    void add(List<Embedding> embeddings) throws CollectionException;

    void update(long id, Embedding newEmbedding) throws CollectionException;

    void update(List<Long> ids, List<Long> newEmbeddings) throws CollectionException;

    VectorCollectionResult query(List<Double> vector, long maxNeighborsCount) throws CollectionException;

    /**
     * Own rules for filter, for ex meta1Key:eq:meta1Val
     */
    VectorCollectionResult query(List<Double> vector, long maxNeighborsCount, String filter) throws CollectionException;

    void delete(long id) throws CollectionException;

    void delete(Collection<Long> ids) throws CollectionException;

    void delete(String filter) throws CollectionException;

    void close() throws CollectionException;
}
