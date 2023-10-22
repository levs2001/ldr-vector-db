package ldr.server.storage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ldr.client.domen.Embedding;

public interface IFastIndex {
    // Static methods in interfaces should have a body, alt + enter вставил тело через idea.
    // Хз зачем ему тело, ну по идее пусть  лучше будут static мы же экземпляры генерить не будем

    static List<Long> getNearest(double[] vector) throws IOException {
        return null;
    }

    private static Map<Integer, List<Long>> readMapFromJsonFile(String filePath) throws IOException {
        return null;
    }


}
