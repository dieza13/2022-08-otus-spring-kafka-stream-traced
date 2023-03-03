package ru.otus.projs.tracing.model;

public interface TracedEntity {
    String getTraceId();
    void setTraceId(String traceId);
}
