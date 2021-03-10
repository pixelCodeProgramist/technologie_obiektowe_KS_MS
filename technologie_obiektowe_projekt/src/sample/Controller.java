package sample;

import Config.Configuration;
import ERDCreator.ERDCreatorController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button openERDCreatorButton;

    @FXML
    private Pane samplePane;

    @FXML
    public void openERDCreator(MouseEvent mouseEvent) throws  IOException{
        Configuration configuration = new Configuration();
        configuration.changeScene("../ERDCreator/ERDCreator.fxml",mouseEvent,new ERDCreatorController());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
