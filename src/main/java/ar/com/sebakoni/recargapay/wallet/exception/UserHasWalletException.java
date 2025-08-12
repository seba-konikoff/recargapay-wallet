package ar.com.sebakoni.recargapay.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserHasWalletException extends Exception {

    public UserHasWalletException(String userId) {
        super(String.format("A wallet for the user with id %s already exists", userId));
    }
}
