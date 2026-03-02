package vincenzocalvaruso.Heritage_Kitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vincenzocalvaruso.Heritage_Kitchen.entity.Recipe;
import vincenzocalvaruso.Heritage_Kitchen.entity.Step;

import java.util.List;
import java.util.UUID;

@Repository
public interface StepRepository extends JpaRepository<Step, UUID> {
    //Recuperare gli step ordinati per la preparazione
    List<Step> findByRecipeOrderByOrdineAsc(Recipe recipe);
}