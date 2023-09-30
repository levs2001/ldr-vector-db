package ldr.server.logic;

import java.util.List;

import ldr.client.domen.Embedding;

public interface IFilter {
    List<Embedding> filter(List<Embedding> embeddings, String filter);
}
