package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor
public class User {
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
    private String role;

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


//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//
//    public void setUsername(String username) {
//        this.username = username;
//    }


    public LocalDateTime getCreated_at() {
        return createdAt;
    }

    public LocalDateTime getUpdated_at() {
        return updatedAt;
    }
}
