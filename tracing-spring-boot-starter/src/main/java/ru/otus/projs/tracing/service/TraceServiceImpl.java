package ru.otus.projs.tracing.service;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.otus.projs.tracing.model.TracedEntity;

import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TraceServiceImpl implements TraceService {

    private final Tracer tracer;

    @Override
    public <ResultType> ResultType tracingAction(Function<Span, ResultType> action, TracedEntity tracedEntity) {
        String fullTraceId = tracedEntity.getTraceId();
        String traceId = fullTraceId.split("-")[1];
        String parentId = fullTraceId.split("-")[2];

         Span.Builder builder= tracer.spanBuilder().name(this.getClass().getSimpleName());
         if (Strings.isNotEmpty(traceId) && Strings.isNotEmpty(parentId)) {
             builder.setParent(tracer.traceContextBuilder()
                     .traceId(traceId)
                     .spanId(parentId)
                     .build()
             );
         }
        Span newSpan = builder.start();
        String newSpanId = newSpan.context().spanId();
        String newTraceId = fullTraceId.replace(parentId, newSpanId);
        tracedEntity.setTraceId(newTraceId);
        try(Tracer.SpanInScope ws = this.tracer.withSpan(newSpan)) {
            return action.apply(newSpan);
        }finally {
            newSpan.end();
        }
    }



    @Override
    public void tracingAction(Consumer<Span> action, TracedEntity tracedEntity) {
        String fullTraceId = tracedEntity.getTraceId();
        String traceId = fullTraceId.split("-")[1];
        String parentId = fullTraceId.split("-")[2];

         Span.Builder builder= tracer.spanBuilder().name(this.getClass().getSimpleName());
         if (Strings.isNotEmpty(traceId) && Strings.isNotEmpty(parentId)) {
             builder.setParent(tracer.traceContextBuilder()
                     .traceId(traceId)
                     .spanId(parentId)
                     .build()
             );
         }
        Span newSpan = builder.start();
        String newSpanId = newSpan.context().spanId();
        String newTraceId = fullTraceId.replace(parentId, newSpanId);
        tracedEntity.setTraceId(newTraceId);
        try(Tracer.SpanInScope ws = this.tracer.withSpan(newSpan)) {
            action.accept(newSpan);
        }finally {
            newSpan.end();
        }
    }
}
