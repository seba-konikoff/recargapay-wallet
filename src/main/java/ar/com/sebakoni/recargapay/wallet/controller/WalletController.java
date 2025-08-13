package ar.com.sebakoni.recargapay.wallet.controller;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.exception.UserHasWalletException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletWithoutSufficientFundsException;
import ar.com.sebakoni.recargapay.wallet.model.Deposit;
import ar.com.sebakoni.recargapay.wallet.model.Transfer;
import ar.com.sebakoni.recargapay.wallet.model.WalletCreation;
import ar.com.sebakoni.recargapay.wallet.model.Withdrawal;
import ar.com.sebakoni.recargapay.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

@RestController()
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/wallet/{walletId}/balance")
    public BigDecimal getBalance(@PathVariable String walletId) throws WalletNotFoundException {
        return this.walletService.getBalance(walletId);
    }

    @PostMapping("/wallet")
    public Wallet createWallet(@RequestBody WalletCreation walletCreation) throws UserHasWalletException {
        return this.walletService.createWallet(walletCreation.userId());
    }

    @PostMapping("/wallet/deposit")
    public BigDecimal deposit(@RequestBody Deposit deposit) throws WalletNotFoundException {
        return this.walletService.deposit(deposit.walletId(), deposit.amount());
    }

    @PostMapping("/wallet/withdraw")
    public BigDecimal withdrawal(@RequestBody Withdrawal withdrawal) throws WalletNotFoundException, WalletWithoutSufficientFundsException {
        return this.walletService.withdraw(withdrawal.walletId(), withdrawal.amount());
    }

    @PostMapping("/wallet/transfer")
    public void transfer(@RequestBody Transfer transfer) throws WalletNotFoundException, WalletWithoutSufficientFundsException {
        this.walletService.transfer(transfer.originWalletId(), transfer.destinationWalletId(), transfer.amount());
    }

    @GetMapping("/wallet/{walletId}/balance/{date}")
    public BigDecimal getBalanceAt(@PathVariable String walletId, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm:ss") Date date) throws WalletNotFoundException {
        return this.walletService.getBalanceAt(walletId, date);
    }
}
