package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;
import ar.com.sebakoni.recargapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {

    public static final BigDecimal EXPECTED_BALANCE = new BigDecimal("10.1");
    public static final String AN_ID = "an-id";

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void getCurrentBalanceOk() throws WalletNotFoundException {
        when(walletRepository.findById(AN_ID)).thenReturn(Optional.of(new Wallet(AN_ID, EXPECTED_BALANCE)));
        assertEquals(EXPECTED_BALANCE, walletService.getBalance(AN_ID));
    }

    @Test
    void getCurrentBalanceNotFound() {
        when(walletRepository.findById(AN_ID)).thenReturn(Optional.empty());
        Exception thrown = assertThrows(Exception.class, () -> {
            walletService.getBalance(AN_ID);
        });

        assertEquals("Wallet with id an-id not found", thrown.getMessage());
    }
}
