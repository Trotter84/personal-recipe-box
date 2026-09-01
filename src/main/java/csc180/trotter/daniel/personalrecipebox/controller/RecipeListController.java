package csc180.trotter.daniel.personalrecipebox.controller;

import csc180.trotter.daniel.personalrecipebox.model.Recipe;
import csc180.trotter.daniel.personalrecipebox.storage.RecipeStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class RecipeListController {

	@FXML
	private ListView<Recipe> recipeListView;
	@FXML
	private Label emptyStateLabel;
	@FXML
	private Button addRecipeButton;

	private ObservableList<Recipe> recipeData;

	@FXML
	public void initialize() {
		List<Recipe> loaded = RecipeStorage.loadRecipes();
		recipeData = FXCollections.observableArrayList(loaded);
		recipeListView.setItems(recipeData);

		recipeListView.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
			@Override
			protected void updateItem(Recipe recipe, boolean empty) {
				super.updateItem(recipe, empty);
				setText(empty || recipe == null ? null : recipe.getName());
			}
		});

		updateEmptyState();

		recipeListView.setOnMouseClicked(event -> {
			Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
			if (selected != null) {
				openEditView(selected);
			}
		});
	}

	private void updateEmptyState() {
		boolean isEmpty = recipeData.isEmpty();
		emptyStateLabel.setVisible(isEmpty);
		recipeListView.setVisible(!isEmpty);
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
				}
				recipeData.add(savedRecipe);
				RecipeStorage.saveRecipes(recipeData);
				updateEmptyState();
			});

			if (recipeToEdit != null) {
				controller.populateFields(recipeToEdit);
			}

			Stage stage = (Stage) addRecipeButton.getScene().getWindow();
			stage.setScene(new Scene(root, 480, 640));
		} catch (IOException e) {
			System.err.println("Failed to open edit view: " + e.getMessage());
		}
	}
}