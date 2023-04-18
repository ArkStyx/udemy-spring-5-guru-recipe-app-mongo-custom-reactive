package guru.springframework.recipe.app.repositories;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.recipe.app.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

}
