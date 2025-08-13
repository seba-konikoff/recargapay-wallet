package ar.com.sebakoni.recargapay.wallet.repository;

import ar.com.sebakoni.recargapay.wallet.entities.WalletTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends CrudRepository<WalletTransaction, String>  {
}
