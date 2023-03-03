package ru.otus.projs.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.otus.projs.tracing.model.TracedEntity;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TracedContractInfo extends Contract implements TracedEntity {
    private transient String traceId;

    @Override
    public String getTraceId() {
        return traceId;
    }

    @Override
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
