package ar.com.sebakoni.recargapay.wallet.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    public String userId;
    public BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "wallet")
    public List<WalletTransaction> walletTransactions = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id) && Objects.equals(userId, wallet.userId) && Objects.equals(balance, wallet.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, balance);
    }
}
