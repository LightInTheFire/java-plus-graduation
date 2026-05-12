package ru.yandex.practicum.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ru.yandex.practicum.exception.AvroSerializationException;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;

public class AvroSerializer implements Serializer<SpecificRecordBase> {

    private static final EncoderFactory ENCODER_FACTORY = EncoderFactory.get();

    @Override
    public byte[] serialize(String topic, SpecificRecordBase data) {
        if (data == null) {
            return null;
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            SpecificDatumWriter<SpecificRecordBase> writer = new SpecificDatumWriter<>(data.getSchema());
            BinaryEncoder encoder = ENCODER_FACTORY.binaryEncoder(outputStream, null);

            writer.write(data, encoder);
            encoder.flush();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new AvroSerializationException("Failed to serialize Avro message for topic " + topic, e);
        }
    }
}
