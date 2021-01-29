package br.com.application.wallet.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "WALLET")
public class Wallet implements Serializable {
    private static final long serialVersionUID = -7080509259541108742L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal balance;

    @OneToMany
    @JoinColumn(name = "wallet_id")
    List<Expense> expenses;

    public Wallet(String description, BigDecimal balance) {
        this.description = description;
        this.balance = balance;
    }
}
