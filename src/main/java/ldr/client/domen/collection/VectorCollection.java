package ldr.client.domen.collection;

import java.util.Collection;
import java.util.List;

import ldr.client.domen.Embedding;
import ldr.client.domen.VectorCollectionResult;
import ldr.server.logic.IFilter;
import ldr.server.storage.IFastIndex;
import ldr.server.storage.drive.IHardDriveEmbeddings;
import ldr.server.storage.mem.IMemoryEmbeddings;

public class VectorCollection implements IVectorCollection {
    private IMemoryEmbeddings inMem;
    private IHardDriveEmbeddings inDrive;
    private IFastIndex fastIndex;
    private IFilter filterUtil;

    // FastHasher (вектор) -> близкие вектора:id
    // Фильтра

    @Override
    public void add(Embedding embedding) throws CollectionException {

    }

    @Override
    public void add(List<Embedding> embeddings) throws CollectionException {

    }

    @Override
    public void update(long id, Embedding newEmbedding) throws CollectionException {

    }

    @Override
    public void update(List<Long> ids, List<Long> newEmbeddings) throws CollectionException {

    }

    @Override
    public VectorCollectionResult query(List<Double> vector, long maxNeighborsCount) {
//        List<Long> nearest = fastIndex.getNearest(vector);
//
//        List<Embedding> memIds = inMem.get(nearest);
//        if (memIds.size() == nearest.size()) {
//            // Все нашли.
////            return //
//        }
////        nearest.remove(memIds);
//        List<Embedding> driveIds = inDrive.get();
//
        // Мержим 2 списка с преоритетом у inMem

        return null;
    }

    @Override
    public VectorCollectionResult query(List<Double> vector, long maxNeighborsCount, String filter) {
        return null;
    }

    @Override
    public void delete(long id) throws CollectionException {

    }

    @Override
    public void delete(Collection<Long> ids) throws CollectionException {

    }

    @Override
    public void delete(String filter) throws CollectionException {

    }

    @Override
    public void close() throws CollectionException {
        // Создаем MemoryEmbeddings
        // Кладем туда смерженные inMem и inDrive
    }
}
