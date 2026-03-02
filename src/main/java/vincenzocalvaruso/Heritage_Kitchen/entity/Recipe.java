package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Ricette")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "recipe_id")
    private UUID id;
    @Column(nullable = false)
    private String titolo;
    private String descrizione;
    private int tempoPrep;
    private int tempoCottura;
    @Enumerated(EnumType.STRING)
    private Difficolta difficolta;
    private boolean isOriginale; //usato per migliorare e facilitare query
    @ManyToOne
    @JoinColumn(name = "parent_recipe_id")
    private Recipe parentRecipe; // Punta al "padre" diretto
    @ManyToOne
    @JoinColumn(name = "root_recipe_id")
    private Recipe rootRecipe;
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

    public Recipe() {
    }

    // Getter e Setter
    public UUID getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getTempo_prep() {
        return tempoPrep;
    }

    public void setTempo_prep(int tempo_prep) {
        this.tempoCottura = tempo_prep;
    }

    public int getTempo_cottura() {
        return tempoCottura;
    }

    public void setTempo_cottura(int tempo_cottura) {
        this.tempoCottura = tempo_cottura;
    }

    public Difficolta getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    public boolean isIs_original() {
        return isOriginale;
    }

    public void setIs_original(boolean isOriginale) {
        this.isOriginale = isOriginale;
    }

    public Recipe getParentRecipe() {
        return parentRecipe;
    }

    public void setParentRecipe(Recipe parentRecipe) {
        this.parentRecipe = parentRecipe;
    }

    public Recipe getRootRecipe() {
        return rootRecipe;
    }

    public void setRootRecipe(Recipe rootRecipe) {
        this.rootRecipe = rootRecipe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getUpdated_at() {
        return updatedAt;
    }

    public void setUpdated_at(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

