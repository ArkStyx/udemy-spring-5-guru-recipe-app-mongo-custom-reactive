package guru.springframework.recipe.app.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.recipe.app.domain.Recipe;
import guru.springframework.recipe.app.services.RecipeService;

class IndexControllerTest {

	IndexController indexController;
	
	@Mock
	RecipeService recipeService;
	
	@Mock
	Model model;
	
	/*
	 * FIXME
	 * PROBLEME MOCKITO SUR ArgumentCaptor.forClass(XXX.class) AVEC DES LIST, SET, ETC :
	 * https://stackoverflow.com/questions/5606541/how-to-capture-a-list-of-specific-type-with-mockito
	 */
	@Captor
	private ArgumentCaptor<Set<Recipe>> argumentCaptorSetRecipe;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		indexController = new IndexController(recipeService);	
	}

	@Test
	void testGetIndexPage() {
		
		/* Given */
		Recipe recetteGuacamole = new Recipe();
		recetteGuacamole.setDescription("Guacamole maison");
		
		Recipe recetteTacos = new Recipe();
		recetteTacos.setDescription("Tacos maison");

		Set<Recipe> fausseListeDeRecettes = new LinkedHashSet<Recipe>();
		fausseListeDeRecettes.add(recetteGuacamole);
		fausseListeDeRecettes.add(recetteTacos);
		
		when(recipeService.getRecipes()).thenReturn(fausseListeDeRecettes);
		

		/* When */
		String retourModel = indexController.getIndexPage(model);
		
		
		/* Then */
		assertEquals("index", retourModel);
		
		/*
		 * TODO ON NE PEUT FAIRE DES verify QUE SUR DES MOCKS !!!!
		 */
//		verify(indexController, Mockito.times(1)).getIndexPage(model);
		
		verify(recipeService, Mockito.times(1)).getRecipes();
		verify(model, Mockito.times(1)).addAttribute(eq("toutesLesRecettes"), argumentCaptorSetRecipe.capture());
		
		Set<Recipe> retourArgumentCaptorSetRecipe = argumentCaptorSetRecipe.getValue();
		assertEquals(2, retourArgumentCaptorSetRecipe.size());
	}
	
	@Test
	void testMockMVC() throws Exception {

		String rootContext = "/";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(rootContext);
		ResultMatcher resultMatcherStatusOk = status().isOk();
		ResultMatcher resultMatcherViewNameIndex = view().name("index");
		
		MockMvc mockMVC = MockMvcBuilders.standaloneSetup(indexController).build();
		mockMVC.perform(requestBuilder).andExpect(resultMatcherStatusOk).andExpect(resultMatcherViewNameIndex);
	}

}
