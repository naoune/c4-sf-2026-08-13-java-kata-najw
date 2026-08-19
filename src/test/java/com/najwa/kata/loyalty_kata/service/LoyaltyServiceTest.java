package com.najwa.kata.loyalty.service;

import com.najwa.kata.loyalty.kafka.LoyaltyEventProducer;
import com.najwa.kata.loyalty.model.LoyaltyAccount;
import com.najwa.kata.loyalty.model.PointBatch;
import com.najwa.kata.loyalty.repository.LoyaltyAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoyaltyServiceTest {

    private LoyaltyAccountRepository repository;
    private LoyaltyEventProducer eventProducer;
    private LoyaltyService service;

    @BeforeEach
    void setUp() {

        repository = mock(LoyaltyAccountRepository.class);
        eventProducer = mock(LoyaltyEventProducer.class);

        service = new LoyaltyService(
                repository,
                eventProducer
        );
    }

    @Test
    void shouldEarnPoints() {

        UUID accountId = UUID.randomUUID();

        LoyaltyAccount account =
                new LoyaltyAccount(accountId);

        when(repository.findById(accountId))
                .thenReturn(Optional.of(account));

        service.earn(accountId, 100);

        assertEquals(
                100,
                account.getBalance()
        );
    }

    @Test
    void shouldSpendPointsUsingFifo() {

        UUID accountId = UUID.randomUUID();

        LoyaltyAccount account =
                new LoyaltyAccount(accountId);

        PointBatch firstBatch =
                new PointBatch(account, 100);

        PointBatch secondBatch =
                new PointBatch(account, 50);

        account.addBatch(firstBatch);
        account.addBatch(secondBatch);

        when(repository.findById(accountId))
                .thenReturn(Optional.of(account));

        service.spend(accountId, 120);

        assertEquals(
                0,
                firstBatch.getPoints()
        );

        assertEquals(
                30,
                secondBatch.getPoints()
        );
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsNotEnough() {

        UUID accountId = UUID.randomUUID();

        LoyaltyAccount account =
                new LoyaltyAccount(accountId);

        account.addBatch(
                new PointBatch(account, 50)
        );

        when(repository.findById(accountId))
                .thenReturn(Optional.of(account));

        assertThrows(
                IllegalStateException.class,
                () -> service.spend(accountId, 100)
        );
    }
}