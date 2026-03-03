package vincenzocalvaruso.Heritage_Kitchen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor
@JsonIgnoreProperties({
        "password",
        "authorities",
        "accountNonExpired",
        "accountNonLocked",
        "credentialsNonExpired",
        "enabled",
        "created_at",    // duplicato in snake_case
        "updated_at",    // duplicato in snake_case
        "role",
        "createdAt",
        "updatedAt"
})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(unique = true, nullable = false)
    @Setter
    private String username;

    @Setter
    private String avatar;

    @Setter
    private String bio;

    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime updatedAt;

    public User(String username, String password, String email, String bio, String avatar) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.avatar = (avatar != null) ? avatar : "https://ui-avatars.com/api/?name=" + username;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    //    TODO: Testare funzionamento, in caso contrario modificare l'assegnazione dei ruoli
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    public LocalDateTime getCreated_at() {
        return createdAt;
    }

    public LocalDateTime getUpdated_at() {
        return updatedAt;
    }
}
