package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.exception.UserHasWalletException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;
import ar.com.sebakoni.recargapay.wallet.repository.WalletRepository;
import ar.com.sebakoni.recargapay.wallet.utils.UUIDGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {

    public static final BigDecimal EXPECTED_BALANCE = BigDecimal.TEN;
    public static final String A_WALLET_ID = "a-wallet-id";
    public static final String A_USER_ID = "a-user-id";

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UUIDGenerator uuidGenerator;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void getCurrentBalanceOk() throws WalletNotFoundException {
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.of(new Wallet(A_WALLET_ID, A_USER_ID, EXPECTED_BALANCE)));
        assertEquals(EXPECTED_BALANCE, walletService.getBalance(A_WALLET_ID));
    }

    @Test
    void getCurrentBalanceNotFound() {
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.empty());
        Exception thrown = assertThrows(Exception.class, () -> {
            walletService.getBalance(A_WALLET_ID);
        });

        assertEquals("Wallet with id a-wallet-id not found", thrown.getMessage());
    }

    @Test
    void createWalletOk() throws UserHasWalletException {
        when(uuidGenerator.generate()).thenReturn(A_WALLET_ID);
        Wallet actual = walletService.createWallet(A_USER_ID);

        assertEquals(new Wallet(A_WALLET_ID, A_USER_ID, BigDecimal.ZERO), actual);
    }

    @Test
    void createWalletWhenUserHasWalletError() {
        when(walletRepository.findByUserId(A_USER_ID)).thenReturn(Optional.of(new Wallet(A_WALLET_ID, A_USER_ID, EXPECTED_BALANCE)));

        assertThrows(UserHasWalletException.class, () -> {
            walletService.createWallet(A_USER_ID);
        });
    }
}
