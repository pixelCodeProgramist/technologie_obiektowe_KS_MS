package sample;

import Config.Configuration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/sample.fxml"));
        Controller controller = new Controller();
        loader.setController(controller);
        Parent root = loader.load();
        primaryStage.setTitle("Kreator diagramów");
        primaryStage.setScene(new Scene(root, Configuration.WIDTH, Configuration.HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
