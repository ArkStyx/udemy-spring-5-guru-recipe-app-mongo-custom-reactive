package guru.springframework.recipe.app.controllers;

import java.util.List;

import org.slf4j.Marker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.recipe.app.domain.Recipe;
import guru.springframework.recipe.app.services.RecipeReactiveService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Controller
public class IndexController {

	private final RecipeReactiveService recipeReactiveService;

	@RequestMapping({"", "/", "/index"})
	public String getIndexPage(Model model) {
		
		log.info("Test @Slf4j");
		log.info(Marker.ANY_MARKER, "Test @Slf4j Marker * ====> https://examples.javacodegeeks.com/enterprise-java/slf4j/slf4j-markers-example/");
//		log.info("Test @Slf4j Throwable", new RuntimeException("Coucou RuntimeERecipeon pour @Slf4j"));
		
		Flux<Recipe> fluxRecipe = recipeReactiveService.getRecipes();
		Mono<List<Recipe>> monoListRecipe = fluxRecipe.collectList();
		List<Recipe> listeDesRecettes = monoListRecipe.block();
		
		model.addAttribute("toutesLesRecettes", listeDesRecettes);
		return "index";
	}
}
