package ldr.server.serialization;

import org.junit.jupiter.api.Test;

import com.google.protobuf.InvalidProtocolBufferException;
import ldr.client.domen.Embedding;

class MyEmbeddingSerializationTest {
    TestFromBytesToBytesUtil testUtil = new TestFromBytesToBytesUtil(new MyEmbeddingSerialization());

    @Test
    public void testFromBytesToBytes() throws InvalidProtocolBufferException {
        for (Embedding embedding : Embeddings.examples) {
            testUtil.makeTest(embedding);
        }
    }
}