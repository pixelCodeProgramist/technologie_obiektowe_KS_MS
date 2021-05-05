package SQLCreator;

import ERDCreator.Line.LineConnection;
import ERDCreator.resources.XTableView;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import models.TableModel;

import java.util.List;

public class InheritanceCreator {
    private List<LineConnection> lineConnections;
    private TextArea textArea;

    public InheritanceCreator(List<LineConnection> lineConnections, TextArea textAreaID) {
        this.lineConnections = lineConnections;
        this.textArea = textAreaID;
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
            String textAreaString = textArea.getText();

            if (lineConnection.getConnectionType().equals("dziedziczenie")) {
                if (textArea.getText().contains("CREATE TABLE " + tableSecondName.getText())) {
                    String[] splittedTextArea = textAreaString.split("CREATE TABLE " + tableSecondName.getText() + " \\(");
                    String[] splittedPartTwo = splittedTextArea[1].split("\\);");
                    StringBuilder newStringBuilder = new StringBuilder(",\n");
                    Pair<List<TableModel>,StringBuilder> pair = getAllTableModelFromTableWithoutKeys(lineConnection,
                            newStringBuilder);
                    List<TableModel> tableModels = pair.getKey();
                    newStringBuilder = pair.getValue();
                    if (newStringBuilder.toString().trim().length() > 1) {
                        if (textArea.getText().contains("CREATE TABLE " + tableFirstName.getText())) {
                            splittedTextArea = textAreaString.split("CREATE TABLE " + tableFirstName.getText() + " \\(");
                            splittedPartTwo = splittedTextArea[1].split("\\);");
                            StringBuilder stringBuilderSecond = new StringBuilder(splittedPartTwo[0]);
                            stringBuilderSecond.append(newStringBuilder.toString());
                            textAreaString = textAreaString.replace(splittedPartTwo[0], stringBuilderSecond.toString());
                            textArea.setText(textAreaString);
                        }
                    }
                }else {
                    StringBuilder query = new StringBuilder("");
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
                    textArea.setText(textArea.getText()+query.toString());
                }
            }
        });
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
