package ldr.client.domen;

import java.util.List;

public record VectorCollectionResult(List<Embedding> results, List<Double> ranks) {
}
