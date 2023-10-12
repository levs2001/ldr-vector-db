package ldr.server.serialization.my;

import java.util.ArrayList;
import java.util.List;

public class VectorEncoder implements DataEncoder<double[]> {
    DataEncoder<Integer> intCoder = new VarIntEncoder();
    DataEncoder<Double> doubleEncoder = new DoubleEncoder();

    @Override
    public byte[] encode(double[] data) {
        List<byte[]> bytesList = new ArrayList<>(data.length + 1);
        int sumBytesCount = 0;
        sumBytesCount += putToList(bytesList, data.length, intCoder);
        for (double el : data) {
            sumBytesCount += putToList(bytesList, el, doubleEncoder);
        }

        byte[] result = new byte[sumBytesCount];
        unwrapList(result, bytesList);

        return result;
    }

    @Override
    public DecodeResult<double[]> decode(byte[] bytes, int from) {
        DecodeResult<Integer> sizeDecode = intCoder.decode(bytes, from);
        double[] result = new double[sizeDecode.result()];
        int offset = from + sizeDecode.bytesCount();
        for (int i = 0; i < sizeDecode.result(); i++) {
            DecodeResult<Double> valDec = doubleEncoder.decode(bytes, offset);
            offset += valDec.bytesCount();
            result[i] = valDec.result();
        }

        return new DecodeResult<>(result, offset - from);
    }
}
