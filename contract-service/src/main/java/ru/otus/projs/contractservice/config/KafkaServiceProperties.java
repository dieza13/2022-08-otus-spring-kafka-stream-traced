package ru.otus.projs.contractservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@Configuration
public class KafkaServiceProperties {

    @Value("${spring.cloud.stream.bindings.contractProcessor-in-0.destination}")
    private String processingTopic;

    @Value("${app.params.store-name}")
    private String storeName;

}
