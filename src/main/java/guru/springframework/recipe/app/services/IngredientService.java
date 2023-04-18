package guru.springframework.recipe.app.services;

import guru.springframework.recipe.app.commands.IngredientCommand;

public interface IngredientService {

	IngredientCommand recupererParIdRecetteEtIdIngredient(String idRecette, String idIngredient);

	IngredientCommand sauvegarderIngredient(IngredientCommand ingredientCommand);

	void supprimerIngredientDansRecetteParId(String idRecette, String idIngredient);
}
