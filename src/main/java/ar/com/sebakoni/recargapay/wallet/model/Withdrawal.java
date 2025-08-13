package ar.com.sebakoni.recargapay.wallet.model;

import java.math.BigDecimal;

public record Withdrawal(String walletId, BigDecimal amount) {
}
