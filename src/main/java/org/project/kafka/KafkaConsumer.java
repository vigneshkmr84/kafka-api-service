package org.project.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication//(exclude = KafkaConfig.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = { "org.project.kafka.service", "org.project.kafka.controller" })
public class KafkaConsumer {

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumer.class, args);
    }

}
