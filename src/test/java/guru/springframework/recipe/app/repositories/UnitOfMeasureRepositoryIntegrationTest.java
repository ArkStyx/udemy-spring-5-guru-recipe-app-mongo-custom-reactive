package guru.springframework.recipe.app.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import guru.springframework.recipe.app.domain.UnitOfMeasure;

@Disabled("Classe de tests seulement utilisable avec JPA")
@ExtendWith(SpringExtension.class)
@DataMongoTest
class UnitOfMeasureRepositoryIntegrationTest {

	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void findByDescription() throws Exception {
		String uniteDeMesureCherchee = "Teaspoon";
		Optional<UnitOfMeasure> optionalTeaspoon = unitOfMeasureRepository.findByDescription(uniteDeMesureCherchee);
		assertEquals(uniteDeMesureCherchee, optionalTeaspoon.get().getDescription());
	}

}
