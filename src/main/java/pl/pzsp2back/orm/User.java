package pl.pzsp2back.orm;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User implements UserDetails{
    @Id
    private String login;
    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private Boolean ifAdmin;

    @Column(nullable = false)
    private Integer balance;

    private String email;

    private String name;

    private String surname;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group.id", nullable = false)
    private Group group;


    @OneToMany(mappedBy = "offerOwner", fetch = FetchType.LAZY)
    private List<TradeOffer> listTradeOffers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "timeslot_user",
            joinColumns = {
                    @JoinColumn(name="user_login", referencedColumnName = "login")
            },
            inverseJoinColumns = {
                    @JoinColumn(name="timeslot_id", referencedColumnName = "id")
            })
    private List<TimeSlot> timeSlots;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static boolean ifUsersHaveSameGroup(List<User> users){
        Group group = users.get(0).getGroup();
        for (User user : users) {
            if (user.getGroup() != group) {
                return false;
            }
        }
        return true;
    }

}