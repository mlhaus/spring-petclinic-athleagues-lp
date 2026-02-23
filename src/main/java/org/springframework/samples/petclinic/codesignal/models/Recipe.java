package org.springframework.samples.petclinic.codesignal.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.*;
import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// This tells JPA the database column name is "recipe_ingredients"
	@Column(name = "recipe_ingredients")
	// This tells Jackson the JSON key name is "recipe_ingredients"
	@JsonProperty("recipe_ingredients")
	private String ingredients;

	private String instructions;

	private String type;

	private String category;

	// Spring/Hibernate automatically maps camelCase to snake_case (dietary_preference)
	private String dietaryPreference;

	@JsonIgnore
	private String internalNotes;

	// Hibernate needs a no-args constructor
	public Recipe() {
	}

	public Long getId() {
		return id;
	}

	public String getIngredients() {
		return ingredients;
	}

	public String getInstructions() {
		return instructions;
	}

	public String getType() {
		return type;
	}

	public String getCategory() {
		return category;
	}

	public String getDietaryPreference() {
		return dietaryPreference;
	}

	public String getInternalNotes() {
		return internalNotes;
	}

}
