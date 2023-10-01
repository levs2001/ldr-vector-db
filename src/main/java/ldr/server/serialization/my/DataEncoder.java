package ldr.server.serialization.my;

import java.util.List;

public interface DataEncoder<D> {
    byte[] encode(D data);

    DecodeResult<D> decode(byte[] bytes, int from);

    default DecodeResult<D> decode(byte[] bytes) {
        return decode(bytes, 0);
    }

    /**
     * Put encoded data to byte array list.
     */
    default <T> int putToList(List<byte[]> bytesList, T toEncode, DataEncoder<T> coder) {
        byte[] result = coder.encode(toEncode);
        bytesList.add(result);

        return result.length;
    }

    default void unwrapList(byte[] result, List<byte[]> bytesList) {
        int pos = 0;
        for (byte[] bytes : bytesList) {
            System.arraycopy(bytes, 0, result, pos, bytes.length);
            pos += bytes.length;
        }
    }

    /**
     * @param result     of decoding.
     * @param bytesCount - count of read bytes during decoding (size of decoded info in coded byte view).
     */
    record DecodeResult<T>(T result, int bytesCount) {
    }
}
