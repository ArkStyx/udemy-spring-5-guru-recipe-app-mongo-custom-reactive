package guru.springframework.recipe.app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import guru.springframework.recipe.app.commands.RecipeCommand;
import guru.springframework.recipe.app.converters.fromcommand.RecipeCommandToRecipe;
import guru.springframework.recipe.app.converters.fromdomain.RecipeToRecipeCommand;
import guru.springframework.recipe.app.domain.Recipe;
import guru.springframework.recipe.app.exceptions.NotFoundException;
import guru.springframework.recipe.app.repositories.RecipeRepository;

class RecipeServiceImplTestJupiter {

	private static final String ID = "1";
	
	RecipeServiceImpl recipeServiceImpl;
	
	@Mock
	RecipeRepository recipeRepository;
	
	@Mock
	RecipeCommandToRecipe recipeCommandToRecipe;
	
	@Mock
	RecipeToRecipeCommand recipeToRecipeCommand;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		recipeServiceImpl = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
	}
	
	
	// XXX correspondance nom methode JAVA GURU - John Thompson : getRecipeByIdTest()
	@Test
	void testGetRecipes() {
		Recipe recetteGuacamole = new Recipe();
		recetteGuacamole.setDescription("Guacamole maison");
		
		Recipe recetteTacos = new Recipe();
		recetteTacos.setDescription("Tacos maison");

		Set<Recipe> fausseListeDeRecettes = new LinkedHashSet<>();
		fausseListeDeRecettes.add(recetteGuacamole);
		fausseListeDeRecettes.add(recetteTacos);

		when(recipeRepository.findAll()).thenReturn(fausseListeDeRecettes);
		
		Set<Recipe> listeDeRecettes = recipeServiceImpl.getRecipes();
		assertEquals(2, listeDeRecettes.size());
		verify(recipeRepository, Mockito.times(1)).findAll();
	}

	// XXX correspondance nom methode JAVA GURU - John Thompson : getRecipeCoomandByIdTest()
	@Test
	void testGetRecipeById() {
		Recipe recette = new Recipe();
		recette.setId(ID);
		
		Optional<Recipe> optionalRecette = Optional.of(recette);
		when(recipeRepository.findById(anyString())).thenReturn(optionalRecette);
		
		RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID);

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipeServiceImpl.findCommandById(ID);
		
        assertNotNull(commandById);
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, never()).findAll();
	}
	
    @Test
    public void getRecipesTest() throws Exception {

        Recipe recipe = new Recipe();
        Set<Recipe> fausseListeDeRecettes = new LinkedHashSet<>();
        fausseListeDeRecettes.add(recipe);

        when(recipeRepository.findAll()).thenReturn(fausseListeDeRecettes);

        Set<Recipe> recipes = recipeServiceImpl.getRecipes();

        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyString());
    }

    @Test
    public void testDeleteById() throws Exception {

        /* Given */

        /* When */
        recipeServiceImpl.deleteById(anyString());

        // TODO no 'when(...)', since method has void return type

        /* Then */
        verify(recipeRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void getRecipeByIdTestNotFound() throws Exception {

        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        assertThrows(NotFoundException.class, () -> {
        	recipeServiceImpl.findById(ID);
        });
    }
    
}
