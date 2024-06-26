package pl.pzsp2back.orm;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="trade_offers")
public class TradeOffer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer price;

    private LocalDateTime timestamp;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offerOwner.login")
    private User offerOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot.id")
    private TimeSlot timeslot;


    @Column(name = "if_sell_offer")
    private Boolean ifSellOffer;

    private Boolean isActive;





}
