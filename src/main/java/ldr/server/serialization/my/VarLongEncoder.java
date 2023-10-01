package ldr.server.serialization.my;

import java.util.ArrayList;
import java.util.List;

public class VarLongEncoder implements DataEncoder<Long> {
    private static final long MAX_VALUE = Long.MAX_VALUE / 2;
    private static final long MIN_VALUE = Long.MIN_VALUE / 2;
    // 01111111
    private static final byte SEGMENT_BITS =  0x7F;
    // 10000000
    private static final int CONTINUE_BIT = 0x80;

    @Override
    public byte[] encode(Long value) {
        if (value > MAX_VALUE || value < MIN_VALUE) {
            throw new RuntimeException("Value incorrect diapason should be between " +
                    MIN_VALUE + " and " + MAX_VALUE + ", your value " + value);
        }

        // Zig Zag
        long unsignedValue = (value << 1) ^ (value >> 63);

        List<Byte> encodedBytes = new ArrayList<>();
        //пока есть байты
        while (unsignedValue > 0) {
            // В каждом байте храним 7 бит числа и бит продолжения
            byte b = (byte) (unsignedValue & SEGMENT_BITS);
            unsignedValue >>= 7;
            // Устанавливаем бит продолжения, если есть ещё группы для кодирования
            if (unsignedValue > 0) {
                b |= CONTINUE_BIT;
            }
            encodedBytes.add(b);
        }
        // Если байты закончились
        if (encodedBytes.isEmpty()) {
            encodedBytes.add((byte) 0);
        }

        // Преобразуем список байтов в массив байтов и возвращаем как результат
        byte[] result = new byte[encodedBytes.size()];
        for (int i = 0; i < encodedBytes.size(); i++) {
            result[i] = encodedBytes.get(i);
        }
        return result;
    }

    @Override
    public Long decode(byte[] encodedBytes) {
        long result = 0;
        byte shift = 0;

        for (byte byteValue : encodedBytes) {

            // Очищаем бит продолжения и объединяем с результатом
            result |= (long) (byteValue & SEGMENT_BITS) << shift;

            // Сдвиг на 7 бит для обработки следующего байта
            shift += 7;

            // Если бит продолжения равен 0, это последний байт числа
            if ((byteValue & CONTINUE_BIT) == 0) {
                break;
            }
        }

        // Раскодирование zig zag
        return (result >> 1) ^ (-(result & 1));
    }

}

