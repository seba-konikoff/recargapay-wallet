package ar.com.sebakoni.recargapay.wallet.repository;

import ar.com.sebakoni.recargapay.wallet.entities.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, String> {
    Optional<Wallet> findByUserId(String userId);
}
