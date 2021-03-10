package Config;

import ERDCreator.ERDCreatorController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Configuration {
    public static int HEIGHT = 780;
    public static int WIDTH = 1000;
    public void changeScene(String fxmlString, Event event,Object o) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlString));
        loader.setController(o);
        Parent root = loader.load();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
