package ru.otus.projs.tracing.deserializer.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.Assert;
import ru.otus.projs.tracing.model.TracedEntity;

import java.io.IOException;
import java.util.Arrays;

public class SimpleJsonDeserializer<T extends TracedEntity> extends JsonDeserializer<T> {

    private static final String TRACE_HEADER = "traceparent";

    protected final ObjectMapper objectMapper;
    private final ObjectReader reader;

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            Header traceIdHeader = null;
            for (Header h : headers.headers(TRACE_HEADER)) {
                if (h.key().equals(TRACE_HEADER)) {
                    traceIdHeader = h;
                    break;
                }
            }
            String traceId = new String(traceIdHeader.value());
            T entity = reader.readValue(data);
            entity.setTraceId(traceId);
            return entity;
        }
        catch (IOException e) {
            throw new SerializationException("Can't deserialize data [" + Arrays.toString(data) +
                    "] from topic [" + topic + "]", e);
        }
    }

    public SimpleJsonDeserializer(Class<T> clazz) {
        this.objectMapper = new ObjectMapper();
        reader = this.objectMapper.readerFor(clazz);
    }

    public SimpleJsonDeserializer(ObjectMapper objectMapper, Class<T> clazz) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null.");
        this.objectMapper = objectMapper;
        reader = this.objectMapper.readerFor(clazz);
    }
}
