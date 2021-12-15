package org.learning.kafkaexample.consumer;

import org.learning.kafkaexample.consumer.model.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    @KafkaListener(topics = "test-topic", groupId = "demo-cluster")
    public void consume(@Payload String message
            , @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key
            ,@Header(KafkaHeaders.RECEIVED_TIMESTAMP) long receivedTimeStamp){
            //,@Header(KafkaHeaders.ORIGINAL_TIMESTAMP) long originalTimeStamp) {
        System.out.println("Received Message Key : " + key );
        System.out.println("Received Message timestamp : " + receivedTimeStamp );
        //System.out.println("Original Message timestamp : " + originalTimeStamp );
        System.out.println("Consumed message : " + message);
    }

}
