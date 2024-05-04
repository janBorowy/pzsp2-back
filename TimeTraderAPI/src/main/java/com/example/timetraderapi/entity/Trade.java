package com.example.timetraderapi.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="trades")
public class Trade {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Date timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer.login", nullable = false)
    private User buyer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer.id", nullable = false)
    private TradeOffer tradeOffer;
}
