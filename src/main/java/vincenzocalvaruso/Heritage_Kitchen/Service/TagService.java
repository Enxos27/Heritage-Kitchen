package vincenzocalvaruso.Heritage_Kitchen.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vincenzocalvaruso.Heritage_Kitchen.entity.Tag;
import vincenzocalvaruso.Heritage_Kitchen.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag saveTag(String nome) {
        // Evito duplicati: se esiste già, lo restituisco, altrimenti lo creo
        return tagRepository.findByNome(nome)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setNome(nome);
                    return tagRepository.save(newTag);
                });
    }

    public Optional<Tag> findByNome(String nome) {
        return tagRepository.findByNome(nome);
    }

}