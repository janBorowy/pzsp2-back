package pl.pzsp2back.orm;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @JoinColumn(name = "schedule.id")
    private Schedule schedule;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "timeslot_user",
            joinColumns = {
                    @JoinColumn(name="timeslot_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name="user_login", referencedColumnName = "login")
            })
    private List<User> users;


    @OneToMany(mappedBy = "timeslot", fetch = FetchType.LAZY)
    private List<TradeOffer> tradeOfferList = new ArrayList<>();

}
