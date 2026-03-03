package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Ricette")
@NoArgsConstructor
@Getter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "recipe_id")
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String titolo;

    @Setter
    private String descrizione;

    @Setter
    private int tempoPrep;

    @Setter
    private int tempoCottura;

    @Setter
    @Enumerated(EnumType.STRING)
    private Difficolta difficolta;

    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_recipe_id")
    private Recipe parentRecipe; // Punta al "padre" diretto

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // L'autore di questa specifica versione

    @Setter
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredienti;

    @Setter
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Step> steps;
    //    Uso cascade = CascadeType.ALL così quando salverò una Recipe,
    //    Spring salverà automaticamente anche tutti gli ingredienti e i passaggi
    //    inseriti nella lista.

    @Setter
    @ManyToMany
    @JoinTable(
            name = "recipe_tags",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Setter
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Setter
    private String imageURL;
}

