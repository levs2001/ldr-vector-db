package ldr.server.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import info.debatty.java.lsh.LSHSuperBit;
import ldr.client.domen.Embedding;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastIndex implements IFastIndex {
    private final static int BUCKETS = 5;
    private final static int VECTOR_LEN = 3;
    private final static int STAGES = 5;
    private final static int INITIAL_SEED = 42;
    private final static LSHSuperBit lsh = new LSHSuperBit(STAGES, BUCKETS, VECTOR_LEN, INITIAL_SEED);


    /* Данная функция принимает массив double (vector из embedding), получает номер бакета для него.
       После чего считывает из json со словарем {bucket -> Список Id} и возвращает  все id данного бакета.
       Если такого хеша нет в файле, она возвращает пустой список Long.
     */
    public List<Long> getNearest(double[] vector) throws IOException {
        int[] hash = lsh.hash(vector);
        int lastBucket = hash.length - 1;

        Map<Integer, List<Long>> bucketIdMap = readMapFromJsonFile("data.json");
        List<Long> listId = bucketIdMap.get(hash[lastBucket]);

        if (listId == null) {
            listId = new ArrayList<>();
        }
        return listId;
    }

    /* Данная функция считывает JSON-данные из указанного файла (filePath)
     с использованием библиотеки Jackson и преобразует его в объект Map<Integer, List<Long>>.
     Нужна для считывания словаря {bucket -> Список Id}
    */
    private static Map<Integer, List<Long>> readMapFromJsonFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), new TypeReference<>() {
        });
    }


    // Функция возвращает словарь {bucket -> Список Id}, нужна для записи его потом json, применяется для сгенерированных эмбеддингов.
    // По идее временная функция, пока не совсем понимаю как буду обновлять файл с бакетами когда будут добавляться новые ембеддинги.
    // В плане, нужно делать append, а не считывать каждый json.
    public Map<Integer, List<Long>> calcaulateLoadBuckets(List<Embedding> embeddings) {
        Map<Integer, List<Long>> bucketIdMap = new HashMap<>();

        for (Embedding e : embeddings) {
            double[] vector = e.vector();
            long id = e.id();
            int[] hash = lsh.hash(vector);
            int lastBucket = hash.length - 1;

            List<Long> listIds = bucketIdMap.get(hash[lastBucket]);

            if (listIds == null) {
                listIds = new ArrayList<>();
            }
            listIds.add(id);
            bucketIdMap.put(hash[hash.length - 1], listIds);
        }
        return bucketIdMap;


    }
}
