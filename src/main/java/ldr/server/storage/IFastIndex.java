package ldr.server.storage;

import java.util.List;
import java.util.Map;

import ldr.client.domen.Embedding;

public interface IFastIndex {
    List<Embedding> getNearest(double[] vector, Map<Integer, List<Embedding>> embeddingMap);
    Map<Integer, List<Embedding>> getBuckets();
}
