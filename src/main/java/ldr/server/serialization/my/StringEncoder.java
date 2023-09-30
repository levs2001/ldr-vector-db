package ldr.server.serialization.my;

public class StringEncoder implements DataEncoder<String> {
    @Override
    public String decode(byte[] bytes) {
        return null;
    }

    @Override
    public byte[] encode(String data) {
        return new byte[0];
    }
}
