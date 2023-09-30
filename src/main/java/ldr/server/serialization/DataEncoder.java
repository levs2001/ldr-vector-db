package ldr.server.serialization;

public interface DataEncoder<D> {
    D decode(byte[] bytes);
    byte[] encode(D data);
}
