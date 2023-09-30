package ldr.server.storage.drive;

import java.util.Iterator;
import java.util.List;

import ldr.client.domen.Embedding;

public class HardDriveEmbeddings implements IHardDriveEmbeddings {
    private HardDriveEmbeddings() {

    }

    public static IHardDriveEmbeddings create() {
        return new HardDriveEmbeddings();
    }

    // TODO: Exception if not exist
    public static IHardDriveEmbeddings load() {
        return new HardDriveEmbeddings();
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

    @Override
    public void save(Iterator<Embedding> embeddingIterator) {

    }
}
