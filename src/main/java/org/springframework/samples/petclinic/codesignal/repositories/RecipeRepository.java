package org.springframework.samples.petclinic.codesignal.repositories;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.codesignal.models.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends Repository<Recipe, Integer> {

	Optional<Recipe> findById(Long recipeId);

	List<Recipe> findByCategory(String recipeCategory);

	List<Recipe> findAll();

}
