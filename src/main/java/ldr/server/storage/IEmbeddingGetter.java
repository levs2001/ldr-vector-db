package ldr.server.storage;

import java.util.List;

import ldr.client.domen.Embedding;
import ldr.server.storage.drive.HardDriveEmbeddings;
import ldr.server.storage.mem.MemoryEmbeddings;

public interface IEmbeddingGetter {
    /**
     * @return Embedding with given id, if it isn't presented returns null.
     */
    Embedding get(long id);

    /**
     * List of all found embeddings with given ids.
     */
    List<Embedding> get(List<Long> ids);
}
