package ldr.server.serialization.my;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringMapEncoder implements DataEncoder<Map<String, String>> {
    private static final DataEncoder<Integer> intCoder = new VarIntEncoder();
    private static final DataEncoder<String> strCoder = new StringEncoder();

    @Override
    public byte[] encode(Map<String, String> data) {
        List<byte[]> mapBytesList = new ArrayList<>(data.size() * 2 + 1);
        int sumBytesCount = 0;
        sumBytesCount += putToList(mapBytesList, data.size(), intCoder);
        for (var entry : data.entrySet()) {
            sumBytesCount += putToList(mapBytesList, entry.getKey(), strCoder);
            sumBytesCount += putToList(mapBytesList, entry.getValue(), strCoder);
        }

        byte[] result = new byte[sumBytesCount];
        unwrapList(result, mapBytesList);
        return result;
    }

    @Override
    public DecodeResult<Map<String, String>> decode(byte[] bytes, int from) {
        DecodeResult<Integer> sizeDecode = intCoder.decode(bytes, from);
        Map<String, String> result = new HashMap<>(sizeDecode.result());
        int offset = from + sizeDecode.bytesCount();
        for (int i = 0; i < sizeDecode.result(); i++) {
            DecodeResult<String> keyDec = strCoder.decode(bytes, offset);
            offset += keyDec.bytesCount();
            DecodeResult<String> valDec = strCoder.decode(bytes, offset);
            offset += valDec.bytesCount();

            result.put(keyDec.result(), valDec.result());
        }

        return new DecodeResult<>(result, offset - from);
    }
}
