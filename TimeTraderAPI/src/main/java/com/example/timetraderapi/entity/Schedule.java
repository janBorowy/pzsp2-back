package com.example.timetraderapi.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group.id")
    private Group group;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<TimeSlot> timeSlotList = new ArrayList<>();

}
