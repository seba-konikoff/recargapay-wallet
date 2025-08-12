package ar.com.sebakoni.recargapay.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WalletNotFoundException extends Exception {
    public WalletNotFoundException(String id) {
        super(String.format("Wallet with id %s not found", id));
    }
}
