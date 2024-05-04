package com.example.timetraderapi.entity;


import jakarta.persistence.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {

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

}
