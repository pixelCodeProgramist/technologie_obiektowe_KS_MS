package SQLCreator;

import Config.Configuration;
import ERDCreator.ERDCreatorController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import models.MoveableNodeModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class SQLCreatorController implements Initializable {

    @FXML
    private TextArea textAreaID;
    private ArrayList<NodeSplitter> nodeSplitters;
    private Set<MoveableNodeModel> nodes;
    public SQLCreatorController(Set<MoveableNodeModel> nodes) {
        this.nodes = nodes;
        this.nodeSplitters = new ArrayList<>();
    }

    @FXML
    public void backToWorkspace(MouseEvent mouseEvent) throws IOException {
        Configuration configuration = new Configuration();
        configuration.changeScene("../ERDCreator/ERDCreator.fxml", mouseEvent, new ERDCreatorController(nodes));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.textAreaID.setEditable(false);
        buildListNodeSpitters();
    }

    private void buildListNodeSpitters(){
        this.nodes.forEach(node ->{
            NodeSplitter nodeSplitter = new NodeSplitter(node);
            this.textAreaID.setText(this.textAreaID.getText()+nodeSplitter.buildQuery());
        });
    }


}
