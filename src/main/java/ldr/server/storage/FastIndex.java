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

    public record Config(Path location, int VECTOR_LEN){};

    public FastIndex(int VECTOR_LEN, Path location) {
        this.location = location;
        this.VECTOR_LEN = VECTOR_LEN;
        this.lsh = new LSHSuperBit(STAGES, BUCKETS, VECTOR_LEN, INITIAL_SEED);
    }


    private int getBucket(double[] vector){
        int[] hash = lsh.hash(vector);
        int lastBucket = hash.length - 1;
        return hash[lastBucket];
    }

/* Данная функция принимает массив double (vector из embedding), получает номер бакета для него.
   После чего считывает из json со словарем {bucket -> Список Id} и возвращает  все id данного бакета.
   Если такого хеша нет в файле, она возвращает пустой список Long.
 */

    public List<Long> getNearest(double[] vector) throws IOException {
        int bucket = getBucket(vector);
        Map<Integer, List<Long>> bucketIdMap = load(location);
        List<Long> listId = bucketIdMap.get(bucket);

        if (listId == null) {
            listId = new ArrayList<>();
        }
        return listId;
    }

    private static Map<Integer, List<Long>> load(Path location) {
        ObjectMapper mapper = new ObjectMapper();

        if (Files.exists(location)) {
            try {
                File jsonFile = location.toFile();
                Map<Integer, List<Long>> data = mapper.readValue(jsonFile, new TypeReference<>() {});
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
        Map<Integer, List<Long>> bucketIdMap = load(location);
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
        for (Embedding e : embeddings) {add(e);}
    }

}
