package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../ERDCreator/ERDCreator.fxml"));
        primaryStage.setTitle("Kreator diagramów");
        primaryStage.setScene(new Scene(root, 1000, 780));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
