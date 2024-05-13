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

    @Column(name = "can_offer")
    private Boolean canOffer;

    private Integer state;

    private LocalDateTime timestamp;

    @OneToOne(mappedBy = "tradeOffer", fetch = FetchType.LAZY)
    private Trade trade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller.login")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot.id")
    private TimeSlot timeslot;

}
