package FastIndex;

import com.fasterxml.jackson.databind.ObjectMapper;
import ldr.client.domen.Embedding;
import ldr.server.storage.Config;
import ldr.server.storage.FastIndex;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FastIndexTest {
    @Test
    public void testFastIndex() throws IOException {
        int embeddingsCount = 100;
        int maxDim = 10;
        int maxMetaSize = 5;

        // генерим эмбеддинги
        List<Embedding> embeddings = generateManyEmbeddings(embeddingsCount, maxDim, maxMetaSize);
        String pathAsString = "collection_test/test.json";
        Path path = Paths.get(pathAsString);

        Config config = new Config(path, maxDim);
        FastIndex fi = new FastIndex(config);

        Embedding embedding = new Embedding(52, new double[]{14.7, 132.5, 133.8, 32.3, 14.7, 132.5, 133.8, 32.3, 14.7, 132.5},
                Map.of("color", "green", "size", "large"));

        fi.add(embedding);
        List<Long> nearestIds = fi.getNearest(embedding.vector());
        System.out.println("\n" + "Input vector " + Arrays.toString(embedding.vector()) + "\n" + "Nearest Ids " + nearestIds);
        fi.add(embeddings);
        fi.remove(90L);
    }


    // Генератор с теста protobuff, но мета и длинна вектора всега одна и та же.
    private static final Random rand = new Random(21);

    private List<Embedding> generateManyEmbeddings(int count, int maxDimension, int maxMetaSize) {
        List<Embedding> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(new Embedding(i, generateVector(maxDimension), generateMeta(maxMetaSize)));
        }
        return result;
    }

    private double[] generateVector(int dimension) {
        double[] vector = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            vector[i] = rand.nextDouble();
        }
        return vector;
    }

    private Map<String, String> generateMeta(int size) {
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