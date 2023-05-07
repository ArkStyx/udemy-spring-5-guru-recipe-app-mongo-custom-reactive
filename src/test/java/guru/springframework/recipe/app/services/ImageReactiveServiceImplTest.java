
package guru.springframework.recipe.app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import guru.springframework.recipe.app.domain.Recipe;
import guru.springframework.recipe.app.repositories.RecipeRepository;

public class ImageReactiveServiceImplTest {

	ImageReactiveServiceImpl imageServiceImpl;
	
	@Mock
	RecipeRepository recipeRepository;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		imageServiceImpl = new ImageReactiveServiceImpl(recipeRepository);
	}
	
	@Test
	public void saveImageFile() throws Exception {
		
    	/* Given */
		String idRecette = "1";

		String name = "file";
		String originalFileName = "testing.txt";
		String contentType = "text/plain";
		byte[] content = "Spring Framework Guru".getBytes();
		MockMultipartFile mockMultipartFile = new MockMultipartFile(name, originalFileName, contentType, content);
		
		Recipe recipe = new Recipe();
		recipe.setId(idRecette);
		
		Optional<Recipe> optionalRecipe = Optional.of(recipe);
		
		when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);
		
		ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);
		
		
    	/* When */
		imageServiceImpl.saveImageFile(idRecette, mockMultipartFile);
		
    	/* Then */
		verify(recipeRepository, times(1)).save(argumentCaptor.capture());
		Recipe savedRecipe = argumentCaptor.getValue();
		assertEquals(mockMultipartFile.getBytes().length, savedRecipe.getImage().length);
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, times(1)).save(any(Recipe.class));
	}
	
}
