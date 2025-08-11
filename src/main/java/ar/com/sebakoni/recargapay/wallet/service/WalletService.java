package ar.com.sebakoni.recargapay.wallet.service;

import java.math.BigDecimal;

public interface WalletService {
    BigDecimal getBalance(String id);
}
