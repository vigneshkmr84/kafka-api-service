package org.learning.kafkaexample.consumer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        System.out.println("health-OK");
        return ResponseEntity.ok().body("OK");
    }

}
