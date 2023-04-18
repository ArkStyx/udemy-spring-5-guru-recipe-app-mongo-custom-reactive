package guru.springframework.recipe.app.controllers;

import org.slf4j.Marker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.recipe.app.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller
public class IndexController {

	private final RecipeService recipeService;

	@RequestMapping({"", "/", "/index"})
	public String getIndexPage(Model model) {
		
		log.info("Test @Slf4j");
		log.info(Marker.ANY_MARKER, "Test @Slf4j Marker * ====> https://examples.javacodegeeks.com/enterprise-java/slf4j/slf4j-markers-example/");
//		log.info("Test @Slf4j Throwable", new RuntimeException("Coucou RuntimeException pour @Slf4j"));
		
		model.addAttribute("toutesLesRecettes", recipeService.getRecipes());
		return "index";
	}
}
