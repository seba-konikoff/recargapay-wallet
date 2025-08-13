package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.exception.UserHasWalletException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletWithoutSufficientFundsException;

import java.math.BigDecimal;
import java.util.Date;

public interface WalletService {
    BigDecimal getBalance(String walletId) throws WalletNotFoundException;
    Wallet createWallet(String userId) throws UserHasWalletException;
    BigDecimal deposit(String walletId, BigDecimal amount) throws WalletNotFoundException;
    BigDecimal withdraw(String walletId, BigDecimal amount) throws WalletNotFoundException, WalletWithoutSufficientFundsException;
    void transfer(String originWalletId, String destinationWalletId, BigDecimal amount) throws WalletNotFoundException, WalletWithoutSufficientFundsException;
    BigDecimal getBalanceAt(String walletId, Date date) throws WalletNotFoundException;
}
