package ldr.client.domen;

import java.util.List;
import java.util.Map;

public record Embedding(long id, List<Double> value, Map<String, String> metas) {
}
