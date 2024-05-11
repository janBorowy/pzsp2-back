package pl.pzsp2back.orm;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {

    @Id
    private String login;

    private String password;

    @Setter
    private boolean isAdmin;

    @Setter
    private String email;

    @Setter
    private String name;

    @Setter
    private String surname;

    public User() {}
}
