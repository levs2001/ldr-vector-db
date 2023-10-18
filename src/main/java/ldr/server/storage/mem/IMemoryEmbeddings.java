package ldr.server.storage.mem;

import ldr.server.storage.IStorageEmbeddings;

public interface IMemoryEmbeddings extends IStorageEmbeddings {
    boolean isNeedFlush();
}
