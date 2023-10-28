package ldr.server.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.TypeFactory;
import info.debatty.java.lsh.LSHSuperBit;
import ldr.client.domen.Embedding;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FastIndex implements IFastIndex {
    final static int BUCKETS = 5;
    final static int STAGES = 5;
    final static int INITIAL_SEED = 42;
    public int VECTOR_LEN;
    public LSHSuperBit lsh;
    public Path location;
    Map<Integer, List<Long>> bucketIdMap;

    public FastIndex(Config config) {
        this.location = config.location();
        this.VECTOR_LEN = config.VECTOR_LEN();
        this.lsh = new LSHSuperBit(STAGES, BUCKETS, VECTOR_LEN, INITIAL_SEED);
        this.bucketIdMap = load(location);
    }


    private int getBucket(double[] vector) {
        int[] hash = lsh.hash(vector);
        int lastBucket = hash.length - 1;
        return hash[lastBucket];
    }

    public List<Long> getNearest(double[] vector) throws IOException {
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

    private void close(Map<Integer, List<Long>> data, Path location) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(String.valueOf(location)), data);
    }

    public void add(Embedding embedding) throws IOException {
        double[] vector = embedding.vector();
        long id = embedding.id();
        int bucket = getBucket(vector);
        List<Long> Ids = bucketIdMap.get(bucket);

        if (Ids == null) {
            Ids = new ArrayList<>();
        }

        Ids.add(id);
        bucketIdMap.putIfAbsent(bucket, Ids);
        close(bucketIdMap, location);
    }

    public void add(List<Embedding> embeddings) throws IOException {
        for (Embedding e : embeddings) {
            add(e);
        }
    }

    public void remove(Long element) throws IOException {
        for (List<Long> list : bucketIdMap.values()) {
            list.remove(element);
        }
        close(bucketIdMap, location);
    }

}
