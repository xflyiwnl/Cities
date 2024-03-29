package me.xflyiwnl.cities.buffer;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class CitiesBuffer {

    private ByteArrayOutputStream outputStream;

    public CitiesBuffer() {
        this.outputStream = new ByteArrayOutputStream();
    }

    public void writeString(@NotNull String string) {
        this.writeByteArray(string.getBytes(StandardCharsets.UTF_8));
    }

    public void writeByteArray(byte[] bytes) {
        this.writeVarInt(bytes.length);
        this.writeBytes(bytes);
    }

    public void writeVarInt(int value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                this.writeByte(value);
                return;
            }

            this.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
    }

    public void writeBytes(byte[] bytes) {
        this.outputStream.writeBytes(bytes);
    }

    public void writeByte(int value) {
        this.outputStream.write((byte) value);
    }

    public byte[] asByteArray() {
        return outputStream.toByteArray();
    }

}
