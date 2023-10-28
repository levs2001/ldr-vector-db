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
    private final Map<Integer, List<Long>> bucketIdMap;

    public record Config(Path location, int vectorLen, int bucket) {
    }

    public FastIndex(Config config) {
        this.location = config.location();
        this.lsh = new LSHSuperBit(STAGES, config.bucket(), config.vectorLen(), INITIAL_SEED);
        this.bucketIdMap = load(location);
    }


    private int getBucket(double[] vector) {
        int[] hash = lsh.hash(vector);
        int lastBucket = hash.length - 1;
        return hash[lastBucket];
    }

    @Override
    public List<Long> getNearest(double[] vector) {
        int bucket = getBucket(vector);
        List<Long> listId = bucketIdMap.get(bucket);

        if (listId == null) {
            listId = new ArrayList<>();
        }
        return listId;
    }

    private Map<Integer, List<Long>> load(Path location) {
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

    @Override
    public void close() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(String.valueOf(location)), bucketIdMap);
    }

    @Override
    public void add(Embedding embedding) {
        double[] vector = embedding.vector();
        long id = embedding.id();
        int bucket = getBucket(vector);
        List<Long> ids = bucketIdMap.get(bucket);

        if (ids == null) {
            ids = new ArrayList<>();
        }

        ids.add(id);
        bucketIdMap.putIfAbsent(bucket, ids);
    }

    @Override
    public void add(List<Embedding> embeddings) {
        for (Embedding e : embeddings) {
            add(e);
        }
    }

    @Override
    public void remove(Long element) {
        for (List<Long> list : bucketIdMap.values()) {
            list.remove(element);
        }
    }

}
