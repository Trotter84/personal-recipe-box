module csc180.trotter.daniel.personalrecipebox {
	requires javafx.controls;
	requires javafx.fxml;


	opens csc180.trotter.daniel.personalrecipebox to javafx.fxml;
	exports csc180.trotter.daniel.personalrecipebox;
	exports csc180.trotter.daniel.personalrecipebox.controller;
	opens csc180.trotter.daniel.personalrecipebox.controller to javafx.fxml;
}