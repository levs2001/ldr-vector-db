package ldr.server.storage.mem;

import java.util.Iterator;

import ldr.client.domen.Embedding;
import ldr.server.storage.IEmbeddingKeeper;

public interface IMemoryEmbeddings extends IEmbeddingKeeper {
    Iterator<Embedding> getAll();

    boolean isNeedFlush();
}
