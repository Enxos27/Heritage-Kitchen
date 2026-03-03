package vincenzocalvaruso.Heritage_Kitchen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "steps")
@NoArgsConstructor
@Getter
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private UUID id;

    @Setter
    @Column(name = "ordine_passaggio", nullable = false)
    private int ordine; // es. 1, 2, 3... per ordinare la preparazione

    @Setter
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descrizione; // Il testo del passaggio

    @Setter
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnore
    private Recipe recipe; // La ricetta a cui appartiene questo step

}