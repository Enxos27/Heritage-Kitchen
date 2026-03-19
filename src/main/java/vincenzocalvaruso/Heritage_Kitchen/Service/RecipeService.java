package vincenzocalvaruso.Heritage_Kitchen.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vincenzocalvaruso.Heritage_Kitchen.entity.*;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.NotEmptyException;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.NotFoundException;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.UnauthorizedException;
import vincenzocalvaruso.Heritage_Kitchen.payloads.IngredientDTO;
import vincenzocalvaruso.Heritage_Kitchen.payloads.RecipeRequestDTO;
import vincenzocalvaruso.Heritage_Kitchen.payloads.RecipeResponseDTO;
import vincenzocalvaruso.Heritage_Kitchen.payloads.StepDTO;
import vincenzocalvaruso.Heritage_Kitchen.repository.FollowRepository;
import vincenzocalvaruso.Heritage_Kitchen.repository.LikeRepository;
import vincenzocalvaruso.Heritage_Kitchen.repository.RecipeRepository;
import vincenzocalvaruso.Heritage_Kitchen.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinaryUploader;
    @Autowired
    private TagService tagService;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private FollowRepository followRepository;

    //     CREA RICETTA
    public Recipe createRecipe(RecipeRequestDTO dto, User autore) {
        Recipe recipe = new Recipe();
        recipe.setTitolo(dto.titolo());
        recipe.setDescrizione(dto.descrizione());
        recipe.setTempoPrep(dto.tempoPrep());
        recipe.setTempoCottura(dto.tempoCottura());
        recipe.setDifficolta(Difficolta.valueOf(dto.difficolta()));
        recipe.setUser(autore);

        // 1. Gestione Versioning
        if (dto.parentRecipeId() != null) {
            Recipe parent = repository.findById(dto.parentRecipeId())
                    .orElseThrow(() -> new RuntimeException("Ricetta padre non trovata"));
            recipe.setParentRecipe(parent);
        }

        // 2. Trasformazione Ingredienti
        List<Ingredient> ingredientiEntities = new ArrayList<>();
        for (IngredientDTO iDto : dto.ingredienti()) {
            Ingredient ing = new Ingredient();
            ing.setNome(iDto.nome());
            ing.setQuantita(iDto.quantita());
            ing.setRecipe(recipe); // Colleghiamo l'ingrediente alla ricetta
            ingredientiEntities.add(ing);
        }
        recipe.setIngredienti(ingredientiEntities);

        // 3. Trasformazione Step di preparazione
        List<Step> stepEntities = new ArrayList<>();
        for (StepDTO stepDTO : dto.steps()) {
            Step step = new Step();
            step.setOrdine(stepDTO.ordine());
            step.setDescrizione(stepDTO.descrizione());
            step.setRecipe(recipe);
            stepEntities.add(step);
        }
        recipe.setSteps(stepEntities);

        // 4. Gestione Tag
        // Il controllo per i dublicati si trova all'interno di saveTag()
        if (dto.tags() != null) {
            List<Tag> tags = new ArrayList<>();
            for (String tagName : dto.tags()) {
                tags.add(tagService.saveTag(tagName));
            }
            recipe.setTags(tags);
        }

        // 5. Salvataggio finale
        return repository.save(recipe);
    }

    //  TROVA TUTTE LE RICETTE
    public Page<Recipe> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // TROVA RICETTA PER ID RICETTA
    public Recipe getRecipeById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ricetta non trovata"));
    }

    // TROVA TUTTE RICETTE ORIGINALI
    public List<Recipe> getOriginalRecipes() {
        return repository.findByParentRecipeIsNull();
    }

    // TROVA RICETTE "FORK" TRAMITE ID RICETTA ORIGINALE
    public List<Recipe> findByParentRecipe(Recipe parent) {
        return repository.findByParentRecipe(parent);
    }

    // TROVA RICETTE IN BASE ALL'USER
    public List<Recipe> findByUser(User user) {
        return repository.findByUser(user);
    }

    // TROVA RICETTA PER ID E MODIFICA IMMAGINE (solo proprietario o ADMIN)
    public Recipe findByIdAndUploadImage(UUID recipeId, MultipartFile file, User currentUser) {
        if (file.isEmpty()) throw new NotEmptyException();

        // Cerco la ricetta
        Recipe found = repository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Ricetta non trovata"));

        // SICUREZZA: verifico che l'utente loggato sia l'autore della ricetta altrimenti sia un ADMIN
        if (!found.getUser().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().name().equals("ADMIN")) {
            throw new UnauthorizedException("Permesso negato");
        }

        try {
            // Carichiamo su Cloudinary
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) result.get("secure_url");

            // Aggiorniamo l'URL nell'entità
            found.setImageURL(imageUrl);

            return repository.save(found);

        } catch (IOException e) {
            throw new RuntimeException("Errore durante l'upload dell'immagine", e);
        }
    }

    // TROVA RICETTA E LA MODIFICA (solo proprietario)
    @Transactional
    public Recipe updateRecipe(UUID id, RecipeRequestDTO dto, User currentUser) {
        Recipe found = this.getRecipeById(id);

        // Controllo sicurezza: solo l'autore può modificare
        if (!found.getUser().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().name().equals("ADMIN")) {
            throw new UnauthorizedException("Permesso negato");
        }

        found.getTags().clear();

        if (dto.tags() != null) {
            dto.tags().forEach(tagName -> {
                Tag tag = tagService.saveTag(tagName);
                // 4. Lo aggiungiamo alla collezione della ricetta
                found.getTags().add(tag);
            });
        }

        // Aggiornamento campi base
        found.setTitolo(dto.titolo());
        found.setDescrizione(dto.descrizione());
        found.setTempoPrep(dto.tempoPrep());
        found.setTempoCottura(dto.tempoCottura());
        found.setDifficolta(Difficolta.valueOf(dto.difficolta()));

        // Per Ingredienti e Step: svuotiamo e ricreiamo
        // (Grazie a orphanRemoval=true, JPA pulisce il DB per noi)
        found.getIngredienti().clear();
        dto.ingredienti().forEach(iDto -> {
            Ingredient ing = new Ingredient();
            ing.setNome(iDto.nome());
            ing.setQuantita(iDto.quantita());
            ing.setRecipe(found);
            found.getIngredienti().add(ing);
        });

        found.getSteps().clear();
        dto.steps().forEach(sDto -> {
            Step step = new Step();
            step.setOrdine(sDto.ordine());
            step.setDescrizione(sDto.descrizione());
            step.setRecipe(found);
            found.getSteps().add(step);
        });

        return repository.save(found);
    }

    // ELIMINA RICETTA (solo proprietario o ADMIN)
    @Transactional
    public void deleteRecipe(UUID id, User currentUser) {
        Recipe found = this.getRecipeById(id);

        // Solo l'autore o un ADMIN possono eliminare
        if (!found.getUser().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().name().equals("ADMIN")) {
            throw new UnauthorizedException("Non puoi eliminare una ricetta non tua.");
        }

        // Recupero le varianti(FORK)
        List<Recipe> variants = repository.findByParentRecipe(found);

        // Sgancio le varianti
        if (!variants.isEmpty()) {
            variants.forEach(v -> {
                v.setParentRecipe(null); // Diventano ricette originali
            });
            repository.saveAll(variants); // Aggiorna tutte le varianti in un colpo solo
        }

        likeRepository.deleteByRecipe(found);

        repository.delete(found);
    }

    public List<Recipe> findRecipesByTag(String tagName) {
        return repository.findByTags_NomeIgnoreCase(tagName);
    }

    // RICERCA RICETTA PER TITOLO (Case Insensitive)
    public List<Recipe> searchRecipes(String query) {
        return repository.findByTitoloContainingIgnoreCase(query);
    }

    public RecipeResponseDTO getRecipeDetail(UUID id, User currentUser) {
        Recipe recipe = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ricetta non trovata"));

        boolean liked = false;
        boolean following = false;

        if (currentUser != null) {
            liked = likeRepository.existsByUserAndRecipe(currentUser, recipe);
            following = followRepository.existsByFollowerAndFollowing(currentUser, recipe.getUser());
        }

        return new RecipeResponseDTO(recipe, liked, following);
    }

    public List<Recipe> findLikedRecipesByUser(User user) {
        // 1. Recuperiamo tutti i record della tabella Like per quell'utente
        List<Like> likes = likeRepository.findByUser(user);

        // 2. Estraiamo la ricetta da ogni oggetto Like e restituiamo la lista
        return likes.stream()
                .map(Like::getRecipe)
                .toList();
    }
}
