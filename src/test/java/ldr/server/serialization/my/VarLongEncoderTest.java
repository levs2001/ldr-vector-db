package ldr.server.serialization.my;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void testOutOfBorders() {
        List<Long> unsupported = List.of(Long.MAX_VALUE, Long.MAX_VALUE - 500, Long.MIN_VALUE, Long.MIN_VALUE + 500);
        DataEncoder<Long> coder = new VarLongEncoder();

        for (long x : unsupported) {
            assertThrows(RuntimeException.class, () -> coder.encode(x));
        }
    }
}