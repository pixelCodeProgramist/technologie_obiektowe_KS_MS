package ERDCreator.TableManagement;

import ERDCreator.resources.XTableView;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import models.Model;
import models.MoveableNodeModel;
import models.TableModel;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class TableApperance {
    private Optional<Model> chosenModel;
    private int classNumber = 0;
    private int tabelNumber = 0;
    protected boolean isLabelOfTableClicked = false;

    protected void setChosenModel(Optional<Model> chosenModel) {
        this.chosenModel = chosenModel;
    }

    private List<String> prohibitedTableNames = new ArrayList<>(Arrays.asList("CREATE", "DISTINCT", "INSERT", "INTO", "SELECT", "TABLE", "*", "VALUES", "NULL", "IS", "DROP", "ALTER", "CONSTRAINT"));
    private List<String> availableTypeNamesWithBracket = new ArrayList<>(Arrays.asList("BIT", "BOOLEAN", "CHAR", "DATETIME2", "DECIMAL", "DECFLOAT", "DOUBLE", "FLOAT", "INTERVAL DAY TO SECOND", "INTERVAL YEAR TO MONTH", "MONEY", "NCHAR", "NUMERIC", "NVARCHAR", "RAW", "SMALLMONEY", "SYSNAME", "TIMESTAMP WITH LOCAL TIME ZONE", "TIMESTAMP WITH TIME ZONE", "TIMESTAMP", "UNIQUEIDENTIFIER", "UROWID", "VARCHAR", "STRING"));
    private List<String> availableTypeNamesWithoutBracket = new ArrayList<>(Arrays.asList("AUDIO", "BFile", "BIGINT", "BINARY", "BINARY DOUBLE", "BINARY FLOAT", "BLOB", "CLOB", "DATALINK", "DATE", "DATETIME", "GRAPHIC", "HTTPURITYPE", "IMAGE", "INTEGER", "LONG CHAR", "LONG_RAW", "NCLOB", "NTEXT", "ORDAUDIO", "ORDDOC", "ORDIMAGE", "ORDIMAGE_SIGNATURE", "ORDVIDEO", "REAL", "ROWID", "SMALLDATETIME", "SMALLINT", "SQL_VARIANT", "SYS_ANYDATA", "SYS_ANYDATASET", "SYS_ANYTYPE", "TEXT", "TINYINT", "TIME", "URITYPE", "VARBINARY", "VARGRAPHIC", "VIDEO", "XDBURITYPE", "XMLTYPE", "JSON"));

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


    protected void setLabelTextIfClicked(Set<MoveableNodeModel> nodes, MoveableNodeModel e, ScrollPane workingPane, TextArea logTextAreaID) {
        Set<MoveableNodeModel> copyNodes = new HashSet<>(nodes);
        copyNodes.remove(e);

        e.getLabel().setOnMouseClicked(event -> {
            this.isLabelOfTableClicked = true;
            Label beforeModificationLabel = e.getLabel();
            TextField textField = new TextField();
            e.gethBox().getChildren().add(textField);
            Label label = new Label();
            label.setId(e.getLabel().getId());
            e.gethBox().getChildren().remove(e.getLabel());

            workingPane.setOnKeyPressed(ec -> {
                label.setText(textField.getText());
                AtomicLong numberOfDuplicateLabelName = new AtomicLong();
                copyNodes.forEach(node -> {
                    if(node.getLabel().getText().equals(label.getText())) numberOfDuplicateLabelName.getAndIncrement();
                });
                String helpString = label.getText().replaceAll("\\s", "");
                if (!label.getText().trim().equals("")
                        && helpString.equals(label.getText())
                        && !prohibitedTableNames.contains(helpString.toUpperCase())
                        && numberOfDuplicateLabelName.get() == 0
                ) {
                    if (!e.gethBox().getChildren().contains(label)) {
                        e.gethBox().getChildren().remove(textField);
                        label.setMinWidth(label.getText().length() * 6.5);
                        e.getxTableView().setMinWidth(label.getText().length() * 6.5);
                        e.gethBox().getChildren().add(label);
                        e.setLabel(label);
                        this.isLabelOfTableClicked = false;
                        String[] textAreaStringSplit = logTextAreaID.getText().split("\n");
                        StringBuilder newTextArea = new StringBuilder();
                        for (String text : textAreaStringSplit) {
                            if (!text.startsWith(e.getLabel().getId()) && !text.trim().equals(""))
                                newTextArea.append(text + "\n");
                        }
                        logTextAreaID.setText(newTextArea.toString());
                    }
                } else {
                    if (ec.getCode().equals(KeyCode.ENTER))
                        if (numberOfDuplicateLabelName.get() == 0) {
                            if (logTextAreaID.getText().equals(""))
                                logTextAreaID.setText(label.getId() + ": " + "Nie zmieniono " + beforeModificationLabel.getText() + " na " + label.getText() + "\n");
                            else
                                logTextAreaID.setText(logTextAreaID.getText() + label.getId() + ": " + "Nie zmieniono " + beforeModificationLabel.getText() + " na " + label.getText() + "\n");
                        } else {
                            logTextAreaID.setText(logTextAreaID.getText() + label.getId() + ": " + "Tabela o takiej nazwie już istnieje\n");
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
                    if (availableTypeNamesWithoutBracket.contains(typeString.toUpperCase())) {
                        tableModel.setType(typeString);
                    } else {
                        availableTypeNamesWithBracket.forEach(str -> {
                            if (typeString.toUpperCase().startsWith(str)) {
                                String[] arrStr = typeString.split("\\(");
                                String[] arrStr2 = typeString.split("\\)");
                                if (arrStr2.length == 1 && arrStr.length == 2 & typeString.endsWith(")")) {
                                    arrStr[1] = arrStr[1].substring(0, arrStr[1].length() - 1);
                                    if (arrStr[1].matches("\\d+") && !arrStr[1].startsWith("0"))
                                        tableModel.setType((String) cellEditEvent.getNewValue());
                                    else tableModel.setType((String) cellEditEvent.getOldValue());
                                } else {
                                    if (typeString.toUpperCase().equals(str)) tableModel.setType(typeString);
                                    else tableModel.setType((String) cellEditEvent.getOldValue());
                                }
                            } else {
                                tableModel.setType((String) cellEditEvent.getOldValue());
                            }
                        });
                    }
                    tableModel.updateData(xTableView);
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
