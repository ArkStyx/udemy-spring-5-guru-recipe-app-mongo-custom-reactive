package guru.springframework.recipe.app.repositories.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import guru.springframework.recipe.app.domain.Category;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {

}
