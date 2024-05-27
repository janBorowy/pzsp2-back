package pl.pzsp2back.orm;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="optimization_processes")
public class OptimizationProcess {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "offer_acceptance_deadline", nullable = false)
    private LocalDateTime offerAcceptanceDeadline;

    @Column(name = "optimization_time")
    private LocalDateTime optimizationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule.id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user.login", nullable = false)
    private User processOwner;

    @OneToMany(mappedBy = "optimizationProcess", fetch = FetchType.LAZY)
    private List<TradeOffer> tradeOffersList;

}