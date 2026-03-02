package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Ricette")
public class Recipe {
    @Id
    @GeneratedValue
    @Column(name = "recipe_id")
    private UUID id;
    @Column(nullable = false)
    private String titolo;
    private String descrizione;
    private int tempo_prep;
    private int tempo_cottura;
    @Enumerated(EnumType.STRING)
    private Difficolta difficolta;
    private boolean is_Original; //usato per migliorare e facilitare query
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
    private LocalDate updated_at;

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
        return tempo_prep;
    }

    public void setTempo_prep(int tempo_prep) {
        this.tempo_prep = tempo_prep;
    }

    public int getTempo_cottura() {
        return tempo_cottura;
    }

    public void setTempo_cottura(int tempo_cottura) {
        this.tempo_cottura = tempo_cottura;
    }

    public Difficolta getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    public boolean isIs_original() {
        return is_Original;
    }

    public void setIs_original(boolean is_Original) {
        this.is_Original = is_Original;
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

    public LocalDate getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDate updated_at) {
        this.updated_at = updated_at;
    }
}

