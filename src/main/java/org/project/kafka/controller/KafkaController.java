package org.project.kafka.controller;

import org.project.kafka.model.Order;
import org.project.kafka.service.KafkaProducerService;
import org.project.kafka.utils.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class KafkaController {

    private static final Logger log = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private JsonParser jsonParser = new JsonParser();

    private RestTemplate restTemplate = new RestTemplate();

    private static final String REDIS_URL = "http://localhost:5000/insert";
    private static final String SERVICE_NAME = "service-1";

    @PostMapping("/order")
    public ResponseEntity<String> produce(@RequestBody Order order) {

        log.info("Order Received - " + order.getId());
        String message = jsonParser.objectToString(order);

        String finalURL = REDIS_URL + "?status=success&" + "serviceName=" + SERVICE_NAME;
        log.info("Final URL for Redis : " + finalURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonParser.objectToString(order), headers);
        ResponseEntity<String> response = restTemplate.exchange(finalURL, HttpMethod.POST, entity, String.class);

        log.info("Status Code : " + response.getStatusCode());
        log.info("Status Message : " + response.getBody());

        kafkaProducerService.sendMessage(message, "999","topic2");

        return ResponseEntity.accepted().body("Order Placed Successfully");
    }


    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("health-OK");
        return ResponseEntity.ok().body("OK");
    }

}
