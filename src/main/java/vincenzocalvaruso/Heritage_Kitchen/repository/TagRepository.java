package vincenzocalvaruso.Heritage_Kitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vincenzocalvaruso.Heritage_Kitchen.entity.Tag;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByNome(String nome);
}