package SQLCreator;

import ERDCreator.Line.LineConnection;
import ERDCreator.resources.XTableView;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
            }//else if(lineConnection.getConnectionType().equals("dziedziczenie")){
                /*if(textArea.getText().contains("CREATE TABLE "+tableSecondName.getText())){
                    //query = "";
                    String textAreaString = textArea.getText();
                    String [] splittedTextArea = textAreaString.split("CREATE TABLE "+tableSecondName.getText()+" \\(");
                    String [] splittedPartTwo = splittedTextArea[1].split("\\);");
                    StringBuilder newStringBuilder = new StringBuilder(",\n");
                    List<TableModel> tableModels = ((XTableView)(lineConnection.getTableSecond().getChildren().get(1))).getItems();
                    for(TableModel tableModel : tableModels){
                        if(!tableModel.isPrimaryKey()&&!tableModel.isForeignKey()){
                            newStringBuilder.append(tableModel.getId() + " " + tableModel.getType());
                            if (tableModel.isUnique()) newStringBuilder.append(" UNIQUE");
                            if (tableModel.isNotNull()) newStringBuilder.append(" NOT NULL");
                            if (!tableModel.equals(tableModels.get(tableModels.size() - 1))) newStringBuilder.append(",\n");
                        }
                    }
                    StringBuilder oldString = new StringBuilder(splittedPartTwo[0]);
                    oldString.append(newStringBuilder.toString());
                    query = textAreaString.replace(splittedPartTwo[0],oldString.toString());
                    //textArea.setText(textAreaString.replace(splittedPartTwo[0],oldString.toString()));
                    
                }*/
            //}
            else query="";
            queryBuilder.append(query);
            queryBuilder.append("\n\n");
        });
        return queryBuilder.toString();
    }
}
