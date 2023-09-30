package ldr.server.serialization.my;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class VarLongEncoder implements DataEncoder<Long> {
    @Override
    public byte[] encode(Long value) {
        // Zig Zag
        if (value > Long.MAX_VALUE / 2 || value < Long.MIN_VALUE / 2) {
            throw new RuntimeException("Value incorrect diapason should be between " +
                    Long.MIN_VALUE / 2 + "and " + Long.MAX_VALUE / 2 + " your value " + value);
        }
        long unsignedValue = (value << 1) ^ (value >> 63);

        List<Byte> encodedBytes = new ArrayList<>();

        //пока есть байты
        while (unsignedValue > 0) {
            // В каждом байте храним 7 бит числа и бит продолжения
            byte b = (byte) (unsignedValue & 0x7F);
            unsignedValue >>= 7;
            // Устанавливаем бит продолжения, если есть ещё группы для кодирования
            if (unsignedValue > 0) {
                b |= 0x80;
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
        long shift = 0;

        for (byte byteValue : encodedBytes) {

            // Очищаем бит продолжения и объединяем с результатом
            result |= (long) (byteValue & 0x7F) << shift;

            // Сдвиг на 7 бит для обработки следующего байта
            shift += 7;

            // Если бит продолжения равен 0, это последний байт числа
            if ((byteValue & 0x80) == 0) {
                break;
            }
        }

        // Раскодирование zig zag
        return (result >> 1) ^ (-(result & 1));
    }

}

