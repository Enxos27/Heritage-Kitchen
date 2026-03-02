package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Column(unique = true, nullable = false)
    private String username;
    private String avatar;
    private String bio;
    private String role;
    private LocalDate created_at;
    private LocalDate updated_at;

    public User() {
    }

    public User(String username, String password, String email, String bio, String avatar) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.avatar = (avatar != null) ? avatar : "https://ui-avatars.com/api/?name=" + username;
        this.created_at = LocalDate.now();
        this.updated_at = LocalDate.now();
    }

    // GETTER e SETTER
    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public LocalDate getUpdated_at() {
        return updated_at;
    }
}
