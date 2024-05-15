package pl.pzsp2back.orm;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "timeslots")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private Integer baseSlotQuantity;

    @Column(nullable = false)
    private Integer lastMarketPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user.login")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule.id")
    private Schedule schedule;

    @OneToMany(mappedBy = "timeslot", fetch = FetchType.LAZY)
    private List<TradeOffer> tradeOfferList = new ArrayList<>();
}
