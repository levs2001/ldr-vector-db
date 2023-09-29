package ldr.server.serialization;

import com.google.protobuf.InvalidProtocolBufferException;
import ldr.client.domen.Embedding;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Use to test different realizations of {@link IEmbeddingSerialization}
 */
public class TestFromBytesToBytesUtil {
    private final IEmbeddingSerialization serialization;

    public TestFromBytesToBytesUtil(IEmbeddingSerialization serialization) {
        this.serialization = serialization;
    }

    public void makeTest(Embedding embedding) throws InvalidProtocolBufferException {
        byte[] bytes = serialization.toByteArray(embedding);
        Embedding parsedFromBytesRes = serialization.parseFrom(bytes);
        assertEquals(embedding, parsedFromBytesRes);
    }
}
