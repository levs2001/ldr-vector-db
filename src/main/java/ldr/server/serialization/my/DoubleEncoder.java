package ldr.server.serialization.my;

import java.nio.ByteBuffer;

public class DoubleEncoder implements DataEncoder<Double> {
    @Override
    public byte[] encode(Double data) {
        return ByteBuffer.allocate(Double.BYTES).putDouble(data).array();
    }

    @Override
    public DecodeResult<Double> decode(byte[] bytes, int from) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        return new DecodeResult<>(bb.getDouble(from), Double.BYTES);
    }
}
