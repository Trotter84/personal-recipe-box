import csc180.trotter.daniel.personalrecipebox.model.Recipe;
import csc180.trotter.daniel.personalrecipebox.storage.RecipeStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StorageTest {
	public static void main(String[] args) {
		// 1. Test loading with no file present yet
		List<Recipe> loaded = RecipeStorage.loadRecipes();
		System.out.println("First load (should be empty): " + loaded.size() + " recipes");

		// 2. Create some test data
		List<Recipe> recipes = new ArrayList<>();
		recipes.add(new Recipe(
				"Pancakes",
				Arrays.asList("2 cups flour", "1 cup milk", "2 eggs"),
				Arrays.asList("Mix ingredients", "Cook on griddle"),
				"Breakfast",
				20
		));

		// 3. Save it
		RecipeStorage.saveRecipes(recipes);
		System.out.println("Saved " + recipes.size() + " recipe(s).");

		// 4. Load it back in a fresh call
		List<Recipe> reloaded = RecipeStorage.loadRecipes();
		System.out.println("Reloaded " + reloaded.size() + " recipe(s):");
		for (Recipe r : reloaded) {
			System.out.println(" - " + r.getName() + " (" + r.getCategory() + ", " + r.getPrepTimeMinutes() + " min)");
		}
	}
}