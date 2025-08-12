package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.entities.WalletTransaction;
import ar.com.sebakoni.recargapay.wallet.exception.UserHasWalletException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;
import ar.com.sebakoni.recargapay.wallet.repository.WalletRepository;
import ar.com.sebakoni.recargapay.wallet.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UUIDGenerator uuidGenerator;

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

        Wallet newWallet = new Wallet(uuidGenerator.generate(), userId, BigDecimal.ZERO);
        walletRepository.save(newWallet);
        return newWallet;
    }

    @Override
    public BigDecimal deposit(String walletId, BigDecimal amount) throws WalletNotFoundException {
        Optional<Wallet> optWallet = this.walletRepository.findById(walletId);

        if (optWallet.isEmpty()) {
            throw new WalletNotFoundException(walletId);
        }

        Wallet wallet = optWallet.get();
        BigDecimal newBalance = wallet.balance.add(amount);

        WalletTransaction transaction = new WalletTransaction();
        transaction.id = this.uuidGenerator.generate();
        transaction.wallet = wallet;
        transaction.amount = amount;
        transaction.newBalance = newBalance;
        wallet.walletTransactions.add(transaction);

        this.walletRepository.save(wallet);

        return newBalance;
    }
}
