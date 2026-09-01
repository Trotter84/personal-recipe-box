package csc180.trotter.daniel.personalrecipebox.storage;

import csc180.trotter.daniel.personalrecipebox.model.Recipe;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class RecipeStorage {

	private static final String FILE_PATH = "src/data/recipes.dat";

	public static void saveRecipes(List<Recipe> recipes) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
			out.writeObject(recipes);
		} catch (IOException e) {
			System.err.println("Failed to save recipes: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Recipe> loadRecipes() {
		File file = new File(FILE_PATH);
		if (!file.exists()) {
			return new ArrayList<>();
		}

		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
			return (List<Recipe>) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Failed to load recipes, starting fresh: " + e.getMessage());
			return new ArrayList<>();
		}
	}
}