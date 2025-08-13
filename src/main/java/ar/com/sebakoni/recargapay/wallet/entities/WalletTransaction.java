package ar.com.sebakoni.recargapay.wallet.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "wallet_transactions")
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    public Wallet wallet;

    public BigDecimal amount;
    public BigDecimal newBalance;
    public Type type;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    public Date createdAt;

    public enum Type {
        DEPOSIT, WITHDRAWAL
    }
}
