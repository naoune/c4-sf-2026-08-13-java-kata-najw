package com.najwa.kata.loyalty.controller;

import com.najwa.kata.loyalty.model.LoyaltyAccount;
import com.najwa.kata.loyalty.service.LoyaltyService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/loyalty")
class LoyaltyController {

    private final LoyaltyService service;

    LoyaltyController(LoyaltyService service) {
        this.service = service;
    }


    /*
     * POST /loyalty/{accountId}/earn?points=100
     */

    @PostMapping("/{accountId}/earn")
    public void earn(
            @PathVariable UUID accountId,
            @RequestParam long points
    ) {

        service.earn(accountId, points);
    }


    /*
     * POST /loyalty/{accountId}/spend?points=50
     */

    @PostMapping("/{accountId}/spend")
    public void spend(
            @PathVariable UUID accountId,
            @RequestParam long points
    ) {

        service.spend(accountId, points);
    }


    /*
     * POST /loyalty/{accountId}/voucher?points=500
     */

    @PostMapping("/{accountId}/voucher")
    public void voucher(
            @PathVariable UUID accountId,
            @RequestParam long points
    ) {

        service.createVoucher(
                accountId,
                points
        );
    }


    /*
     * POST /loyalty/{accountId}/donation?points=200
     */

    @PostMapping("/{accountId}/donation")
    public void donation(
            @PathVariable UUID accountId,
            @RequestParam long points
    ) {

        service.donate(
                accountId,
                points
        );
    }


    /*
     * GET /loyalty/{accountId}
     */

    @GetMapping("/{accountId}")
    public LoyaltyAccount getAccount(
            @PathVariable UUID accountId
    ) {

        return service.getAccount(accountId);
    }
}