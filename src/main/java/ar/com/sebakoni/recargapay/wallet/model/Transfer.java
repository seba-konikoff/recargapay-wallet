package ar.com.sebakoni.recargapay.wallet.model;

import java.math.BigDecimal;

public record Transfer(String originWalletId, String destinationWalletId, BigDecimal amount) {
}
