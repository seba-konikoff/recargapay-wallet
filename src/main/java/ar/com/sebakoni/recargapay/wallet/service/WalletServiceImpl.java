package ar.com.sebakoni.recargapay.wallet.service;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import ar.com.sebakoni.recargapay.wallet.exception.WalletNotFoundException;
import ar.com.sebakoni.recargapay.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public BigDecimal getBalance(String id) throws WalletNotFoundException {
        Optional<Wallet> wallet = walletRepository.findById(id);

        if (wallet.isEmpty()) {
            throw new WalletNotFoundException(id);
        }

        return wallet.get().balance;
    }
}
