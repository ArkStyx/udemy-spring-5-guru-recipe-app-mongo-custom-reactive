package guru.springframework.recipe.app.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import guru.springframework.recipe.app.commands.RecipeCommand;
import guru.springframework.recipe.app.services.ImageService;
import guru.springframework.recipe.app.services.RecipeService;

public class ImageControllerTest {

	@Mock
	ImageService imageService;
	
	@Mock
	RecipeService recipeService;
	
	@InjectMocks
	ImageController imageController;
	
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(imageController).
				setControllerAdvice(new ControllerExceptionHandler()).
				build();
	}
	
	@Test
	public void getImageForm() throws Exception {
		
		/* Given */
		String idRecette = "1";
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId(idRecette);
		
		when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);
		
		/* When */
		
		/* Then */
		mockMvc.perform(
					MockMvcRequestBuilders.get("/recipe/1/image")
				).
				andExpect(status().isOk()).
				andExpect(view().name("recipe/imageuploadform")).
				andExpect(model().attributeExists("recipe"));
		
		verify(recipeService, times(1)).findCommandById(anyString());
	}
	
	@Test
	public void handleImagePost() throws Exception {
		
		/* Given */
		String name = "imagefile";
		String originalFileName = "testing.txt";
		String contentType = "text/plain";
		byte[] content = "Spring Framework Guru".getBytes();
		MockMultipartFile mockMultipartFile = new MockMultipartFile(name, originalFileName, contentType, content);
		
		/* When */
		
		/* Then */
		mockMvc.perform(
					MockMvcRequestBuilders.multipart("/recipe/1/image").file(mockMultipartFile)
				).
				andExpect(status().is3xxRedirection()).
				andExpect(header().string("Location", "/recipe/1/show"));
		
		verify(imageService, times(1)).saveImageFile(anyString(), any());
	}
	
	@Test
	void renderImageFromDB() throws Exception {
		
		/* Given */
		String fakeImage = "fake image text";
		byte[] fakeImageByte = fakeImage.getBytes();
		
		int i = 0;
		Byte[] imageFromDB = new Byte[fakeImageByte.length];
		for (byte primByte : fakeImageByte) {
			imageFromDB[i++] = primByte;
		}
		
		String idRecette = "1";
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId(idRecette);
		recipeCommand.setImage(imageFromDB);
		
		when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);
		
		/* When */
		
		/* Then */
		MockHttpServletResponse response = mockMvc.perform(
					MockMvcRequestBuilders.get("/recipe/1/recipeimage")
				).
				andExpect(status().isOk()).
				andReturn().getResponse();
		
		byte[] reponseBytes = response.getContentAsByteArray();
		
		assertEquals(fakeImageByte.length, reponseBytes.length);
	}
	
}
