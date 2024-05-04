package com.example.timetraderapi.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean ifAdmin;

    @Column(unique = true)
    private String email;

    private String name;

    private String surname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group.id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<Trade> listTrades = new ArrayList<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<TradeOffer> listTradeOffers = new ArrayList<>();

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

}
