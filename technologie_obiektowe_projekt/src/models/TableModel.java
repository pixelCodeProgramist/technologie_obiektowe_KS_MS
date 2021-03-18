package models;

import ERDCreator.resources.XTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class TableModel {
    @Override
    public String toString() {
        return "TableModel{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", primaryForeignNoneKey=" + primaryForeignNoneKey +
                '}';
    }

    private String id;
    private String type;
    private ImageView primaryForeignNoneKey;

    public TableModel(String id, String type,ImageView primaryForeignNoneKey) {
        this.id = id;
        this.type = type;
        this.primaryForeignNoneKey = primaryForeignNoneKey;
    }

    public ImageView getPrimaryForeignNoneKey() {
        return primaryForeignNoneKey;
    }

    public void setPrimaryForeignNoneKey(ImageView primaryForeignNoneKey) {
        this.primaryForeignNoneKey = primaryForeignNoneKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ObservableList<?> getColumns(){

        TableColumn keyColumn = new TableColumn("id");
        keyColumn.setCellValueFactory(new PropertyValueFactory("id"));
        keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        keyColumn.setMinWidth(25);
        keyColumn.setMaxWidth(25);
        TableColumn typeColumn = new TableColumn("type");
        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));
        typeColumn.setMinWidth(35);
        typeColumn.setMaxWidth(35);
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn primaryForeignNoneKeyColumn = new TableColumn("primaryForeignNoneKey");
        primaryForeignNoneKeyColumn.setCellValueFactory(new PropertyValueFactory("primaryForeignNoneKey"));
        primaryForeignNoneKeyColumn.setMinWidth(35);
        primaryForeignNoneKeyColumn.setMaxWidth(35);

        List list = new ArrayList();
        list.add(keyColumn);
        list.add(typeColumn);
        list.add(primaryForeignNoneKeyColumn);
        return FXCollections.observableList(list);
    }



    public void assignPrimaryKey(XTableView xTableView){
        List list = new ArrayList();
        list.add(new TableModel(id,type,primaryForeignNoneKey));
        ObservableList data = FXCollections.observableList(list);
        xTableView.setItems(data);

    }

}

