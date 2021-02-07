package org.rj.homectl.kafka.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.LoggingErrorHandler;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

    @Bean
    public LoggingErrorHandler errorHandler() {
        return new LoggingErrorHandler();
    }

}
