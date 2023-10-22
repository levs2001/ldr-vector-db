package FastIndex;

import com.fasterxml.jackson.databind.ObjectMapper;
import ldr.client.domen.Embedding;
import ldr.server.storage.FastIndex;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
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

        // класс, +- понимаю, что это лажа
        FastIndex fi = new FastIndex();

        // Вычисляем бакеты для сгенеренных эмбеддингов
        Map<Integer, List<Long>> embeddingMap = fi.calcaulateLoadBuckets(embeddings);

        embeddingMap.forEach((key, value) -> {
            System.out.println("Bucket: " + key + ", Ids: " + value);
        });

        String filePath = "data.json";

        // Запись Map в JSON-файл
        writeMapToJsonFile(embeddingMap, filePath);
        System.out.println("Map has been written to " + filePath);

        // Вычисляем бакет для sample и выводим его соседей
        double[] sample = {1.2, 1.7, 2.56, 1000.1, 23.2, 1.2, 1.7, 2.56, 1000.1, 23.2};
        List<Long> nearestIds = fi.getNearest(sample);
        System.out.println("\n" + "Input vector " + Arrays.toString(sample) + "\n" + "Nearest Ids " + nearestIds);

        // Для теста, хз как создат List<Long> сразу, он все подчеркивает, костыль через цикл.
        long[] numbers = {4, 5, 9, 10, 12, 13, 18, 19, 24, 27, 30, 32, 34, 41, 42, 44, 45, 48,
                51, 52, 53, 55, 63, 68, 79, 80, 81, 82, 86, 89, 90, 93, 94, 95, 97, 98};

        List<Long> actualIds = new ArrayList<>();
        for (long number : numbers) {
            actualIds.add(number);
        }

        assertEquals(nearestIds, actualIds);

    }
    // Запись в json
    public void writeMapToJsonFile(Map<Integer, List<Long>> data, String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(filePath), data);
    }

    // Генератор с теста protobuff, но мета и длинна вектора всега одна и та же.
    private static final Random rand = new Random(42);

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