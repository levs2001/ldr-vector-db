package ldr.server.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.IntStream;

import com.google.protobuf.InvalidProtocolBufferException;
import ldr.client.domen.Embedding;

public interface IEmbeddingSerialization {
    Embedding parseFrom(byte[] data) throws InvalidProtocolBufferException;
    Embedding parseFrom(InputStream input) throws IOException;
    byte[] toByteArray(Embedding embedding);
    void writeTo(Embedding embedding, OutputStream output) throws IOException;
}
