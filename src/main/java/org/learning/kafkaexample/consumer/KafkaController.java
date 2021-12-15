package org.learning.kafkaexample.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    KafkaProducerService kafkaProducerService;


    @GetMapping("/produce")
    public String produce(){
        kafkaProducerService.sendMessage("{\"id\":1,\"first_name\":\"Elaine\"}");

        return "OK";
    }

}
