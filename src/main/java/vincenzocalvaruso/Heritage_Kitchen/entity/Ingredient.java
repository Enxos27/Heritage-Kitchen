package vincenzocalvaruso.Heritage_Kitchen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "ingredienti")
@Getter
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String nome; // es. "Farina 00"

    @Setter
    private String quantita; // es. "500g" o "2 cucchiai"

    @Setter
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe; // La ricetta a cui appartiene questo ingrediente

}