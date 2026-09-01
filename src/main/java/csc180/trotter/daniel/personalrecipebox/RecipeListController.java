package csc180.trotter.daniel.personalrecipebox;

import csc180.trotter.daniel.personalrecipebox.model.Recipe;
import csc180.trotter.daniel.personalrecipebox.storage.RecipeStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

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
				openDetailView(selected);
			}
		});
	}

	private void updateEmptyState() {
		boolean isEmpty = recipeData.isEmpty();
		emptyStateLabel.setVisible(isEmpty);
		recipeListView.setVisible(!isEmpty);
	}

	private void openDetailView(Recipe recipe) {
		System.out.println("Clicked: " + recipe.getName());
	}

	@FXML
	protected void onAddRecipeClick() {
	}
}