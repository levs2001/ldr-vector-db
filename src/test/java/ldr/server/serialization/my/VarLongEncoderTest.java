package ldr.server.serialization.my;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VarLongEncoderTest {
    @Test
    public void testCoding() {
        List<Long> expList = List.of(Long.MAX_VALUE / 2, Long.MIN_VALUE / 2, -1231231L, 123L, 0L);

        DataEncoder<Long> coder = new VarLongEncoder();

        for (long x : expList) {
            byte[] encodedBytes = coder.encode(x);
            long decodedValue = coder.decode(encodedBytes);
            assertEquals(x, decodedValue);

        }

    }
}