package SQLCreator;

import ERDCreator.Line.LineConnection;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import models.Model;
import models.TableModel;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ConnectionSQLCreator {
    private List<LineConnection> lineConnections;


    public ConnectionSQLCreator(List<LineConnection> lineConnections) {
        this.lineConnections = lineConnections;
    }
    private Label findLabelAfterAnchorPane(AnchorPane anchorPane){
        HBox hBox = (HBox) anchorPane.getChildren().get(0);
        return  (Label) hBox.getChildren().get(0);
    }

    public String buildSQL(){
        StringBuilder queryBuilder = new StringBuilder();
        lineConnections.forEach(lineConnection->{
            Label tableSecondName = findLabelAfterAnchorPane(lineConnection.getTableSecond());
            Label tableFirstName = findLabelAfterAnchorPane(lineConnection.getTableFirst());
            TableModel connectedKey = lineConnection.getConnectedKey();

            TableModel foreignKey = lineConnection.getTableModel();
            String query;
            if (lineConnection.getConnectionType().equals("1 do *")||lineConnection.getConnectionType().equals("1 do 1"))
                query = "ALTER TABLE " +tableSecondName.getText() + " ADD CONSTRAINT fk"+ UUID.randomUUID().toString().replace("-","").substring(0,27) +
                    " FOREIGN KEY ("+foreignKey.getId()+") REFERENCES "+tableFirstName.getText()+"("+connectedKey.getId()+");";
            else if(lineConnection.getConnectionType().equals("* do *")){
                query = "CREATE TABLE " + tableFirstName.getText()+tableSecondName.getText()+" (\n"+
                        lineConnection.getConnectedKey().getId() +tableFirstName.getText() + " "+
                        lineConnection.getConnectedKey().getType() +
                        " NOT NULL REFERENCES " + tableFirstName.getText()+"("+lineConnection.getConnectedKey().getId()+")"+ ",\n"+
                        lineConnection.getConnectedKeySecond().getId()+tableSecondName.getText() + " "+
                        lineConnection.getConnectedKeySecond().getType() +
                        " NOT NULL REFERENCES " + tableSecondName.getText()+"("+lineConnection.getConnectedKeySecond().getId()
                        +")"+ ",\n"+
                        "PRIMARY KEY("+lineConnection.getConnectedKey().getId()+tableFirstName.getText()+","+
                        lineConnection.getConnectedKeySecond().getId()+tableSecondName.getText()+"));";
            }
            else query="";
            queryBuilder.append(query);
            queryBuilder.append("\n\n");
        });
        return queryBuilder.toString();
    }
}
