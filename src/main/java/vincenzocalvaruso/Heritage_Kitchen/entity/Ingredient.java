package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "ingredienti")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome; // es. "Farina 00"

    private String quantita; // es. "500g" o "2 cucchiai"

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe; // La ricetta a cui appartiene questo ingrediente

    public Ingredient() {
    }

    // GETTER E SETTER
    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQuantita() {
        return quantita;
    }

    public void setQuantita(String quantita) {
        this.quantita = quantita;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}