package ERDCreator.TableManagement;

import ERDCreator.resources.XTableView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import models.Model;
import models.MoveableNodeModel;
import models.TableModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class TableApperance {
    private Optional<Model> chosenModel;
    private int classNumber = 0;
    private int tabelNumber = 0;
    protected void setChosenModel(Optional<Model> chosenModel){
        this.chosenModel = chosenModel;
    }
    private List<String> prohibitedTableNames = new ArrayList<>(Arrays.asList("CREATE","DISTINCT","INSERT","INTO","SELECT","TABLE","*","VALUES","NULL","IS","DROP","ALTER","CONSTRAINT"));
    private List<String> availableTypeNamesWithBracket = new ArrayList<>(Arrays.asList("INT","VARCHAR","TINYINT","SMALLINT","MEDIUMINT","BIGINT","DECIMAL","FLOAT","DOUBLE","DATE","REAL","BIT","CHAR","BINARY","VARBINARY","ENUM","SET","GEOMETRY","POINT","LINESTRING","POLYGON","MULTIPOINT","MULTILINESTRING","MULTIPOLYGON","GEOMETRYCOLLECTION","JSOM"));
    private List<String> availableTypeNamesWithoutBracket = new ArrayList<>(Arrays.asList("TEXT","DATE","BOOLEAN","SERIAL","DATETIME","TIMESTAMP","TIME","YEAR","TINYTEXT","MEDIUMTEXT","LONGTEXT","TINYBLOB","BLOB","MEDIUMBLOB","LONGBLOB"));
    protected String getFirstTextToLabel() {
        if (chosenModel.get().getDescription().equalsIgnoreCase("klasa")) {
            classNumber++;
            return "Klasa" + classNumber;
        }
        if (chosenModel.get().getDescription().equalsIgnoreCase("tabela")) {
            tabelNumber++;
            return "Tabela" + tabelNumber;
        }
        return null;
    }

    protected Background getColorToMovableNode() {
        if (chosenModel.get().getDescription().equalsIgnoreCase("klasa"))
            return new Background(new BackgroundFill(Color.GRAY,
                    CornerRadii.EMPTY,
                    Insets.EMPTY));
        if (chosenModel.get().getDescription().equalsIgnoreCase("tabela")) {
            return new Background(new BackgroundFill(Color.LIGHTBLUE,
                    CornerRadii.EMPTY,
                    Insets.EMPTY));
        }
        return null;
    }


    protected void setLabelTextIfClicked(MoveableNodeModel e, ScrollPane workingPane) {
        e.getLabel().setOnMouseClicked(event -> {
            TextField textField = new TextField();
            e.gethBox().getChildren().add(textField);
            Label label = new Label();
            e.gethBox().getChildren().remove(e.getLabel());

            workingPane.setOnMouseClicked(ec -> {
                label.setText(textField.getText());
                String helpString = label.getText().replaceAll("\\s", "");
                if (!label.getText().trim().equals("")
                        && helpString.equals(label.getText())
                        && !prohibitedTableNames.contains(helpString.toUpperCase())
                    ) {
                    if (!e.gethBox().getChildren().contains(label)) {
                        e.gethBox().getChildren().remove(textField);
                        label.setMinWidth(label.getText().length() * 6.5);
                        e.getxTableView().setMinWidth(label.getText().length() * 6.5);
                        e.gethBox().getChildren().add(label);
                        e.setLabel(label);
                    }
                }
            });
        });
    }

    protected void setResize(MoveableNodeModel m) {
        XTableView xTableView = m.getxTableView();
        AtomicReference<Double> max = new AtomicReference<>((double) 10);
        xTableView.getColumns().stream().forEach(column -> {
            TableColumn tableColumn = (TableColumn) column;
            tableColumn.setOnEditCommit(comm -> {
                double tableWidth = 0;
                TableColumn.CellEditEvent cellEditEvent = (TableColumn.CellEditEvent) comm;
                TableModel tableModel = (TableModel)
                        cellEditEvent.getTableView().getItems().get(cellEditEvent.getTablePosition().getRow());
                if (cellEditEvent.getTablePosition().getColumn() == 0)
                    tableModel.setId((String) cellEditEvent.getNewValue());
                if (cellEditEvent.getTablePosition().getColumn() == 1) {
                    String typeString = (String) cellEditEvent.getNewValue();
                    if(availableTypeNamesWithoutBracket.contains(typeString.toUpperCase())) {
                        tableModel.setType(typeString);
                    }else {
                        availableTypeNamesWithBracket.forEach(str->{
                            if(typeString.toUpperCase().startsWith(str)) {
                                String[] arrStr = typeString.split("\\(");
                                String[] arrStr2 = typeString.split("\\)");
                                if(arrStr2.length==1&&arrStr.length==2&typeString.endsWith(")")){
                                    arrStr[1] = arrStr[1].substring(0,arrStr[1].length()-1);
                                    if(arrStr[1].matches("\\d+")&&!arrStr[1].startsWith("0")) tableModel.setType((String) cellEditEvent.getNewValue());
                                    else tableModel.setType((String) cellEditEvent.getOldValue());
                                }else {
                                    if(arrStr.length==1&&arrStr2.length==1) tableModel.setType(typeString);
                                    else tableModel.setType((String) cellEditEvent.getOldValue());
                                }
                            }
                            else {
                                tableModel.setType((String) cellEditEvent.getOldValue());
                            }
                        });
                    }
                }

                for (int i = 0; i < xTableView.getItems().size(); i++) {
                    if (!tableColumn.getCellData(i).equals("")) {
                        double size = 12.5 *
                                tableColumn.getCellData(i).toString().length();
                        if (size > max.get() && !tableColumn.getCellData(i).getClass().equals(ImageView.class)) {
                            max.set(size);
                        }
                        if (tableColumn.getCellData(i).getClass().equals(ImageView.class)) {
                            max.set(tableColumn.getMinWidth());
                        }
                        tableColumn.setMinWidth(max.get());
                        tableColumn.setMaxWidth(max.get());
                    }
                }
                for (int j = 0; j < xTableView.getColumns().size(); j++) {
                    tableWidth += ((TableColumn) xTableView.getColumns().get(j)).getWidth();
                }
                xTableView.setMinWidth(tableWidth + 10);
                xTableView.setMaxWidth(tableWidth + 10);
                if ((tableWidth + 10) > m.gethBox().getWidth()) {
                    m.gethBox().setMinWidth(tableWidth + 10);
                    m.gethBox().setMaxWidth(tableWidth + 10);
                } else {
                    xTableView.setMinWidth(m.gethBox().getMinWidth());
                    xTableView.setMaxWidth(m.gethBox().getMaxWidth());
                }

            });

        });
    }
}
