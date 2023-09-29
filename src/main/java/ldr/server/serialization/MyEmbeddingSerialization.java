package ldr.server.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.protobuf.InvalidProtocolBufferException;
import ldr.client.domen.Embedding;

public class MyEmbeddingSerialization implements IEmbeddingSerialization {
    @Override
    public byte[] toByteArray(Embedding embedding) {
        return new byte[0];
    }

    @Override
    public void writeTo(Embedding embedding, OutputStream output) throws IOException {

    }

    @Override
    public Embedding parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return null;
    }

    @Override
    public Embedding parseFrom(InputStream input) {
        return null;
    }
}
