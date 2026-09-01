package csc180.trotter.daniel.personalrecipebox.controller;

import csc180.trotter.daniel.personalrecipebox.model.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;


public class RecipeEditController {

	@FXML
	private TextField nameField;
	@FXML
	private TextField categoryField;
	@FXML
	private TextField prepTimeField;
	@FXML
	private TextArea ingredientsField;
	@FXML
	private TextArea stepsField;
	@FXML
	private Label nameErrorLabel;
	@FXML
	private Label prepTimeErrorLabel;
	@FXML
	private Label ingredientsErrorLabel;

	private Consumer<Recipe> onSaveCallback;

	public void setOnSaveCallback(Consumer<Recipe> callback) {
		this.onSaveCallback = callback;
	}

	public void populateFields(Recipe recipe) {
		nameField.setText(recipe.getName());
		categoryField.setText(recipe.getCategory());
		prepTimeField.setText(String.valueOf(recipe.getPrepTimeMinutes()));
		ingredientsField.setText(String.join("\n", recipe.getIngredients()));
		stepsField.setText(String.join("\n", recipe.getSteps()));
	}

	@FXML
	protected void onSaveClick() {
		String name = nameField.getText();
		String category = categoryField.getText();
		int prepTime;
		try {
			prepTime = Integer.parseInt(prepTimeField.getText().trim());
		} catch (NumberFormatException e) {
			prepTimeErrorLabel.setText("Prep time must be a number");
			prepTimeErrorLabel.setVisible(true);
			return;
		}

		Recipe recipe = new Recipe(
				name,
				Arrays.asList(ingredientsField.getText().split("\n")),
				Arrays.asList(stepsField.getText().split("\n")),
				category,
				prepTime
		);

		if (onSaveCallback != null) {
			onSaveCallback.accept(recipe);
		}
		goBackToList();
	}

	@FXML
	protected void onCancelClick() {
		goBackToList();
	}

	private void goBackToList() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/csc180/trotter/daniel/personalrecipebox/recipe-list-view.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage) nameField.getScene().getWindow();
			stage.setScene(new Scene(root, 480, 640));
		} catch (IOException e) {
			System.err.println("Failed to return to list view: " + e.getMessage());
		}
	}

}
