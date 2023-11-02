package ldr.server.storage.meta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: Написать тесты
public class MetaStorage implements IMetaStorage {
    // id, которые удовлетворяют данному коду (key + value).hashCode.
    private final Map<Integer, Set<Long>> idsSets;

    public MetaStorage() {
        this.idsSets = new HashMap<>();
    }

    @Override
    public void put(long id, Map<String, String> metas) {
        for(var meta : metas.entrySet()) {
            int hash = getHash(meta);
            if (idsSets.get(hash) == null) {
                idsSets.put(hash, new HashSet<>());
            }

            var idsSet = idsSets.get(hash);
            idsSet.add(id);
        }
    }

    @Override
    public void put(List<Long> ids, List<Map<String, String>> metasList) {

    }

    @Override
    public List<Long> get(Map<String, String> metas) {
        for (var meta : metas.entrySet()) {
            int hash = getHash(meta);
            var idsSet = idsSets.get(hash);
            // Берем общее подмножество для всех этих множеств
        }

        // возвращаем общее подмножество
        // TODO:
        return null;
    }

    @Override
    public void close() {

    }

    private int getHash(Map.Entry<String, String> meta) {
        return (meta.getKey() + meta.getValue()).hashCode();
    }
}
