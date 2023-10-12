package ldr.server.serialization.comparsion;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ldr.client.domen.Embedding;
import ldr.server.serialization.my.DataEncoder;
import ldr.server.serialization.my.EmbeddingEncoder;
import ldr.server.serialization.protobuf.ProtobufEmbeddingEncoder;

public class TestProtobufComparsion {
    private static final int seed = 10;
    private static final Random rand = new Random(seed);
    private static final int embeddingsCount = 100_000;
    private static final int maxDim = 100;
    private static final int maxMetaSize = 10;

    @Test
    public void testMyProtobuf() throws IOException {
        doTest("my protobuf", new EmbeddingEncoder());
    }

    @Test
    public void testDefaultProtobuf() throws IOException {
        doTest("default protobuf", new ProtobufEmbeddingEncoder());
    }

    private void doTest(String implName, DataEncoder<Embedding> coder) throws IOException {
        doTest(implName, coder, embeddingsCount, maxDim, maxMetaSize);
    }

    private void doTest(String implName, DataEncoder<Embedding> coder, int embeddingsCount, int maxDim, int maxMetaSize)
            throws IOException {
        List<Embedding> embeddings = generateManyEmbeddings(embeddingsCount, maxDim, maxMetaSize);
        Path path = Paths.get("embeddings.b");
        long elapsedNanos;
        try (OutputStream stream = new BufferedOutputStream(Files.newOutputStream(path, StandardOpenOption.CREATE))) {
            long startTime = System.nanoTime();
            for (Embedding embedding : embeddings) {
                stream.write(coder.encode(embedding));
            }
            elapsedNanos = System.nanoTime() - startTime;
        }

        long bytesSize = Files.size(path);
        printResults(implName, embeddingsCount, maxDim, maxMetaSize, bytesSize, elapsedNanos);
        Files.delete(path);
    }

    private void printResults(String implName, int embeddingsCount, int maxDim, int maxMetaSize,
                              long bytesSize, long elapsedNanos) {
        System.out.printf("Realization: %s%n", implName);
        System.out.println("Input data was: ");
        System.out.printf("Seed: %d%n", seed);
        System.out.printf("Embeddings count: %d%n", embeddingsCount);
        System.out.printf("Max dimension: %d%n", maxDim);
        System.out.printf("Max metas map size: %d%n", maxMetaSize);

        System.out.println();
        System.out.println("Bench size results:");
        System.out.printf("Size of file in bytes: %d%n", bytesSize);
        System.out.printf("Size of file in kilobytes: %d%n", bytesSize / 1024);
        System.out.printf("Size of file in megabytes: %d%n", bytesSize / 1048576);

        System.out.println();
        System.out.println("Bench time results:");
        System.out.printf("Time in nanos: %d%n", elapsedNanos);
        System.out.printf("Time in millis: %d%n", elapsedNanos / 1_000_000);
        System.out.printf("Time in seconds: %d%n", elapsedNanos / 1_000_000_000);
    }

    private List<Embedding> generateManyEmbeddings(int count, int maxDimension, int maxMetaSize) {
        List<Embedding> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(new Embedding(i, generateVector(i % maxDimension), generateMeta(i % maxMetaSize)));
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
