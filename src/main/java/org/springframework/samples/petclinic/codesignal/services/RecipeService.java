package org.springframework.samples.petclinic.codesignal.services;

import org.springframework.samples.petclinic.codesignal.models.Recipe;

import java.util.List;

public interface RecipeService {
	/**
	 * Retrieves recipes filtered by type.
	 * @param type The category of recipe (e.g., "vegan"), or null to return all.
	 * @return A list of matching recipes.
	 */
	List<Recipe> getRecipesByType(String type);
}
