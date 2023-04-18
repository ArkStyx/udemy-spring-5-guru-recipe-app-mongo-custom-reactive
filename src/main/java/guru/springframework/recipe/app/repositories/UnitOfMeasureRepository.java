package guru.springframework.recipe.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.recipe.app.domain.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

	Optional<UnitOfMeasure> findByDescription(String description);
}
