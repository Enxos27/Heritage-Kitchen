package vincenzocalvaruso.Heritage_Kitchen.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vincenzocalvaruso.Heritage_Kitchen.entity.Like;
import vincenzocalvaruso.Heritage_Kitchen.entity.Recipe;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.payloads.LikeNotificationDTO;
import vincenzocalvaruso.Heritage_Kitchen.repository.LikeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    public void toggleLike(User user, Recipe recipe) {
        // Verifico se il like esiste già (usando il metodo nella Repository)
        if (likeRepository.existsByUserAndRecipe(user, recipe)) {
            // Se esiste, lo tolgo (Unlike)
            likeRepository.deleteByUserAndRecipe(user, recipe);
        } else {
            // Se non esiste, lo creo
            Like like = new Like();
            like.setUser(user);
            like.setRecipe(recipe);
            likeRepository.save(like);
        }
    }

    public long getRecipeLikeCount(Recipe recipe) {
        return likeRepository.countByRecipe(recipe);
    }

    // Restituisce la lista di RICETTE a cui l'utente ha messo like
    public List<Recipe> getUserFavorites(User user) {
        return likeRepository.findByUser(user)
                .stream()
                .map(Like::getRecipe)
                .toList();
    }

    public List<LikeNotificationDTO> getRecentLikeNotifications(User currentUser) {
        List<Like> likes = likeRepository.findRecentLikesForUser(currentUser.getId());

        return likes.stream()
                .filter(l -> !l.getUser().getId().equals(currentUser.getId())) // Esclude i propri like
                .map(l -> new LikeNotificationDTO(
                        l.getUser().getUsername(),
                        l.getUser().getAvatar(),
                        l.getRecipe().getTitolo(),
                        l.getRecipe().getId(),
                        l.getCreatedAt()
                )).toList();
    }

    public Map<UUID, Long> getLikesCounts(List<UUID> recipeIds) {
        Map<UUID, Long> counts = new HashMap<>();

        for (UUID id : recipeIds) {
            long count = likeRepository.countByRecipeId(id);
            counts.put(id, count);
        }

        return counts;
    }
}
