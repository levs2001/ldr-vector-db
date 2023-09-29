package ldr.client.domen.collection;

import java.util.Collection;
import java.util.List;

import ldr.client.domen.Embedding;
import ldr.client.domen.VectorCollectionResult;

public class VectorCollection implements IVectorCollection {

    @Override
    public void add(Embedding embedding) throws CollectionException {
        // Сразу сохраняет на диск и перестраивает индексы (и другие штуки в ram), поскольку скорость не важна
    }

    @Override
    public void add(Collection<Embedding> embeddings) throws CollectionException {
        // Сразу сохраняет на диск и перестраивает индексы (и другие штуки в ram)
    }

    @Override
    public void update(long id, Embedding newEmbedding) throws CollectionException {

    }

    @Override
    public void update(List<Long> ids, List<Long> newEmbeddings) throws CollectionException {

    }

    @Override
    public VectorCollectionResult query(List<Double> vector, long maxNeighborsCount) {
        // Скорость важна, поэтому в ram должно быть что-то, помогающее искать
        return null;
    }

    @Override
    public VectorCollectionResult query(List<Double> vector, long maxNeighborsCount, String filter) {
        return null;
    }

    @Override
    public void delete(long id) throws CollectionException {

    }

    @Override
    public void delete(Collection<Long> ids) throws CollectionException {

    }

    @Override
    public void delete(String filter) throws CollectionException {

    }
}
