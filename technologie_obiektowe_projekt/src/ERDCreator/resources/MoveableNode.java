package ERDCreator.resources;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MoveableNode implements Initializable {
    @FXML
    private HBox idHbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idHbox.setMinWidth(Config.WIDTH_TABLE);
    }
}
