package csc180.trotter.daniel.personalrecipebox.controller;

import csc180.trotter.daniel.personalrecipebox.model.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;


public class RecipeDetailController {

	@FXML
	private Label nameLabel;
	@FXML
	private Label categoryPrepLabel;
	@FXML
	private VBox ingredientsBox;
	@FXML
	private VBox stepsBox;

	private Recipe recipe;
	private Consumer<Recipe> onEditRequested;

	public void setOnEditRequested(Consumer<Recipe> callback) {
		this.onEditRequested = callback;
	}

	public void populate(Recipe recipe) {
		this.recipe = recipe;

		nameLabel.setText(recipe.getName());
		categoryPrepLabel.setText(recipe.getCategory() + "  •  " + recipe.getPrepTimeMinutes() + " min");

		ingredientsBox.getChildren().clear();
		for (String ingredient : recipe.getIngredients()) {
			ingredientsBox.getChildren().add(new Label("• " + ingredient));
		}

		stepsBox.getChildren().clear();
		List<String> steps = recipe.getSteps();
		for (int i = 0; i < steps.size(); i++) {
			Label stepLabel = new Label((i + 1) + ". " + steps.get(i));
			stepLabel.setWrapText(true);
			stepsBox.getChildren().add(stepLabel);
		}
	}

	@FXML
	protected void onCloseClick() {
		closeWindow();
	}

	@FXML
	protected void onEditClick() {
		if (onEditRequested != null) {
			onEditRequested.accept(recipe);
		}
		closeWindow();
	}

	private void closeWindow() {
		Stage stage = (Stage) nameLabel.getScene().getWindow();
		stage.close();
	}
}