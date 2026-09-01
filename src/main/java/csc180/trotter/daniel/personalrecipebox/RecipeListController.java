package csc180.trotter.daniel.personalrecipebox;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class RecipeListController {

	@FXML
	private ListView<String> recipeListView;

	@FXML
	private Label emptyStateLabel;

	@FXML
	private Button addRecipeButton;

	@FXML
	protected void onAddRecipeClick() {

	}
}
