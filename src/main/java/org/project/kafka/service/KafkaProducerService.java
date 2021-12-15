package org.project.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    //private static final String TOPIC_NAME = "test-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     *
     * @param key Kafka Key
     * @param message Message (Value)
     * @param topicName Topic Name to insert messages to
     */
    public void sendMessage(String key, String message, String topicName) {
        log.info(String.format("Message sent -> %s", message));
        this.kafkaTemplate.send(topicName, key, message);
    }
}