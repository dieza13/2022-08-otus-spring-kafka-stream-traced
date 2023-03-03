package ru.otus.projs.tracing.serializer.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import ru.otus.projs.tracing.model.TracedEntity;

public class SimpleJsonSerializer<T extends TracedEntity> extends JsonSerializer<T> {

    private static final String TRACE_HEADER = "traceparent";

    protected final ObjectMapper objectMapper;

    @Override
    @Nullable
    public byte[] serialize(String topic, Headers headers, @Nullable T data) {
        if (data == null) {
            return null;
        }
        headers.remove(TRACE_HEADER);
        headers.add(TRACE_HEADER, data.getTraceId().getBytes());
        return serialize(topic, data);
    }

    public SimpleJsonSerializer() {
        this.objectMapper = new ObjectMapper();
    }

    public SimpleJsonSerializer(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null.");
        this.objectMapper = objectMapper;
    }
}
