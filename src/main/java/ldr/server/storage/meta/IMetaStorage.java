package ldr.server.storage.meta;

import java.util.List;
import java.util.Map;

public interface IMetaStorage {
    void put(long id, Map<String, String> metas);

    void put(List<Long> ids, List<Map<String, String>> metasList);

    /**
     * Возвращает все id, которые подходят под данные мета.
     */
    List<Long> get(Map<String, String> metas);

    /**
     * Сохраняет все на диск, чтобы при открытии считать данные.
     */
    void close();
}
