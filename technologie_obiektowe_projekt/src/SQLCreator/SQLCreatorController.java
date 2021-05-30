package SQLCreator;

import Config.Configuration;
import ERDCreator.ERDCreatorController;
import ERDCreator.Line.LineConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
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
    private ArrayList<NodeSql> nodeSqls;
    public SQLCreatorController(Set<MoveableNodeModel> nodes, List<LineConnection> lineConnections) {
        this.nodes = nodes;
        this.lineConnections = lineConnections;
        this.nodeSplitters = new ArrayList<>();
        this.nodeSqls = new ArrayList<>();
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
            NodeSplitter nodeSplitter = new NodeSplitter(node,nodeSqls);
            this.textAreaID.setText(this.textAreaID.getText()+nodeSplitter.buildQuery());
        });
        InheritanceCreator inheritanceCreator = new InheritanceCreator(lineConnections,nodeSqls);
        inheritanceCreator.buildSQL();
        ConnectionSQLCreator connectionSQLCreator = new ConnectionSQLCreator(lineConnections);
        makeDistinctTables();
        StringBuilder stringBuilder = new StringBuilder();
        for(NodeSql nodeSql: nodeSqls)
            stringBuilder.append(nodeSql.getHeader()+"\n"+nodeSql.getBody()+"\n\n");
        this.textAreaID.setText(stringBuilder.toString() +connectionSQLCreator.buildSQL());
    }

    private void makeDistinctTables(){
        ArrayList<String> headers = new ArrayList<>();
        for(NodeSql n: nodeSqls){
            headers.add(n.getHeader());
        }
        int size = nodeSqls.size();
        for(int i=0;i<size;i++)
        {
            int index = headers.indexOf(nodeSqls.get(i).getHeader());
            int lastIndex = headers.lastIndexOf(nodeSqls.get(i).getHeader());
            if(lastIndex!=index)
            {
                nodeSqls.remove(index);
                headers.remove(index);
                size--;
            }
        }
    }

}
