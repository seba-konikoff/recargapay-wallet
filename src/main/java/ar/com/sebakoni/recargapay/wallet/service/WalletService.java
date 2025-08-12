package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.exception.UserHasWalletException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;

import java.math.BigDecimal;

public interface WalletService {
    BigDecimal getBalance(String walletId) throws WalletNotFoundException;
    Wallet createWallet(String userId) throws UserHasWalletException;
    BigDecimal deposit(String walletId, BigDecimal amount) throws WalletNotFoundException;
}
