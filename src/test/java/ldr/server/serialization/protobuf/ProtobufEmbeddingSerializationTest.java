package ldr.server.serialization.protobuf;

import org.junit.jupiter.api.Test;

import com.google.protobuf.InvalidProtocolBufferException;
import ldr.client.domen.Embedding;
import ldr.server.serialization.Embeddings;
import ldr.server.serialization.TestFromBytesToBytesUtil;

class ProtobufEmbeddingSerializationTest {
    TestFromBytesToBytesUtil testUtil = new TestFromBytesToBytesUtil(new ProtobufEmbeddingSerialization());

    @Test
    public void testFromBytesToBytes() throws InvalidProtocolBufferException {
        for (Embedding embedding : Embeddings.examples) {
            testUtil.makeTest(embedding);
        }
    }
}