package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Ricette")
@NoArgsConstructor
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

    private int tempoPrep;

    private int tempoCottura;

    @Setter
    @Enumerated(EnumType.STRING)
    private Difficolta difficolta;

    private boolean isOriginale; //usato per migliorare e facilitare query

    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_recipe_id")
    private Recipe parentRecipe; // Punta al "padre" diretto

    @Setter
    @ManyToOne
    @JoinColumn(name = "root_recipe_id")
    private Recipe rootRecipe;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // L'autore di questa specifica versione

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredienti;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Step> steps;
    //    Uso cascade = CascadeType.ALL così quando salverò una Recipe,
    //    Spring salverà automaticamente anche tutti gli ingredienti e i passaggi
    //    inseriti nella lista.

    @ManyToMany
    @JoinTable(
            name = "recipe_tags",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    private LocalDateTime updatedAt;

    // Getter e Setter

    public void setTempo_prep(int tempo_prep) {
        this.tempoCottura = tempo_prep;
    }

    public void setTempo_cottura(int tempo_cottura) {
        this.tempoCottura = tempo_cottura;
    }


    public void setIs_original(boolean isOriginale) {
        this.isOriginale = isOriginale;
    }


    public void setUpdated_at(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

