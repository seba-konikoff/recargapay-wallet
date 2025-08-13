package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.entities.WalletTransaction;
import ar.com.sebakoni.recargapay.wallet.exception.UserHasWalletException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletWithoutSufficientFundsException;
import ar.com.sebakoni.recargapay.wallet.repository.WalletRepository;
import ar.com.sebakoni.recargapay.wallet.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Override
    public BigDecimal getBalance(String walletId) throws WalletNotFoundException {
        Optional<Wallet> wallet = walletRepository.findById(walletId);

        if (wallet.isEmpty()) {
            throw new WalletNotFoundException(walletId);
        }

        return wallet.get().balance;
    }

    @Override
    public Wallet createWallet(String userId) throws UserHasWalletException {
        if (walletRepository.findByUserId(userId).isPresent()) {
            throw new UserHasWalletException(userId);
        }

        Wallet newWallet = new Wallet();
        newWallet.userId = userId;

        return walletRepository.save(newWallet);
    }

    @Override
    @Transactional(rollbackFor = { SQLException.class })
    public BigDecimal deposit(String walletId, BigDecimal amount) throws WalletNotFoundException {
        Optional<Wallet> optWallet = this.walletRepository.findById(walletId);

        if (optWallet.isEmpty()) {
            throw new WalletNotFoundException(walletId);
        }

        Wallet wallet = optWallet.get();
        BigDecimal newBalance = wallet.balance.add(amount);

        WalletTransaction transaction = new WalletTransaction();
        transaction.wallet = wallet;
        transaction.amount = amount;
        transaction.newBalance = newBalance;
        transaction.type = WalletTransaction.Type.DEPOSIT;
        wallet.balance = newBalance;
        wallet.walletTransactions.add(transaction);

        this.walletTransactionRepository.save(transaction);
        this.walletRepository.save(wallet);

        return newBalance;
    }

    @Override
    @Transactional(rollbackFor = { SQLException.class })
    public BigDecimal withdraw(String walletId, BigDecimal amount) throws WalletNotFoundException, WalletWithoutSufficientFundsException {
        Optional<Wallet> optWallet = this.walletRepository.findById(walletId);

        if (optWallet.isEmpty()) {
            throw new WalletNotFoundException(walletId);
        }

        Wallet wallet = optWallet.get();
        if (wallet.balance.compareTo(amount) < 0) {
            throw new WalletWithoutSufficientFundsException(walletId);
        }

        BigDecimal newBalance = wallet.balance.subtract(amount);

        WalletTransaction transaction = new WalletTransaction();
        transaction.wallet = wallet;
        transaction.amount = amount;
        transaction.newBalance = newBalance;
        transaction.type = WalletTransaction.Type.WITHDRAWAL;
        wallet.balance = newBalance;
        wallet.walletTransactions.add(transaction);

        this.walletTransactionRepository.save(transaction);
        this.walletRepository.save(wallet);

        return newBalance;
    }

    @Override
    @Transactional(rollbackFor = { SQLException.class })
    public void transfer(String originWalletId, String destinationWalletId, BigDecimal amount) throws WalletNotFoundException, WalletWithoutSufficientFundsException {
        this.withdraw(originWalletId, amount);
        this.deposit(destinationWalletId, amount);
    }

    @Override
    public BigDecimal getBalanceAt(String walletId, Date targetDate) throws WalletNotFoundException {
        Optional<Wallet> optWallet = this.walletRepository.findById(walletId);

        if (optWallet.isEmpty()) {
            throw new WalletNotFoundException(walletId);
        }

        Wallet wallet = optWallet.get();

        if (wallet.walletTransactions.isEmpty()) {
            return BigDecimal.ZERO; // wallets are created with an empty balance
        }

        Optional<WalletTransaction> transaction = wallet.walletTransactions.reversed().stream().filter(t -> t.createdAt.before(targetDate)).findFirst();
        if (transaction.isEmpty()) {
            return BigDecimal.ZERO; // there are no transactions before the given date
        }

        return transaction.get().newBalance;
    }
}
