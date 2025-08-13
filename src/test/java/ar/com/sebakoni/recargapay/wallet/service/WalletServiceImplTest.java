package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.entities.WalletTransaction;
import ar.com.sebakoni.recargapay.wallet.exception.UserHasWalletException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletWithoutSufficientFundsException;
import ar.com.sebakoni.recargapay.wallet.repository.WalletRepository;
import ar.com.sebakoni.recargapay.wallet.repository.WalletTransactionRepository;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {
    public static final String A_WALLET_ID = "a-wallet-id";
    public static final String ANOTHER_WALLET_ID = "another-wallet-id";

    public static final String A_USER_ID = "a-user-id";
    public static final String ANOTHER_USER_ID = "another-user-id";

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
        returnedWallet.balance = BigDecimal.ONE;
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.of(returnedWallet));
        assertEquals(BigDecimal.ONE, walletService.getBalance(A_WALLET_ID));
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
        returnedWallet.balance = BigDecimal.ONE;
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
        returnedWallet.balance = BigDecimal.ZERO;
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.of(returnedWallet));

        BigDecimal newAmount = walletService.deposit(A_WALLET_ID, BigDecimal.ONE);

        assertEquals(BigDecimal.ONE, newAmount);
        returnedWallet.balance = BigDecimal.ONE;
        verify(this.walletRepository, times(1)).save(returnedWallet);
        verify(this.walletTransactionRepository, times(1)).save(any(WalletTransaction.class));
    }

    @Test
    void depositIntoUnexistentWalletError() {
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class, () -> {
            walletService.deposit(A_WALLET_ID, BigDecimal.ONE);
        });
    }

    @Test
    void withdrawFromAccountOk() throws WalletNotFoundException, WalletWithoutSufficientFundsException {
        Wallet returnedWallet = new Wallet();
        returnedWallet.id = A_WALLET_ID;
        returnedWallet.userId = A_USER_ID;
        returnedWallet.balance = BigDecimal.ONE;
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.of(returnedWallet));

        BigDecimal newAmount = walletService.withdraw(A_WALLET_ID, BigDecimal.ONE);

        assertEquals(BigDecimal.ZERO, newAmount);
        returnedWallet.balance = BigDecimal.ZERO;
        verify(this.walletRepository, times(1)).save(returnedWallet);
        verify(this.walletTransactionRepository, times(1)).save(any(WalletTransaction.class));
    }

    @Test
    void withdrawFromUnexistentWalletError() {
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class, () -> {
            walletService.withdraw(A_WALLET_ID, BigDecimal.ONE);
        });
    }

    @Test
    void withdrawFromAccountWithoutFundsError() {
        Wallet returnedWallet = new Wallet();
        returnedWallet.id = A_WALLET_ID;
        returnedWallet.userId = A_USER_ID;
        returnedWallet.balance = BigDecimal.ZERO;
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.of(returnedWallet));

        assertThrows(WalletWithoutSufficientFundsException.class, () -> {
            walletService.withdraw(A_WALLET_ID, BigDecimal.ONE);
        });
    }

    @Test
    void transferBetweenAccountsOk() throws WalletNotFoundException, WalletWithoutSufficientFundsException {
        Wallet originWallet = new Wallet();
        originWallet.id = A_WALLET_ID;
        originWallet.userId = A_USER_ID;
        originWallet.balance = BigDecimal.ONE;

        Wallet destiationWallet = new Wallet();
        destiationWallet.id = ANOTHER_WALLET_ID;
        destiationWallet.userId = ANOTHER_USER_ID;
        destiationWallet.balance = BigDecimal.ZERO;

        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.of(originWallet));
        when(walletRepository.findById(ANOTHER_WALLET_ID)).thenReturn(Optional.of(destiationWallet));

        walletService.transfer(A_WALLET_ID, ANOTHER_WALLET_ID, BigDecimal.ONE);

        originWallet.balance = BigDecimal.ZERO;
        destiationWallet.balance = BigDecimal.ONE;

        verify(this.walletRepository, times(1)).save(originWallet);
        verify(this.walletRepository, times(1)).save(destiationWallet);
        verify(this.walletTransactionRepository, times(2)).save(any(WalletTransaction.class));
    }

    @Test
    void getBalanceAtOk() throws WalletNotFoundException {
        Wallet wallet = new Wallet();
        wallet.id = A_WALLET_ID;
        wallet.userId = A_USER_ID;
        wallet.balance = BigDecimal.TWO;

        WalletTransaction t1 = new WalletTransaction();
        t1.id = "transaction-id-1";
        t1.type = WalletTransaction.Type.DEPOSIT;
        t1.wallet = wallet;
        t1.amount = BigDecimal.ONE;
        t1.newBalance = BigDecimal.ONE;
        t1.createdAt = DateUtil.yesterday();
        wallet.walletTransactions.add(t1);

        WalletTransaction t2 = new WalletTransaction();
        t2.id = "transaction-id-1";
        t2.type = WalletTransaction.Type.DEPOSIT;
        t2.wallet = wallet;
        t2.amount = BigDecimal.ONE;
        t2.newBalance = BigDecimal.TWO;
        t2.createdAt = DateUtil.tomorrow();
        wallet.walletTransactions.add(t2);

        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.of(wallet));

        BigDecimal previousBalance = walletService.getBalanceAt(A_WALLET_ID, DateUtil.now());

        assertEquals(BigDecimal.ONE, previousBalance);
    }

    @Test
    void getBalanceAtNotFound() {
        when(walletRepository.findById(A_WALLET_ID)).thenReturn(Optional.empty());
        Exception thrown = assertThrows(Exception.class, () -> {
            walletService.getBalanceAt(A_WALLET_ID, DateUtil.now());
        });

        assertEquals("Wallet with id a-wallet-id not found", thrown.getMessage());
    }
}
