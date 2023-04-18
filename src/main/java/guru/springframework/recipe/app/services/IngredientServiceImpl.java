package guru.springframework.recipe.app.services;

import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.recipe.app.commands.IngredientCommand;
import guru.springframework.recipe.app.converters.fromcommand.IngredientCommandToIngredient;
import guru.springframework.recipe.app.converters.fromdomain.IngredientToIngredientCommand;
import guru.springframework.recipe.app.domain.Ingredient;
import guru.springframework.recipe.app.domain.Recipe;
import guru.springframework.recipe.app.repositories.RecipeRepository;
import guru.springframework.recipe.app.repositories.UnitOfMeasureRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {
	
	private final RecipeRepository recipeRepository;
	private final UnitOfMeasureRepository unitOfMeasureRepository;
	
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	
	// XXX correspondance nom methode JAVA GURU - John Thompson : findByRecipeIdAndIngredientId()
	@Override
	public IngredientCommand recupererParIdRecetteEtIdIngredient(String idRecette, String idIngredient) {
		
		Optional<Recipe> optionalRecipe = recipeRepository.findById(idRecette);
		if (!optionalRecipe.isPresent()) {
			// TODO IMPLEMENTER ERREUR
			log.error("Aucune recette trouvee pour cet idRecette : " + idRecette);
		}
		
		Recipe recette = optionalRecipe.get();
		
		Optional<IngredientCommand> optionalIngredientCommand = recette.getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(idIngredient))
				.map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
				.findFirst();
		
		if (!optionalIngredientCommand.isPresent()) {
			// TODO IMPLEMENTER ERREUR
			log.error("Aucune ingredient trouve pour cet idIngredient : " + idIngredient);
		}

		return optionalIngredientCommand.get();
	}
	
	// XXX correspondance nom methode JAVA GURU - John Thompson : saveIngredientCommand()
	@Transactional
	@Override
	public IngredientCommand sauvegarderIngredient(IngredientCommand ingredientCommand) {

		String idRecette = ingredientCommand.getRecipeId();
		Optional<Recipe> optionalRecipe = recipeRepository.findById(idRecette);
		
		if (optionalRecipe.isPresent()) {
			Recipe recetteTrouvee = optionalRecipe.get();

			Optional<Ingredient> optionalIngredient = recetteTrouvee.getIngredients()
																	.stream()
																	.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
																	.findFirst();
			
			if (optionalIngredient.isPresent()) {
				Ingredient ingredientTrouve = optionalIngredient.get();
				ingredientTrouve.setDescription(ingredientCommand.getDescription());
				ingredientTrouve.setAmount(ingredientCommand.getAmount());

				/* FIXME ON VERIFIE QUE L'UNITE DE MESURE EXISTE BIEN */
				ingredientTrouve.setUnitOfMeasure(unitOfMeasureRepository
						.findById(ingredientCommand.getUnitOfMeasure().getId())
						.orElseThrow(() -> new RuntimeException("Unite de mesure non trouvee")));
			}
			else {
				
				// TODO METHODE 01
//				recetteTrouvee.addIngredient(ingredientCommandToIngredient.convert(ingredientCommand));
				
				// TODO METHODE 02 - QUEL DIFFERENCE AVEC LA METHODE 01 ?
				Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
				recetteTrouvee.addIngredient(ingredient);
			}
			
			Recipe recetteSauvegardee = recipeRepository.save(recetteTrouvee);
			
			Optional<Ingredient> optionalIngredientSauvegarde = recetteSauvegardee.getIngredients()
																				.stream()
																				.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
																				.findFirst();
			
            //check by description
            if(!optionalIngredientSauvegarde.isPresent()){
                //not totally safe... But best guess
            	optionalIngredientSauvegarde = recetteSauvegardee.getIngredients().stream()
            		.filter(recipeIngredients -> recipeIngredients.getDescription().equals(ingredientCommand.getDescription()))
            		.filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredientCommand.getAmount()))
            		.filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(ingredientCommand.getUnitOfMeasure().getId()))
            		.findFirst();
            }
			
			return ingredientToIngredientCommand.convert(optionalIngredientSauvegarde.get());
		}
		else {
			// TODO IMPLEMENTER ERREUR
			log.error("Aucune recette trouvee pour cet idRecette : " + idRecette);
            return new IngredientCommand();
		}
	}

	
	// XXX correspondance nom methode JAVA GURU - John Thompson : deleteById()
	@Override
	public void supprimerIngredientDansRecetteParId(String idRecette, String idIngredient) {

		Optional<Recipe> optionalRecipe = recipeRepository.findById(idRecette);
		if (optionalRecipe.isPresent()) {
			Recipe recette = optionalRecipe.get();
			
			Predicate<Ingredient> filtreIdIngredient = ingredient -> ingredient.getId().equals(idIngredient);
			Optional<Ingredient> optionalIngredient = recette.getIngredients().stream()
																			.filter(filtreIdIngredient)
																			.findFirst();
			if (optionalIngredient.isPresent()) {
                Ingredient ingredientPourSuppression = optionalIngredient.get(); 
                recette.getIngredients().remove(ingredientPourSuppression);
                recipeRepository.save(recette);
			}
			
		}
		else {
			log.error("La recette recherchee n'existe pas - idRecette : " + idRecette);
		}
	}

}
