package csc180.trotter.daniel.personalrecipebox.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


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

	@FXML
	protected void onSaveClick() {
		System.out.println("Save clicked");
	}

	@FXML
	protected void onCancelClick() {
		System.out.println("Cancel clicked");
	}

}
