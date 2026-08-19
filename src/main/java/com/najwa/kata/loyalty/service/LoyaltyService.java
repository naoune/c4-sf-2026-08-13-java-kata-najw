package com.najwa.kata.loyalty.service;

import com.najwa.kata.loyalty.kafka.LoyaltyEvent;
import com.najwa.kata.loyalty.kafka.LoyaltyEventProducer;
import com.najwa.kata.loyalty.model.LoyaltyAccount;
import com.najwa.kata.loyalty.model.PointBatch;
import com.najwa.kata.loyalty.repository.LoyaltyAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoyaltyService {

    private final LoyaltyAccountRepository repository;
    private final LoyaltyEventProducer eventProducer;

    public void earn(UUID accountId, long points) {

        LoyaltyAccount account = getAccount(accountId);

        PointBatch batch = new PointBatch(
                account,
                points
        );

        account.addBatch(batch);

        repository.save(account);

        eventProducer.send(
                new LoyaltyEvent(
                        accountId,
                        "POINTS_EARNED",
                        points
                )
        );
    }

    public void spend(UUID accountId, long points) {

        LoyaltyAccount account = getAccount(accountId);

        if (account.getBalance() < points) {
            throw new IllegalStateException(
                    "Not enough points"
            );
        }

        var batches = account.getPointBatches()
                .stream()
                .filter(batch -> !batch.isExpired())
                .sorted(
                        Comparator.comparing(
                                PointBatch::getEarnedAt
                        )
                )
                .toList();

        long remaining = points;

        for (PointBatch batch : batches) {

            if (remaining <= 0) {
                break;
            }

            long consumed = Math.min(
                    batch.getPoints(),
                    remaining
            );

            batch.consume(consumed);

            remaining -= consumed;
        }

        repository.save(account);

        eventProducer.send(
                new LoyaltyEvent(
                        accountId,
                        "POINTS_SPENT",
                        points
                )
        );
    }

    public String createVoucher(
            UUID accountId,
            long points
    ) {

        spend(accountId, points);

        String voucherCode =
                "VCH-" +
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 8);

        eventProducer.send(
                new LoyaltyEvent(
                        accountId,
                        "VOUCHER_CREATED",
                        points
                )
        );

        return voucherCode;
    }

    public void donate(
            UUID accountId,
            long points
    ) {

        spend(accountId, points);

        eventProducer.send(
                new LoyaltyEvent(
                        accountId,
                        "POINTS_DONATED",
                        points
                )
        );
    }

    public LoyaltyAccount getAccount(UUID accountId) {

        return repository.findById(accountId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Account not found"
                        )
                );
    }

    @Scheduled(cron = "0 0 * * * *")
    public void expirePoints() {

        List<LoyaltyAccount> accounts =
                repository.findAll();

        for (LoyaltyAccount account : accounts) {

            for (PointBatch batch :
                    account.getPointBatches()) {

                if (!batch.isExpired()
                        || batch.getPoints() == 0) {
                    continue;
                }

                long expired =
                        batch.getPoints();

                batch.consume(expired);

                eventProducer.send(
                        new LoyaltyEvent(
                                account.getCustomerId(),
                                "POINTS_EXPIRED",
                                expired
                        )
                );
            }

            repository.save(account);
        }
    }

}