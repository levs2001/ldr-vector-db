package ldr.server;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import ldr.client.domen.Embedding;

public class TestUtils {
    public static final Path resourcesPath = Paths.get("src", "test", "resources");

    public static List<Embedding> generateManyEmbeddings(int count, int maxDimension, int maxMetaSize) {
        return generateManyEmbeddings(count, maxDimension, maxMetaSize, false);
    }

    /**
     * @param serialId - флаг на то, чтобы id были последовательными и начинались с 0.
     */
    public static List<Embedding> generateManyEmbeddings(int count, int maxDimension, int maxMetaSize, boolean serialId) {
        List<Embedding> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(
                    new Embedding(
                            serialId ? i : ThreadLocalRandom.current().nextInt(),
                            generateVector(i % maxDimension),
                            generateMeta(i % maxMetaSize)
                    )
            );
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
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static List<Embedding> getRandomSubList(int subListSize, List<Embedding> list) {
        if (subListSize > list.size()) {
            throw new IndexOutOfBoundsException(
                    String.format("Sublist is bigger then list. sublist size: %d, list size: %d",
                            subListSize, list.size())
            );
        }

        List<Embedding> subList = new ArrayList<>(subListSize);
        for (int i = 0; i < subListSize; i++) {
            subList.add(list.get(randomInt(list.size())));
        }

        subList.sort(Comparator.comparingLong(Embedding::id));

        return subList;
    }

    private static double[] generateVector(int dimension) {
        double[] vector = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            vector[i] = ThreadLocalRandom.current().nextDouble();
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
