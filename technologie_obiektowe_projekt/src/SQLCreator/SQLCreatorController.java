package SQLCreator;

import Config.Configuration;
import ERDCreator.ERDCreatorController;
import ERDCreator.Line.LineConnection;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class SQLCreatorController implements Initializable {

    @FXML
    private TextArea textAreaID;
    private ArrayList<NodeSplitter> nodeSplitters;
    private Set<MoveableNodeModel> nodes;
    private List<LineConnection> lineConnections;
    public SQLCreatorController(Set<MoveableNodeModel> nodes, List<LineConnection> lineConnections) {
        this.nodes = nodes;
        this.lineConnections = lineConnections;
        this.nodeSplitters = new ArrayList<>();
    }

    @FXML
    public void backToWorkspace(MouseEvent mouseEvent) throws IOException {
        Configuration configuration = new Configuration();
        configuration.changeScene("../ERDCreator/ERDCreator.fxml", mouseEvent, new ERDCreatorController(nodes,lineConnections));
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
        InheritanceCreator inheritanceCreator = new InheritanceCreator(lineConnections,textAreaID);
        inheritanceCreator.buildSQL();
        ConnectionSQLCreator connectionSQLCreator = new ConnectionSQLCreator(lineConnections);
        this.textAreaID.setText(this.textAreaID.getText() +connectionSQLCreator.buildSQL());


    }


}
