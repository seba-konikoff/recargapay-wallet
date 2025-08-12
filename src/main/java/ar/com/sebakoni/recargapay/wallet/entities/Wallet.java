package ar.com.sebakoni.recargapay.wallet.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity(name = "wallets")
public class Wallet {

    @Id
    public String id;
    public BigDecimal balance;

    public Wallet(String id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }
}
