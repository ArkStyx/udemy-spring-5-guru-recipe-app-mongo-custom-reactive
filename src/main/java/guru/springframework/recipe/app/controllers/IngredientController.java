package guru.springframework.recipe.app.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import guru.springframework.recipe.app.commands.IngredientCommand;
import guru.springframework.recipe.app.commands.RecipeCommand;
import guru.springframework.recipe.app.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.app.services.IngredientService;
import guru.springframework.recipe.app.services.RecipeService;
import guru.springframework.recipe.app.services.UnitOfMeasureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller
public class IngredientController {

	private final RecipeService recipeService;
	private final IngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;
	
	// XXX correspondance nom methode JAVA GURU - John Thompson : listIngredients()
	@GetMapping(value = "/recipe/{idRecetteDansUrl}/ingredients")
	public String recupererListeIngredients(Model model, @PathVariable("idRecetteDansUrl") String id) {
		log.debug("Id de la recette pour les ingredients recherches : " + id);
		model.addAttribute("recipe", recipeService.findCommandById(id));
		return "recipe/ingredient/list";
	}
	
	// XXX correspondance nom methode JAVA GURU - John Thompson : showRecipeIngredient()
	@GetMapping(value = "/recipe/{idRecette}/ingredient/{idIngredient}/show")
	public String afficherIngredientDansRecette(Model model, @PathVariable String idRecette, @PathVariable String idIngredient) {
		model.addAttribute("ingredient", ingredientService.recupererParIdRecetteEtIdIngredient(idRecette, idIngredient));
		return "recipe/ingredient/show";
	}
	
	// XXX correspondance nom methode JAVA GURU - John Thompson : updateRecipeIngredient()
    @GetMapping("recipe/{recipeId}/ingredient/{id}/update")
	public String modifierIngredientDansRecette(Model model, @PathVariable("recipeId") String idRecette, @PathVariable("id") String idIngredient) {
    	model.addAttribute("ingredient", ingredientService.recupererParIdRecetteEtIdIngredient(idRecette, idIngredient));
    	model.addAttribute("listeUnitesDeMesure", unitOfMeasureService.recupererToutesLesUnitesDeMesure());
		return "recipe/ingredient/ingredientform";
	}

	// XXX correspondance nom methode JAVA GURU - John Thompson : saveOrUpdate()
	@PostMapping("recipe/{recipeId}/ingredient")
	public String sauvegarderOuModifierIngredientDansRecette(@ModelAttribute IngredientCommand ingredientCommand) {
		IngredientCommand ingredientSauvegarde = ingredientService.sauvegarderIngredient(ingredientCommand);
		String idRecette = ingredientSauvegarde.getRecipeId();
		String idIngredient = ingredientSauvegarde.getId();
		
		return "redirect:/recipe/" + idRecette + "/ingredient/" + idIngredient + "/show";
	}
	
	// XXX correspondance nom methode JAVA GURU - John Thompson : newIngredient() / newRecipe()
    @GetMapping("recipe/{recipeId}/ingredient/new")
    public String creerNouvelIngredient(Model model, @PathVariable("recipeId") String idRecette) {
    	
    	IngredientCommand ingredientCommand = new IngredientCommand();
    	ingredientCommand.setRecipeId(idRecette);
    	ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
    	
    	RecipeCommand recetteTrouvee = recipeService.findCommandById(idRecette);
    	recetteTrouvee.getIngredients().add(ingredientCommand);
    	
    	List<UnitOfMeasureCommand> linkedHashSetUnitOfMeasureCommand = unitOfMeasureService.recupererToutesLesUnitesDeMesure().collectList().block();

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("listeUnitesDeMesure", linkedHashSetUnitOfMeasureCommand);
        
        return "recipe/ingredient/ingredientform";
    }

	// XXX correspondance nom methode JAVA GURU - John Thompson : deleteIngredient()
    @GetMapping("recipe/{recipeId}/ingredient/{id}/delete")
    public String supprimerIngredient(@PathVariable("recipeId") String idRecette, @PathVariable("id") String idIngredient) {

        log.info("Suppression ingredient dans recette - idRecette : " + idRecette + " / idIngredient : " + idIngredient);
    	ingredientService.supprimerIngredientDansRecetteParId(idRecette, idIngredient);
    	
    	return "redirect:/recipe/" + idRecette + "/ingredients";
    }
  
}
