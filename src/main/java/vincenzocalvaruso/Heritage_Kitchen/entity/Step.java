package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "steps")
public class Step {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "ordine_passaggio", nullable = false)
    private int ordine; // es. 1, 2, 3... per ordinare la preparazione

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descrizione; // Il testo del passaggio

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe; // La ricetta a cui appartiene questo step

    public Step() {
    }

    // GETTER E SETTER
    public UUID getId() {
        return id;
    }

    public int getOrdine() {
        return ordine;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}