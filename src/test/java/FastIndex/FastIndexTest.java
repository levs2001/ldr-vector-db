package FastIndex;
import ldr.client.domen.Embedding;
import ldr.server.storage.FastIndex;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FastIndexTest {
    @Test
    public void testFastIndex() {

        List<Embedding> examples = List.of(
                new Embedding(1, new double[]{11.0, 12.5, 13.0},
                        Map.of("color", "red", "size", "small")),
                new Embedding(2, new double[]{11.1, 12.6, 13.2},
                        Map.of("color", "blue", "size", "big")),
                new Embedding(3, new double[]{11.3, 12.1, 13.1},
                        Map.of("color", "red", "size", "big")),
                new Embedding(4, new double[]{-1000, -1111, -1111},
                        Map.of("color", "blue", "size", "small")),
                new Embedding(5, new double[]{-1001, -1112, -1114},
                        Map.of("color", "red", "size", "big")),
                new Embedding(6, new double[]{-1001, -1113, -1113},
                        Map.of("color", "red", "size", "big")),
                new Embedding(7, new double[]{100000, 10000001, 10000002},
                        Map.of("color", "blue", "size", "big")),
                new Embedding(8, new double[]{100001, 10000002, 10000003},
                        Map.of("color", "red", "size", "big")),
                new Embedding(9, new double[]{100004, 10000005, 10000006},
                        Map.of("color", "blue", "size", "big")),
                new Embedding(10, new double[]{100001, 10000007, 10000008},
                        Map.of("color", "red", "size", "small"))
        );

        int buckets = 3;
        int vectorLen = examples.get(0).vector().length;

        FastIndex lsh = new FastIndex(buckets, vectorLen, examples);
        Map<Integer, List<Embedding>> embeddingMap = lsh.getBuckets();

        embeddingMap.forEach((key, value) -> {
            System.out.println("Бакет: " + key + ", Значение: " + value);
        });


        double[] vector = {-1001, -1001, -1001};
        List<Embedding> listEmbeddings = lsh.getNearest(vector, embeddingMap);

        listEmbeddings.forEach(embedd -> {
            System.out.println(embedd);
        });

        List<Embedding> examplesNearest = List.of(
                new Embedding(4, new double[]{-1000, -1111, -1111},
                        Map.of("color", "blue", "size", "small")),
                new Embedding(5, new double[]{-1001, -1112, -1114},
                        Map.of("color", "red", "size", "big")),
                new Embedding(6, new double[]{-1001, -1113, -1113},
                        Map.of("color", "red", "size", "big")));

        assertEquals(listEmbeddings, examplesNearest);

    }
}