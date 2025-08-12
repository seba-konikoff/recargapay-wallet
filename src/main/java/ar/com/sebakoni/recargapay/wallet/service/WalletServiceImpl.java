package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.exception.UserHasWalletException;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;
import ar.com.sebakoni.recargapay.wallet.repository.WalletRepository;
import ar.com.sebakoni.recargapay.wallet.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Override
    public BigDecimal getBalance(String id) throws WalletNotFoundException {
        Optional<Wallet> wallet = walletRepository.findById(id);

        if (wallet.isEmpty()) {
            throw new WalletNotFoundException(id);
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
}
