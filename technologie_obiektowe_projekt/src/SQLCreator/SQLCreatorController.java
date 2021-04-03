package SQLCreator;

import Config.Configuration;
import ERDCreator.ERDCreatorController;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import models.MoveableNodeModel;

import java.io.IOException;
import java.util.Set;

public class SQLCreatorController{

    private Set<MoveableNodeModel> nodes;
    public SQLCreatorController(Set<MoveableNodeModel> nodes) {
        this.nodes = nodes;
    }

    @FXML
    public void backToWorkspace(MouseEvent mouseEvent) throws IOException {

        Configuration configuration = new Configuration();
        configuration.changeScene("../ERDCreator/ERDCreator.fxml", mouseEvent, new ERDCreatorController(nodes));
    }
}
