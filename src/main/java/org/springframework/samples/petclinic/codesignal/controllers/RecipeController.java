package org.springframework.samples.petclinic.codesignal.controllers;

import org.springframework.samples.petclinic.codesignal.models.Recipe;
import org.springframework.samples.petclinic.codesignal.repositories.RecipeRepository;
import org.springframework.samples.petclinic.codesignal.services.RecipeService;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class RecipeController {

	private final RecipeService recipeService;
	private final RecipeRepository recipeRepository;

	public RecipeController(RecipeService recipeService, RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
		this.recipeService = recipeService;
	}

	@GetMapping("/recipes/{recipeId}")
	public Recipe getRecipe(@PathVariable("recipeId") Long id) {
		return recipeRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
	}

	@GetMapping("/recipes")
	public List<Recipe> getRecipesByType(@RequestParam Optional<String> type) {
		return recipeService.getRecipesByType(type.orElse(null));
	}

	@GetMapping("/category/{recipeCategory}")
	public List<Recipe> getRecipesByCategoryAndDietaryPreference(@PathVariable String recipeCategory, @RequestParam Optional<String> dietaryPreference) {
		List<Recipe> recipes = recipeRepository.findByCategory(recipeCategory);

		if (dietaryPreference.isPresent()) {
			return recipes.stream()
				.filter(recipe -> recipe.getDietaryPreference().equalsIgnoreCase(dietaryPreference.get()))
				.collect(Collectors.toList());
		}

		return recipes;
	}
}
