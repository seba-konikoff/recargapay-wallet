package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;

import java.math.BigDecimal;

public interface WalletService {
    BigDecimal getBalance(String id) throws WalletNotFoundException;
}
