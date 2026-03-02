package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower; // Chi segue

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following; // Chi viene seguito

    public Follow() {
    }

    public UUID getId() {
        return id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }
}