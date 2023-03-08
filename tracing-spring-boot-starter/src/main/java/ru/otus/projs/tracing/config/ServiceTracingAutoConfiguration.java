package ru.otus.projs.tracing.config;


import io.micrometer.tracing.Tracer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import ru.otus.projs.tracing.service.TraceService;
import ru.otus.projs.tracing.service.TraceServiceImpl;

@AutoConfiguration
@ConditionalOnClass(TraceService.class)
public class ServiceTracingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TraceService traceService(Tracer tracer) {
        return new TraceServiceImpl(tracer);
    }
}
