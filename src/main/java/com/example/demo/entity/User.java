package com.example.demo.entity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    private String firstName;

    @Setter
    private String lastName;

    @Setter
    private String password;

    @Setter
    private String email;

    @Setter
    private String phone;

    @Lob
    @Setter
    private String photo;

    @Setter
    private String note;

    @Transient
    @Setter
    private Double driverRating;

    @Transient
    @Setter
    private Double passengerRating;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    @Setter
    @ToString.Exclude
    private Code code;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Trip> trips;

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Reservation> reservations;

    public User(String firstName, String lastName, String password, String email,
                String phone, String photo, String note) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
        this.note = note;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
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
}
