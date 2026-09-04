package csc180.trotter.daniel.personalrecipebox.controller;

import csc180.trotter.daniel.personalrecipebox.model.IngredientRowData;
import csc180.trotter.daniel.personalrecipebox.model.ListRowData;
import csc180.trotter.daniel.personalrecipebox.model.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;


public class RecipeEditController {

	@FXML
	private TextField nameField;
	@FXML
	private TextField categoryField;
	@FXML
	private TextField prepTimeField;
	@FXML
	private VBox ingredientRowsContainer;
	@FXML
	private Button addIngredientButton;
	@FXML
	private VBox stepRowsContainer;
	@FXML
	private Button addStepButton;
	@FXML
	private Label nameErrorLabel;
	@FXML
	private Label prepTimeErrorLabel;
	@FXML
	private Label ingredientsErrorLabel;

	private ListRowData ingredientRowList;
	private ListRowData stepRowList;

	private static final ObservableList<String> UNIT_OPTIONS = FXCollections.observableArrayList(
			"", "cup", "cups", "tbsp", "tsp", "oz", "lb", "lbs", "g", "kg", "ml", "l", "pinch", "dash"
	);

	private Consumer<Recipe> onSaveCallback;

	/**
	 * <b>Quantity:</b>
	 * whole number, decimal, fraction, compound ("1 1/2"), or a range ("3-5")
	 */
	private static final Pattern QUANTITY_PATTERN = Pattern.compile(
			"^(?:\\d+\\s+)?\\d+(?:\\.\\d+|/\\d+)?(?:-\\d+)?$"
	);

	/**
	 * <b>Item name:</b>
	 * letters, spaces, hyphens, commas (e.g. "Butter, softened")
	 */
	private static final Pattern ITEM_NAME_PATTERN = Pattern.compile(
			"^[A-Za-z][A-Za-z\\-, ]*[A-Za-z]$"
	);

	/**
	 * <b>Optional note:</b>
	 * must be wrapped in parentheses if present, e.g. "(300g)"
	 */
	private static final Pattern NOTE_PATTERN = Pattern.compile(
			"^\\([A-Za-z0-9 ]+\\)$"
	);

	@FXML
	public void initialize() {
		ingredientRowList = new ListRowData(ingredientRowsContainer, this::buildIngredientRow);
		ingredientRowList.addRow();

		stepRowList = new ListRowData(stepRowsContainer, this::buildStepRow);
		stepRowList.setOnRowsChanged(v -> renumberSteps());
		stepRowList.addRow();
	}

	@FXML
	protected void onAddIngredientClick() {
		ingredientRowList.addRow();
	}

	@FXML
	protected void onAddStepClick() {
		stepRowList.addRow();
	}

	private HBox buildIngredientRow() {
		TextField quantityField = new TextField();
		quantityField.setPromptText("1 1/2");
		quantityField.setPrefWidth(70);

		ComboBox<String> unitBox = new ComboBox<>(UNIT_OPTIONS);
		unitBox.setPromptText("unit");

		TextField itemField = new TextField();
		itemField.setPromptText("sugar");
		itemField.setPrefWidth(140);

		TextField noteField = new TextField();
		noteField.setPromptText("(300g)");
		noteField.setPrefWidth(90);

		Button removeButton = new Button("\u2716");

		HBox row = new HBox(6.0, quantityField, unitBox, itemField, noteField, removeButton);
		row.setUserData(new TextField[]{quantityField, itemField, noteField});
		row.getProperties().put("unitBox", unitBox);

		removeButton.setOnAction(e -> ingredientRowList.removeRow(row));

		return row;
	}

	private HBox buildStepRow() {
		Label numberLabel = new Label();
		numberLabel.setPrefWidth(24);

		TextField stepField = new TextField();
		stepField.setPromptText("Mix ingredients");
		HBox.setHgrow(stepField, Priority.ALWAYS);

		Button removeButton = new Button("\u2716");

		HBox row = new HBox(6.0, numberLabel, stepField, removeButton);
		row.setUserData(stepField);

		removeButton.setOnAction(e -> stepRowList.removeRow(row));

		return row;
	}

	private void renumberSteps() {
		List<HBox> rows = stepRowList.getRows();
		for (int i = 0; i < rows.size(); i++) {
			Label numberLabel = (Label) rows.get(i).getChildren().get(0);
			numberLabel.setText((i + 1) + ".");
		}
	}

	public void setOnSaveCallback(Consumer<Recipe> callback) {
		this.onSaveCallback = callback;
	}

	public void populateFields(Recipe recipe) {
		nameField.setText(recipe.getName());
		categoryField.setText(recipe.getCategory());
		prepTimeField.setText(String.valueOf(recipe.getPrepTimeMinutes()));

		ingredientRowList.clear();
		for (String ingredientLine : recipe.getIngredients()) {
			HBox row = ingredientRowList.addRow();
			populateRowFromLine(row, ingredientLine);
		}

		stepRowList.clear();
		for (String step : recipe.getSteps()) {
			HBox row = stepRowList.addRow();
			TextField stepField = (TextField) row.getUserData();
			stepField.setText(step);
		}
	}

