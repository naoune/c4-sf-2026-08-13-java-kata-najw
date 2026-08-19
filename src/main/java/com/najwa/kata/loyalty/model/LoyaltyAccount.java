package com.najwa.kata.loyalty.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class LoyaltyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID customerId;

    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PointBatch> pointBatches = new ArrayList<>();

    public LoyaltyAccount(UUID customerId) {
        this.customerId = customerId;
    }

    public void addBatch(PointBatch batch) {
        pointBatches.add(batch);
    }

    public long getBalance() {

        return pointBatches.stream()
                .filter(batch -> !batch.isExpired())
                .mapToLong(PointBatch::getPoints)
                .sum();
    }
}