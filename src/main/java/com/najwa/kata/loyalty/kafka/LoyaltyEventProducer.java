package com.najwa.kata.loyalty.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoyaltyEventProducer {

    private static final String TOPIC = "loyalty-events";

    private final KafkaTemplate<String, LoyaltyEvent> kafkaTemplate;

    public void send(LoyaltyEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event.accountId().toString(),
                event
        );
    }
}