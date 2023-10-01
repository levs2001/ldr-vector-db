package ldr.server.serialization.my;

/**
 * VarInt is the same as varLong, so we reuse it.
 */
public class VarIntEncoder implements DataEncoder<Integer> {
    private static final DataEncoder<Long> longEncoder = new VarLongEncoder();

    @Override
    public DecodeResult<Integer> decode(byte[] bytes, int from) {
        DecodeResult<Long> longRes = longEncoder.decode(bytes, from);
        if (longRes.bytesCount() > Integer.BYTES + 1) {
            throw new RuntimeException("Too many bytes in VarInt");
        }
        return new DecodeResult<>((int) (long) longRes.result(), longRes.bytesCount());
    }

    @Override
    public byte[] encode(Integer data) {
        return longEncoder.encode((long) data);
    }
}
