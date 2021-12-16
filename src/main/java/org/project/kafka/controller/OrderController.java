package org.project.kafka.controller;

import org.project.kafka.model.Order;
import org.project.kafka.service.KafkaProducerService;
import org.project.kafka.utils.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private static final String REDIS_URL = "http://localhost:5000/insert";

    private final JsonParser jsonParser = new JsonParser();
    private static final int max = 1693;
    private static final int min = 3;
    private static final int mod = 11;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private Environment env;


    @PostMapping("/order")
    public ResponseEntity<String> produce(@RequestBody Order order) {
        String topic = env.getProperty("kafka.producer-topic");
        String serviceName = env.getProperty("service.name");

        log.info("Kafka Topic - " + topic);
        log.info("Order Received - " + order.getId());
        String message = jsonParser.objectToString(order);

        String finalURL = REDIS_URL + "?status=success&" + "serviceName=" + serviceName;
        log.info("Final URL for Redis : " + finalURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String orderId = uuidGenerator();
        log.info("Order Id Generated - " + orderId);
        order.setOrderId(orderId);
        RestTemplate restTemplate = context.getBean("restTemplate", RestTemplate.class);
        HttpEntity<String> entity = new HttpEntity<>(jsonParser.objectToString(order), headers);
        boolean flag = false;

        try {
            ResponseEntity<String> response = restTemplate.exchange(finalURL, HttpMethod.POST, entity, String.class);

            log.info("HTTP Status Code : " + response.getStatusCode());
            log.info("HTTP Message : " + response.getBody());

            log.info("Inserting message to kafka");
            // key will be generated order-id
            kafkaProducerService.sendMessage(orderId, message, topic);

        } catch (Exception e) {
            log.error("TimeOut occurred during Redis insert " + e.getMessage());
            flag = true;
        }

        if ( flag )
            return ResponseEntity.internalServerError().body("Internal Server Error");

        return ResponseEntity.accepted().body("Order Placed Successfully");
    }


    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("health-OK");
        return ResponseEntity.ok().body("OK");
    }

    public String uuidGenerator(){
        return UUID.randomUUID().toString(); //.replace("-", "");
    }

    // function to manually induce error during inserts
    public boolean genError(){
        return (int)Math.floor(Math.random()*(max-min+1)+min) % mod == 0;
    }
}
