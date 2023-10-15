package ldr.server.storage;

import info.debatty.java.lsh.LSHSuperBit;
import ldr.client.domen.Embedding;

import java.util.*;

public class FastIndex implements IFastIndex {
    private int buckets;
    private int vectorLen;
    private List<Embedding> examples;
    final int STAGES = 1;
    private LSHSuperBit lsh = new LSHSuperBit();

    public FastIndex(int buckets, int vectorLen, List<Embedding> examples) {
        this.buckets = buckets;
        this.vectorLen = vectorLen;
        this.examples = examples;
        this.lsh = new LSHSuperBit(STAGES, buckets, vectorLen);
    }

    @Override
    public Map<Integer, List<Embedding>> getBuckets() {
        Map<Integer, List<Embedding>> embeddingMap = new HashMap<>();

        for (Embedding example : examples) {
            double[] vector = example.vector();
            int[] hash = lsh.hash(vector);

            List<Embedding> listForKey = embeddingMap.get(hash[0]);

            if (listForKey == null) {
                listForKey = new ArrayList<>();
            }

            listForKey.add(example);
            embeddingMap.put(hash[0], listForKey);
        }
        return embeddingMap;
    }

    @Override
    public List<Embedding> getNearest(double[] vector, Map<Integer, List<Embedding>> embeddingMap) {
        int[] hash = lsh.hash(vector);
        List<Embedding> bucketEmbeddings = embeddingMap.get(hash[0]);
        if (bucketEmbeddings == null) {
            bucketEmbeddings = new ArrayList<>();
        }
        return bucketEmbeddings;
    }
}
