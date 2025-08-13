package ar.com.sebakoni.recargapay.wallet.model;

import java.math.BigDecimal;

public record Deposit(String walletId, BigDecimal amount) {
}
