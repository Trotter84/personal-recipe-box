package csc180.trotter.daniel.personalrecipebox;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApp extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		Image backgroundImage = new Image("file:recipe-book-background-cat.jpg");
		BackgroundImage background = new BackgroundImage(
				backgroundImage,
				BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT
		);
		Background backgroundImg = new Background(background);


		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("recipe-list-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 584, 781);
		StackPane root = new StackPane();
		root.setBackground(backgroundImg);
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
