package ldr.server.storage.drive;

import java.util.Iterator;

import ldr.client.domen.Embedding;
import ldr.server.storage.IStorageEmbeddings;

public interface IHardDriveEmbeddings extends IStorageEmbeddings {
    void save(Iterator<Embedding> embeddingIterator);
}
