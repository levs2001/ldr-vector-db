package ldr.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ldr.client.domen.Embedding;

public class TestUtils {
    private static final int seed = 10;
    private static final Random rand = new Random(seed);

    public static List<Embedding> generateManyEmbeddings(int count, int maxDimension, int maxMetaSize) {
        return generateManyEmbeddings(count, maxDimension, maxMetaSize, false);
    }

    /**
     * @param serialId - флаг на то, чтобы id были последовательными и начинались с 0.
     */
    public static List<Embedding> generateManyEmbeddings(int count, int maxDimension, int maxMetaSize, boolean serialId) {
        List<Embedding> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(new Embedding(serialId ? i : rand.nextInt(), generateVector(i % maxDimension), generateMeta(i % maxMetaSize)));
        }

        return result;
    }

    /**
     * Default size list of embeddings.
     */
    public static List<Embedding> generateManyEmbeddings() {
        return generateManyEmbeddings(100, 20, 10);
    }

    public static int randomInt(int bound) {
        return rand.nextInt(bound);
    }

    private static double[] generateVector(int dimension) {
        double[] vector = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            vector[i] = rand.nextDouble();
        }

        return vector;
    }

    private static Map<String, String> generateMeta(int size) {
        Map<String, String> meta = new HashMap<>();

        for (int i = 0; i < size; i++) {
            meta.put(
                    ("key" + i).repeat(i),
                    ("val" + i).repeat(i)
            );
        }

        return meta;
    }
}
