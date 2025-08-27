package com.example.clientsocketservice.Consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "sample-topic")
    public void listen(String message) {
        System.out.println("Received message from sample-topic: " + message);
    }

    @Service
    public static class KafkaConsumerService1 {
        @KafkaListener(topics = "sample-topic")
        public void listen(String message) {
            System.out.println("Kafka consumer new message from topic sample topic " + message);
        }
    }
}
