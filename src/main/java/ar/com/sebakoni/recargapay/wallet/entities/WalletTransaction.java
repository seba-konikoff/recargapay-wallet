package ar.com.sebakoni.recargapay.wallet.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "wallet_transactions")
public class WalletTransaction {

    @Id
    public String id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    public Wallet wallet;

    public BigDecimal amount;
    public BigDecimal newBalance;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    public Date createdAt;
}
