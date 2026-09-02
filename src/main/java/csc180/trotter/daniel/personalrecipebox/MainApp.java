package csc180.trotter.daniel.personalrecipebox;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApp extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("recipe-list-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 500, 740);
		scene.getStylesheets().add(
				MainApp.class.getResource("recipe-box-theme.css").toExternalForm()
		);
		stage.setTitle("Recipe Box");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
