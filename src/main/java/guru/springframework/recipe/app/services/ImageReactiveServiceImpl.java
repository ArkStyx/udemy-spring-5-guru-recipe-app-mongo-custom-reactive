package guru.springframework.recipe.app.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.recipe.app.domain.Recipe;
import guru.springframework.recipe.app.repositories.RecipeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class ImageReactiveServiceImpl implements ImageReactiveService {

	private final RecipeRepository recipeRepository;
	
	@Override
	public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {
		log.info("Reception d'un fichier : " + file.getName());

		Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
		if (optionalRecipe.isPresent()) {
			
			try {
				byte[] byteFile = file.getBytes();
				Byte[] byteObjects = new Byte[byteFile.length];
				int i = 0;
				for (byte b : byteFile) {
					byteObjects[i++] = b;
				}
				
				Recipe recipe = optionalRecipe.get();
				recipe.setImage(byteObjects);
				recipeRepository.save(recipe);
				
			} catch (IOException e) {
				log.error("Erreur lors de la lecture du fichier");
			}
		}
		else {
			log.error("La recette n'existe pas - id recette : " + recipeId);
		}
		return Mono.empty();
	}

}
