package com.kamilglazer.gosport.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.kamilglazer.gosport.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique = true,nullable = false)
    private String email;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private USER_ROLE role = USER_ROLE.USER;

    @OneToOne(cascade = CascadeType.ALL)
    private UserCredentials credentials;


    @OneToMany(mappedBy = "sender",cascade = CascadeType.ALL)
    private List<Connection> sentConnections;

    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL)
    private List<Connection> receivedConnections;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword(){
        return password;
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
