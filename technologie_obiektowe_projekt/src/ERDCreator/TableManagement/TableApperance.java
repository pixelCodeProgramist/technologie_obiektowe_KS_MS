package ERDCreator.TableManagement;

import ERDCreator.resources.XTableView;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class TableApperance {
    private Optional<Model> chosenModel;
    private int classNumber = 0;
    private int tabelNumber = 0;

    protected void setChosenModel(Optional<Model> chosenModel){
        this.chosenModel = chosenModel;
    }

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

    protected void setLabelTextIfClicked(MoveableNodeModel e,AnchorPane workingPane) {
        e.getLabel().setOnMouseClicked(event -> {
            TextField textField = new TextField();
            e.gethBox().getChildren().add(textField);
            Label label = new Label();
            e.gethBox().getChildren().remove(e.getLabel());

            workingPane.setOnMouseClicked(ec -> {
                label.setText(textField.getText());
                String helpString = label.getText().replaceAll("\\s", "");
                if (!label.getText().trim().equals("") && helpString.equals(label.getText())) {
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
                if (cellEditEvent.getTablePosition().getColumn() == 0) tableModel.setId((String)
                        cellEditEvent.getNewValue());
                if (cellEditEvent.getTablePosition().getColumn() == 1) tableModel.setType((String)
                        cellEditEvent.getNewValue());

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
