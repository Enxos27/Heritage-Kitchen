package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "follows")
@Getter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower; // Chi segue

    @Setter
    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following; // Chi viene seguito

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp // Imposta la data al momento dell'insert
    private LocalDateTime createdAt;
}