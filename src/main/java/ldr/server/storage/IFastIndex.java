package ldr.server.storage;

import java.util.List;

import ldr.client.domen.Embedding;

public interface IFastIndex {
    List<Long> getNearest(List<Double> vector);
}
