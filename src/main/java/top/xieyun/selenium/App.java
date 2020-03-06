package top.xieyun.selenium;

import com.jfoenix.controls.JFXButton;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;;

/**
 * Hello world!
 *
 */
@SuppressWarnings("restriction")
public class App extends Application {
	@Override
	public void start(Stage primaryStage) {
		Application.setUserAgentStylesheet("\\jfoenix.css");
		JFXButton jfoenixButton = new JFXButton("JFoenix Button");
		JFXButton button = new JFXButton("Raised Button".toUpperCase());
		button.getStyleClass().add("button-raised");

		Scene scene = new Scene(jfoenixButton, 200, 250);
		primaryStage.setTitle("MyJavaFX"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	// 在IDE中需显式调用
	public static void main(String[] args) {
		Application.launch(args);
	}
}
