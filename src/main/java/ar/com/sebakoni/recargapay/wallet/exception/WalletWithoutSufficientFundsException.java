package ar.com.sebakoni.recargapay.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WalletWithoutSufficientFundsException extends Exception {
    public WalletWithoutSufficientFundsException(String walletId) {
        super(String.format("Wallet with id %s does not have sufficient funds for the transaction", walletId));
    }
}
