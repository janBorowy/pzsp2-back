package pl.pzsp2back.orm;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Column()
    private Integer finalPrice;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "optimizationProcesses.id", nullable = false)
    private OptimizationProcess optimizationProcess;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerOffer.id", nullable = false)
    private TradeOffer sellerOffer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyerOffer.id", nullable = false)
    private TradeOffer buyerOffer;



}
