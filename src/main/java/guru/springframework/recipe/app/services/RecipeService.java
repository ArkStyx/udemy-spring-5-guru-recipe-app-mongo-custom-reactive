package guru.springframework.recipe.app.services;

import java.util.Set;

import guru.springframework.recipe.app.commands.RecipeCommand;
import guru.springframework.recipe.app.domain.Recipe;

public interface RecipeService {

	Set<Recipe> getRecipes();
	
	Recipe findById(String id);
	
	RecipeCommand saveRecipeCommand(RecipeCommand command);
	
	RecipeCommand findCommandById(String id);
	
	void deleteById(String id);
}
