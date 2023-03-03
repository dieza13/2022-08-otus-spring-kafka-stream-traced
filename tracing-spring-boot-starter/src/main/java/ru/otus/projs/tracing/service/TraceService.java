package ru.otus.projs.tracing.service;

import io.micrometer.tracing.Span;
import ru.otus.projs.tracing.model.TracedEntity;

import java.util.function.Consumer;
import java.util.function.Function;

public interface TraceService {
//    <ResultType> ResultType tracingAction(Function< Span, ResultType> action, String traceId, String parentId);
    <ResultType> ResultType tracingAction(Function< Span, ResultType> action, TracedEntity tracedEntity);

//    void tracingAction(Consumer<Span> action, String traceId, String parentId);
    void tracingAction(Consumer<Span> action, TracedEntity tracedEntity);
}
