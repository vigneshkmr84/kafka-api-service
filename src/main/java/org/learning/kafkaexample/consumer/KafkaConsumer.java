package org.learning.kafkaexample.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(exclude = KafkaConfig.class)
@EnableAutoConfiguration
public class KafkaConsumer {

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumer.class, args);
    }

}
