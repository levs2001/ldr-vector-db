package ldr.server.serialization.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import ldr.client.domen.Embedding;
import ldr.server.serialization.IEmbeddingSerialization;

public class ProtobufEmbeddingSerialization implements IEmbeddingSerialization {
    @Override
    public Embedding parseFrom(byte[] data) throws InvalidProtocolBufferException {
        EmbeddingOuterClass.Embedding parsedEmbedding = EmbeddingOuterClass.Embedding.parseFrom(data);
        return new Embedding(parsedEmbedding.getId(), parsedEmbedding.getValueList(), parsedEmbedding.getMetasMap());
    }


    @Override
    public Embedding parseFrom(InputStream input) throws IOException {
        EmbeddingOuterClass.Embedding parsedEmbedding = EmbeddingOuterClass.Embedding.parseFrom(input);
        return new Embedding(parsedEmbedding.getId(), parsedEmbedding.getValueList(), parsedEmbedding.getMetasMap());
    }

    @Override
    public byte[] toByteArray(Embedding embedding) {
        return EmbeddingOuterClass.Embedding.newBuilder()
                .setId(embedding.id())
                .addAllValue(embedding.value())
                .putAllMetas(embedding.metas())
                .build()
                .toByteArray();
    }

    @Override
    public void writeTo(Embedding embedding, OutputStream output) throws IOException {
        EmbeddingOuterClass.Embedding.newBuilder()
                .setId(embedding.id())
                .addAllValue(embedding.value())
                .putAllMetas(embedding.metas())
                .build()
                .writeTo(output);
    }
}