	private void populateRowFromLine(HBox row, String line) {
		TextField[] fields = (TextField[]) row.getUserData();
		@SuppressWarnings("unchecked")
		ComboBox<String> unitBox = (ComboBox<String>) row.getProperties().get("unitBox");

		String note = "";
		String remainder = line.trim();
		int parenStart = remainder.indexOf('(');
		if (parenStart != -1 && remainder.endsWith(")")) {
			note = remainder.substring(parenStart);
			remainder = remainder.substring(0, parenStart).trim();
		}

		String[] parts = remainder.split("\\s+");
		StringBuilder quantity = new StringBuilder();
		int i = 0;
		while (i < parts.length && parts[i].matches("\\d+(\\.\\d+|/\\d+)?(-\\d+)?")) {
			quantity.append(parts[i]).append(" ");
			i++;
		}

		String unit = "";
		if (i < parts.length && UNIT_OPTIONS.contains(parts[i].toLowerCase())) {
			unit = parts[i];
			i++;
		}

		StringBuilder itemName = new StringBuilder();
		while (i < parts.length) {
			itemName.append(parts[i]).append(" ");
			i++;
		}

		fields[0].setText(quantity.toString().trim());
		unitBox.setValue(unit);
		fields[1].setText(itemName.toString().trim());
		fields[2].setText(note);
	}

	@FXML
	protected void onSaveClick() {
		nameErrorLabel.setVisible(false);
		prepTimeErrorLabel.setVisible(false);
		ingredientsErrorLabel.setVisible(false);

		boolean isValid = true;

		String name = nameField.getText().trim();
		if (name.isEmpty()) {
			nameErrorLabel.setText("Recipe name is required.");
			nameErrorLabel.setVisible(true);
			isValid = false;
		}

		String prepTimeText = prepTimeField.getText().trim();
		int prepTime = 0;
		try {
			prepTime = Integer.parseInt(prepTimeText);
			if (prepTime <= 0) {
				prepTimeErrorLabel.setText("Prep time must be a positive number.");
				prepTimeErrorLabel.setVisible(true);
				isValid = false;
			}
		} catch (NumberFormatException e) {
			prepTimeErrorLabel.setText("Prep time must be a whole number.");
			prepTimeErrorLabel.setVisible(true);
			isValid = false;
		}

		List<String> ingredients = new ArrayList<>();
		boolean ingredientsValid = true;

		for (HBox row : ingredientRowList.getRows()) {
			TextField[] fields = (TextField[]) row.getUserData();
			TextField quantityField = fields[0];
			TextField itemField = fields[1];
			TextField noteField = fields[2];
			@SuppressWarnings("unchecked")
			ComboBox<String> unitBox = (ComboBox<String>) row.getProperties().get("unitBox");

			String quantity = quantityField.getText().trim();
			String unit = unitBox.getValue();
			String itemName = itemField.getText().trim();
			String note = noteField.getText().trim();

			if (!note.isEmpty() && !note.startsWith("(")) {
				note = "(" + note + ")";
			}
			if (!note.isEmpty() && !note.endsWith(")")) {
				note = note + ")";
			}

			if (quantity.isEmpty() && itemName.isEmpty()) {
				continue;
			}

			if (!QUANTITY_PATTERN.matcher(quantity).matches()) {
				ingredientsErrorLabel.setText("Quantity \"" + quantity + "\" isn't valid. Try \"2\" or \"1 1/2\".");
				ingredientsErrorLabel.setVisible(true);
				ingredientsValid = false;
				break;
			}
			if (!ITEM_NAME_PATTERN.matcher(itemName).matches()) {
				ingredientsErrorLabel.setText("Ingredient name \"" + itemName + "\" isn't valid. Use letters only.");
				ingredientsErrorLabel.setVisible(true);
				ingredientsValid = false;
				break;
			}
			if (!note.isEmpty() && !NOTE_PATTERN.matcher(note).matches()) {
				ingredientsErrorLabel.setText("Note should look like \"(300g)\" \u2014 got \"" + note + "\".");
				ingredientsErrorLabel.setVisible(true);
				ingredientsValid = false;
				break;
			}

			IngredientRowData rowData = new IngredientRowData(quantity, unit, itemName, note);
			ingredients.add(rowData.toIngredientLine());
		}

		if (!ingredientsValid || ingredients.isEmpty()) {
			if (ingredients.isEmpty() && ingredientsValid) {
				ingredientsErrorLabel.setText("Add at least one ingredient.");
				ingredientsErrorLabel.setVisible(true);
			}
			isValid = false;
		}

		if (!isValid) {
			return;
		}

		String category = categoryField.getText().trim();
		List<String> steps = new ArrayList<>();
		for (HBox row : stepRowList.getRows()) {
			TextField stepField = (TextField) row.getUserData();
			String text = stepField.getText().trim();
			if (!text.isEmpty()) {
				steps.add(text);
			}
		}

		Recipe recipe = new Recipe(name, ingredients, steps, category, prepTime);

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
			Scene newScene = new Scene(root, 585, 781);
			newScene.getStylesheets().add(
					getClass().getResource("/csc180/trotter/daniel/personalrecipebox/recipe-box-theme.css").toExternalForm()
			);
			stage.setScene(newScene);
		} catch (IOException e) {
			System.err.println("Failed to return to list view: " + e.getMessage());
		}
	}

}
