package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.exception.UserHasWalletException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;
import ar.com.sebakoni.recargapay.wallet.repository.WalletRepository;
import ar.com.sebakoni.recargapay.wallet.repository.WalletTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {

    public static final BigDecimal EXPECTED_BALANCE = BigDecimal.ONE;
    public static final BigDecimal NEW_AMOUNT = BigDecimal.ONE;
    public static final BigDecimal NEW_BALANCE = BigDecimal.TWO;

    public static final String A_WALLET_ID = "a-wallet-id";
    public static final String A_USER_ID = "a-user-id";

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void getCurrentBalanceOk() throws WalletNotFoundException {
        Wallet returnedWallet = new Wallet();
        returnedWallet.id = A_WALLET_ID;
        returnedWallet.userId = A_USER_ID;
        returnedWallet.balance = EXPECTED_BALANCE;
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.of(returnedWallet));
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
        Wallet expectedWallet = new Wallet();
        expectedWallet.id = A_WALLET_ID;
        expectedWallet.userId = A_USER_ID;
        when(walletRepository.save(any(Wallet.class))).thenReturn(expectedWallet);

        Wallet actual = walletService.createWallet(A_USER_ID);

        assertEquals(expectedWallet, actual);
    }

    @Test
    void createWalletWhenUserHasWalletError() {
        Wallet returnedWallet = new Wallet();
        returnedWallet.id = A_WALLET_ID;
        returnedWallet.userId = A_USER_ID;
        returnedWallet.balance = EXPECTED_BALANCE;
        when(walletRepository.findByUserId(A_USER_ID)).thenReturn(Optional.of(returnedWallet));

        assertThrows(UserHasWalletException.class, () -> {
            walletService.createWallet(A_USER_ID);
        });
    }

    @Test
    void depositIntoWalletOk() throws WalletNotFoundException {
        Wallet returnedWallet = new Wallet();
        returnedWallet.id = A_WALLET_ID;
        returnedWallet.userId = A_USER_ID;
        returnedWallet.balance = EXPECTED_BALANCE;
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.of(returnedWallet));

        BigDecimal newAmount = walletService.deposit(A_WALLET_ID, NEW_AMOUNT);

        assertEquals(NEW_BALANCE, newAmount);
        verify(this.walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void depositIntoUnexistentWalletError() {
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class, () -> {
            walletService.deposit(A_WALLET_ID, NEW_AMOUNT);
        });
    }
}
