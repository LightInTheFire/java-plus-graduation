package ru.yandex.practicum.serialization;

import java.io.IOException;

import ru.yandex.practicum.exception.AvroSerializationException;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;

public class BaseAvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

    private static final DecoderFactory DECODER_FACTORY = DecoderFactory.get();
    private final DatumReader<T> datumReader;

    public BaseAvroDeserializer(Schema schema) {
        this.datumReader = new SpecificDatumReader<>(schema);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            BinaryDecoder decoder = DECODER_FACTORY.binaryDecoder(data, null);
            return datumReader.read(null, decoder);
        } catch (IOException e) {
            throw new AvroSerializationException("Failed to deserialize Avro message for topic " + topic, e);
        }
    }
}
