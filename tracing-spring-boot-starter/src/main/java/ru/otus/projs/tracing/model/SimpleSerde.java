package ru.otus.projs.tracing.model;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

@RequiredArgsConstructor
public class SimpleSerde<T> implements Serde<T> {

    private final Deserializer<T> deserializer;
    private final Serializer<T> serializer;


    @Override
    public Serializer<T> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<T> deserializer() {
        return deserializer;
    }
}
