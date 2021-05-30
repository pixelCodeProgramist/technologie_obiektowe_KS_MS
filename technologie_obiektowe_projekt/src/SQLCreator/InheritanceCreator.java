package SQLCreator;

import ERDCreator.Line.LineConnection;
import ERDCreator.resources.XTableView;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import models.TableModel;

import java.util.ArrayList;
import java.util.List;

public class InheritanceCreator {
    private List<LineConnection> lineConnections;
    private ArrayList<NodeSql> nodeSqls;
    private String oldCode;

    public InheritanceCreator(List<LineConnection> lineConnections, ArrayList<NodeSql> nodeSqls) {
        this.lineConnections = lineConnections;
        this.nodeSqls = nodeSqls;
        createOldCode();
    }

    public void createOldCode(){
        oldCode = "";
        for(NodeSql nodeSql: nodeSqls){
            oldCode+=nodeSql.getHeader()+"\n"+nodeSql.getBody()+"\n";
        }
    }

    private Label findLabelAfterAnchorPane(AnchorPane anchorPane) {
        HBox hBox = (HBox) anchorPane.getChildren().get(0);
        return (Label) hBox.getChildren().get(0);
    }

    public void buildSQL() {
        StringBuilder queryBuilder = new StringBuilder();
        lineConnections.forEach(lineConnection -> {
            Label tableSecondName = findLabelAfterAnchorPane(lineConnection.getTableSecond());
            Label tableFirstName = findLabelAfterAnchorPane(lineConnection.getTableFirst());
            String textAreaString = oldCode;

            if (lineConnection.getConnectionType().equals("dziedziczenie")) {
                if (oldCode.contains("CREATE TABLE " + tableSecondName.getText())) {
                    String[] splittedTextArea = textAreaString.split("CREATE TABLE " + tableSecondName.getText() + " \\(");
                    String[] splittedPartTwo = splittedTextArea[1].split("\\);");
                    StringBuilder newStringBuilder = new StringBuilder(",\n");
                    Pair<List<TableModel>,StringBuilder> pair = getAllTableModelFromTableWithoutKeys(lineConnection,
                            newStringBuilder);
                    List<TableModel> tableModels = pair.getKey();
                    newStringBuilder = pair.getValue();
                    if (newStringBuilder.toString().trim().length() > 1) {
                        if (oldCode.contains("CREATE TABLE " + tableFirstName.getText())) {
                            NodeSql nodeSql = new NodeSql();
                            splittedTextArea = textAreaString.split("CREATE TABLE " + tableFirstName.getText() + " \\(");
                            splittedPartTwo = splittedTextArea[1].split("\\);");
                            StringBuilder stringBuilderSecond = new StringBuilder(splittedPartTwo[0]);
                            stringBuilderSecond.append(newStringBuilder.toString());
                            textAreaString = textAreaString.replace(splittedPartTwo[0], stringBuilderSecond.toString());
                            nodeSql.setHeaderAndBody(textAreaString);
                            nodeSqls.add(nodeSql);
                            createOldCode();
                        }
                    }
                    if (!oldCode.contains("CREATE TABLE " + tableFirstName.getText())) {
                        createNewEntity(tableFirstName,lineConnection);
                    }
                }else {
                    createNewEntity(tableFirstName,lineConnection);
                }
            }
        });
    }

    private void createNewEntity(Label tableFirstName, LineConnection lineConnection){
        StringBuilder query = new StringBuilder("");
        NodeSql nodeSql = new NodeSql();
        query.append("CREATE TABLE " + tableFirstName.getText() + " (\n");
        List<TableModel> tableModels = ((XTableView) (lineConnection.getTableFirst().getChildren().get(1))).getItems();
        tableModels.forEach(row -> {
            query.append(row.getId() + " " + row.getType());
            if (row.isPrimaryKey()) query.append(" NOT NULL PRIMARY KEY");
            if (!row.isPrimaryKey()) {
                if (row.isUnique()) query.append(" UNIQUE");
                if (row.isNotNull()) query.append(" NOT NULL");
            }
            if (!row.equals(tableModels.get(tableModels.size() - 1))) query.append(",\n");
        });
        StringBuilder newStringBuilder = new StringBuilder(",\n");
        Pair<List<TableModel>,StringBuilder> pair = getAllTableModelFromTableWithoutKeys(lineConnection,
                newStringBuilder);
        newStringBuilder = pair.getValue();
        if(newStringBuilder.toString().trim().length()>1){
            query.append(newStringBuilder.toString());
        }
        query.append(");\n\n");
        nodeSql.setHeaderAndBody(query.toString());
        nodeSqls.add(nodeSql);
    }

    private Pair<List<TableModel>,StringBuilder> getAllTableModelFromTableWithoutKeys(LineConnection lineConnection,
                                                                                      StringBuilder newStringBuilder){

        List<TableModel> tableModels = ((XTableView) (lineConnection.getTableSecond().getChildren().get(1))).getItems();
        for (TableModel tableModel : tableModels) {
            if (!tableModel.isPrimaryKey() && tableModel.getPrimaryForeignNoneKey() == null) {
                newStringBuilder.append(tableModel.getId() + " " + tableModel.getType());
                if (tableModel.isUnique()) newStringBuilder.append(" UNIQUE");
                if (tableModel.isNotNull()) newStringBuilder.append(" NOT NULL");
                if (!tableModel.equals(tableModels.get(tableModels.size() - 1)))
                    newStringBuilder.append(",\n");
            }
        }
        return new Pair<>(tableModels,newStringBuilder);
    }


}
