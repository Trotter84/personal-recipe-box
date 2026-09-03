package csc180.trotter.daniel.personalrecipebox.controller;

import csc180.trotter.daniel.personalrecipebox.model.Recipe;
import csc180.trotter.daniel.personalrecipebox.storage.RecipeStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RecipeListController {

	@FXML
	private ComboBox<String> categoryFilterBox;
	@FXML
	private ListView<Recipe> recipeListView;
	@FXML
	private Label emptyStateLabel;
	@FXML
	private Button addRecipeButton;

	private List<Recipe> allRecipes;
	private ObservableList<Recipe> recipeData;

	@FXML
	public void initialize() {
		List<Recipe> loaded = RecipeStorage.loadRecipes();
		allRecipes = new ArrayList<>(loaded);
		recipeData = FXCollections.observableArrayList(loaded);
		recipeListView.setItems(recipeData);

		recipeListView.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
			@Override
			protected void updateItem(Recipe recipe, boolean empty) {
				super.updateItem(recipe, empty);
				setText(empty || recipe == null ? null : recipe.getName());
			}
		});

		populateCategoryFilter();
		updateEmptyState();

		recipeListView.setOnMouseClicked(event -> {
			Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
			if (selected != null) {
				openEditView(selected);
			}
		});

		categoryFilterBox.setOnAction(event -> applyCategoryFilter());
	}

	/**
	 * <b>Lambda</b>
	 * maps each recipe to its category, collects distinct values, sorts them.
	 */
	private void populateCategoryFilter() {
		List<String> categories = allRecipes.stream()
											.map(Recipe::getCategory)
											.filter(c -> c != null && !c.isBlank())
											.distinct()
											.sorted()
											.collect(java.util.stream.Collectors.toList());
		categoryFilterBox.getItems().setAll(categories);
	}

	private void updateEmptyState() {
		boolean isEmpty = recipeData.isEmpty();
		emptyStateLabel.setVisible(isEmpty);
		recipeListView.setVisible(!isEmpty);
	}

	/**
	 * <b>Lambda</b>
	 * keeps only recipes that match the selected category.
	 */
	private void applyCategoryFilter() {
		String selected = categoryFilterBox.getValue();
		if (selected == null) {
			recipeData.setAll(allRecipes);
			return;
		}
		List<Recipe> filtered = allRecipes.stream().filter(r -> selected.equals(r.getCategory())).collect(java.util.stream.Collectors.toList());
		recipeData.setAll(filtered);
		updateEmptyState();
	}

	@FXML
	protected void onClearFilterClick() {
		categoryFilterBox.setValue(null);
		recipeData.setAll(allRecipes);
		updateEmptyState();
	}

	/**
	 * <b>Lambda</b>
	 * sorts by name (case-insensitive)
	 */
	@FXML
	protected void onSortByNameClick() {
		recipeData.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
	}

	/**
	 * <b>Lambda</b>
	 * sorts by prep time, <i>ASC</i>
	 */
	@FXML
	protected void onSortByPrepTimeClick() {
		recipeData.sort((a, b) -> Integer.compare(a.getPrepTimeMinutes(), b.getPrepTimeMinutes()));
	}

	@FXML
	protected void onAddRecipeClick() {
		openEditView(null);
	}

	private void openEditView(Recipe recipeToEdit) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/csc180/trotter/daniel/personalrecipebox/recipe-edit-view.fxml"));
			javafx.scene.Parent root = loader.load();

			RecipeEditController controller = loader.getController();
			controller.setOnSaveCallback(savedRecipe -> {
				if (recipeToEdit != null) {
					recipeData.remove(recipeToEdit);
					allRecipes.remove(recipeToEdit);
				}
				recipeData.add(savedRecipe);
				allRecipes.add(savedRecipe);
				RecipeStorage.saveRecipes(recipeData);
				populateCategoryFilter();
				updateEmptyState();
			});

			if (recipeToEdit != null) {
				controller.populateFields(recipeToEdit);
			}

			Stage stage = (Stage) addRecipeButton.getScene().getWindow();
			Scene newScene = new Scene(root, 600, 840);
			newScene.getStylesheets().add(
					getClass().getResource("/csc180/trotter/daniel/personalrecipebox/recipe-box-theme.css").toExternalForm()
			);
			stage.setScene(newScene);
		} catch (IOException e) {
			System.err.println("Failed to open edit view: " + e.getMessage());
		}
	}
}