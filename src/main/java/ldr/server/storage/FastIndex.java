package ldr.server.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import info.debatty.java.lsh.LSHSuperBit;
import ldr.client.domen.Embedding;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastIndex implements IFastIndex {
    private final static int STAGES = 5;
    private final static int INITIAL_SEED = 42;
    private final LSHSuperBit lsh;
    private final Path location;
    private final Map<Integer, List<Long>> buckets;

    public static IFastIndex load(Config config) {
        return new FastIndex(config, loadFromFile(config.location));
    }

    // Use load.
    private FastIndex(Config config, Map<Integer, List<Long>> buckets) {
        this.location = config.location();
        this.buckets = buckets;
        this.lsh = new LSHSuperBit(STAGES, config.bucket(), config.vectorLen(), INITIAL_SEED);
    }

    @Override
    public List<Long> getNearest(double[] vector) {
        int bucket = getBucket(vector);
        List<Long> listId = buckets.get(bucket);

        if (listId == null) {
            listId = new ArrayList<>();
        }
        return listId;
    }

    @Override
    public void close() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(String.valueOf(location)), buckets);
    }

    @Override
    public void add(Embedding embedding) {
        double[] vector = embedding.vector();
        long id = embedding.id();
        int bucket = getBucket(vector);
        List<Long> ids = buckets.get(bucket);

        if (ids == null) {
            ids = new ArrayList<>();
        }

        ids.add(id);
        buckets.putIfAbsent(bucket, ids);
    }

    @Override
    public void add(List<Embedding> embeddings) {
        for (Embedding e : embeddings) {
            add(e);
        }
    }

    @Override
    public void remove(Long id) {
        for (List<Long> list : buckets.values()) {
            list.remove(id);
        }
    }

    @Override
    public void remove(List<Long> ids) {
        ids.forEach(this::remove);
    }

    private int getBucket(double[] vector) {
        int[] hash = lsh.hash(vector);
        int lastBucket = hash.length - 1;
        return hash[lastBucket];
    }

    private static Map<Integer, List<Long>> loadFromFile(Path location) {
        ObjectMapper mapper = new ObjectMapper();

        if (Files.exists(location)) {
            try {
                File jsonFile = location.toFile();
                Map<Integer, List<Long>> data = mapper.readValue(jsonFile, new TypeReference<>() {
                });
                return data;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }

    public record Config(Path location, int vectorLen, int bucket) {
    }
}
