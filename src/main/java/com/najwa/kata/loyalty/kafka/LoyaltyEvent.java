package com.najwa.kata.loyalty.kafka;

import java.util.UUID;

public record LoyaltyEvent(
        UUID accountId,
        String type,
        long points
) {
}