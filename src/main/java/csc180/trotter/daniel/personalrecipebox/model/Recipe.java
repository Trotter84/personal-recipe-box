package csc180.trotter.daniel.personalrecipebox.model;


import java.io.Serializable;
import java.util.List;


public class Recipe implements Serializable {
	private String name;
	private List<String> ingredients;
	private List<String> steps;
	private String category;
	private int prepTimeMinutes;
	private List<String> tags;

	public Recipe(String name, List<String> ingredients, List<String> steps, String category, int prepTimeMinutes, List<String> tags) {
		this.name = name;
		this.ingredients = ingredients;
		this.steps = steps;
		this.category = category;
		this.prepTimeMinutes = prepTimeMinutes;
		this.tags = tags;
	}

	public Recipe(String name, List<String> ingredients, List<String> steps, String category, int prepTimeMinutes) {
		this.name = name;
		this.ingredients = ingredients;
		this.steps = steps;
		this.category = category;
		this.prepTimeMinutes = prepTimeMinutes;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	private void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public List<String> getSteps() {
		return steps;
	}

	private void setSteps(List<String> steps) {
		this.steps = steps;
	}

	public String getCategory() {
		return category;
	}

	private void setCategory(String category) {
		this.category = category;
	}

	public int getPrepTimeMinutes() {
		return prepTimeMinutes;
	}

	private void setPrepTimeMinutes(int prepTimeMinutes) {
		this.prepTimeMinutes = prepTimeMinutes;
	}

	public List<String> getTags() {
		return tags;
	}

	private void setTags(List<String> tags) {
		this.tags = tags;
	}
}