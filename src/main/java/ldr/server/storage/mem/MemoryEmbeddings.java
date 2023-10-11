package ldr.server.storage.mem;

import java.util.Iterator;
import java.util.List;

import ldr.client.domen.Embedding;

public class MemoryEmbeddings implements IMemoryEmbeddings{
    @Override
    public void put(Embedding embedding) {

    }

    @Override
    public void put(List<Embedding> embeddings) {

    }

    @Override
    public Embedding get(long id) {
        return null;
    }

    @Override
    public List<Embedding> get(List<Long> ids) {
        return null;
    }

    @Override
    public Iterator<Embedding> getAll() {
        return null;
    }
}
