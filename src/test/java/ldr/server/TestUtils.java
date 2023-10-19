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
        List<Embedding> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(new Embedding(i, generateVector(i % maxDimension), generateMeta(i % maxMetaSize)));
        }

        return result;
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
