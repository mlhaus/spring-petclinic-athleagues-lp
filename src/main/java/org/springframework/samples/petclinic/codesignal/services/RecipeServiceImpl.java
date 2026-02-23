package org.springframework.samples.petclinic.codesignal.services;

import org.springframework.samples.petclinic.codesignal.models.Recipe;
import org.springframework.samples.petclinic.codesignal.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

	// Assuming you have a repository injected
	private final RecipeRepository recipeRepository;

	public RecipeServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Override
	public List<Recipe> getRecipesByType(String type) {
		List<Recipe> allRecipes = recipeRepository.findAll();

		if (type == null || type.isEmpty()) {
			return allRecipes;
		}

		// Case-insensitive filtering using Java Streams
		return allRecipes.stream()
			.filter(recipe -> recipe.getType().equalsIgnoreCase(type))
			.collect(Collectors.toList());
	}

}
