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
import guru.springframework.recipe.app.domain.UnitOfMeasure;
import guru.springframework.recipe.app.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.recipe.app.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {
	
	private final RecipeReactiveRepository recipeReactiveRepository;
	
	private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
	
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	
	// XXX correspondance nom methode JAVA GURU - John Thompson : findByRecipeIdAndIngredientId()
	@Override
	public Mono<IngredientCommand> recupererParIdRecetteEtIdIngredient(String idRecette, String idIngredient) {
		
		Mono<Recipe> monoRecipe = recipeReactiveRepository.findById(idRecette);
		
		Optional<IngredientCommand> optionalIngredientCommand =  monoRecipe.block().getIngredients()
																		.stream()
																		.filter(ingredient -> ingredient.getId().equals(idIngredient))
																		.map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
																		.findFirst();

		return Mono.just(optionalIngredientCommand.get());
	}
	
	// XXX correspondance nom methode JAVA GURU - John Thompson : saveIngredientCommand()
	@Transactional
	@Override
	public Mono<IngredientCommand> sauvegarderIngredient(IngredientCommand ingredientCommand) {

		String idRecette = ingredientCommand.getRecipeId();
		Mono<Recipe> monoRecipe = recipeReactiveRepository.findById(idRecette);
		
		Recipe recetteTrouvee = monoRecipe.block();
		if (recetteTrouvee == null) {
			return Mono.just(new IngredientCommand());
		}
		else {
			Optional<Ingredient> optionalIngredient = recetteTrouvee.getIngredients()
																	.stream()
																	.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
																	.findFirst();
			
			if (optionalIngredient.isPresent()) {
				Ingredient ingredientTrouve = optionalIngredient.get();
				ingredientTrouve.setDescription(ingredientCommand.getDescription());
				ingredientTrouve.setAmount(ingredientCommand.getAmount());

				/* ON VERIFIE QUE L'UNITE DE MESURE EXISTE BIEN */
				Mono<UnitOfMeasure> monoUnitOfMeasure = unitOfMeasureReactiveRepository.findById(ingredientCommand.getUnitOfMeasure().getId());
				ingredientTrouve.setUnitOfMeasure(monoUnitOfMeasure.block());
			}
			else {
				recetteTrouvee.addIngredient(ingredientCommandToIngredient.convert(ingredientCommand));
			}
			
			Mono<Recipe> monoRecipeSauvegardee = recipeReactiveRepository.save(recetteTrouvee);
			
			Recipe recetteSauvegardee = monoRecipeSauvegardee.block();
			
			Optional<Ingredient> optionalIngredientSauvegarde = recetteSauvegardee.getIngredients()
																		.stream()
																		.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
																		.findFirst();
			
            //check by description
            if (!optionalIngredientSauvegarde.isPresent()) {
                //not totally safe... But best guess
            	optionalIngredientSauvegarde = recetteSauvegardee.getIngredients().stream()
            		.filter(recipeIngredients -> recipeIngredients.getDescription().equals(ingredientCommand.getDescription()))
            		.filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredientCommand.getAmount()))
            		.filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(ingredientCommand.getUnitOfMeasure().getId()))
            		.findFirst();
            }
            
            IngredientCommand ingredientCommandSauvegardee = ingredientToIngredientCommand.convert(optionalIngredientSauvegarde.get());
			return Mono.just(ingredientCommandSauvegardee);
		}
	}

	
	// XXX correspondance nom methode JAVA GURU - John Thompson : deleteById()
	@Override
	public Mono<Void> supprimerIngredientDansRecetteParId(String idRecette, String idIngredient) {

		Mono<Recipe> monoRecipe = recipeReactiveRepository.findById(idRecette);
		if (monoRecipe == null) {
			log.error("La recette recherchee n'existe pas - idRecette : " + idRecette);
		}
		else {
			Recipe recette = monoRecipe.block();
			
			Predicate<Ingredient> filtreIdIngredient = ingredient -> ingredient.getId().equals(idIngredient);
			Optional<Ingredient> optionalIngredient = recette.getIngredients().stream()
																			.filter(filtreIdIngredient)
																			.findFirst();
			
			if (optionalIngredient.isPresent()) {
                Ingredient ingredientPourSuppression = optionalIngredient.get(); 
                recette.getIngredients().remove(ingredientPourSuppression);
                recipeReactiveRepository.save(recette);
			}
		}
		return Mono.empty();
	}

}
