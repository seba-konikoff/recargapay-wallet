package ar.com.sebakoni.recargapay.wallet.controller;

import ar.com.sebakoni.recargapay.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController()
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/wallets/{id}/balance")
    public BigDecimal getBalance(@PathVariable String id) {
        return this.walletService.getBalance(id);
    }
}
