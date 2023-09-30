package ldr.server.serialization.my;

public interface DataEncoder<D> {
    D decode(byte[] bytes);
    byte[] encode(D data);
}
