package com.najwa.kata.loyalty.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class PointBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private long points;

    private LocalDate earnedAt;

    private LocalDate expiresAt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private LoyaltyAccount account;

    public PointBatch(
            LoyaltyAccount account,
            long points
    ) {

        if (points <= 0) {
            throw new IllegalArgumentException(
                    "Points must be positive"
            );
        }

        this.account = account;
        this.points = points;
        this.earnedAt = LocalDate.now();
        this.expiresAt = LocalDate.now().plusYears(1);
    }

    public void consume(long points) {

        if (points <= 0 || points > this.points) {
            throw new IllegalArgumentException(
                    "Invalid points amount"
            );
        }

        this.points -= points;
    }

    public boolean isExpired() {
        return !expiresAt.isAfter(LocalDate.now());
    }
}