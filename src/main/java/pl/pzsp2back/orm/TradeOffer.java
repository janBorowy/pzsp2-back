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

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer price;

    private LocalDateTime timestamp;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offerOwner.login")
    private User offerOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot.id")
    private TimeSlot timeslot;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "optimizationProcess.id")
    private OptimizationProcess optimizationProcess;

    @Column(name = "if_want_offer")
    private Boolean ifWantOffer;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private Boolean isActive;





}
