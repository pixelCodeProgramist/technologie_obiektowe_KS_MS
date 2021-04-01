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

    private String id;
    private String type;
    private ImageView primaryForeignNoneKey;
    private String additional;
    private boolean primaryKey, foreignKey, isUnique, isNotNull;

    public TableModel(String id, String type,ImageView primaryForeignNoneKey,String additional) {
        this.id = id;
        this.type = type;
        this.primaryForeignNoneKey = primaryForeignNoneKey;
        this.additional = additional;
    }

    public ImageView getPrimaryForeignNoneKey() {

        return primaryForeignNoneKey;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public void setNotNull(boolean notNull) {
        isNotNull = notNull;
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

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
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

        TableColumn additionalColumn = new TableColumn("additional");
        additionalColumn.setCellValueFactory(new PropertyValueFactory("additional"));
        additionalColumn.setMinWidth(50);
        additionalColumn.setMaxWidth(50);

        List list = new ArrayList();
        list.add(keyColumn);
        list.add(typeColumn);
        list.add(primaryForeignNoneKeyColumn);
        list.add(additionalColumn);
        return FXCollections.observableList(list);
    }



    public void assignPrimaryKey(XTableView xTableView){
        List list = new ArrayList(xTableView.getItems());
        list.add(this);
        ObservableList data = FXCollections.observableList(list);
        xTableView.setItems(data);
    }

    public void updateDataKey(XTableView xTableView, boolean isUpdatedPK){
        List list = new ArrayList(xTableView.getItems());
        if(isUpdatedPK) {
            xTableView.getItems().forEach(pk -> {
                if (!pk.equals(this) && ((TableModel) pk).isPrimaryKey()) {
                    ((TableModel) pk).setPrimaryForeignNoneKey(null);
                    ((TableModel) pk).setPrimaryKey(false);
                    int index = list.indexOf(pk);
                    list.remove(index);
                    TableModel t = new TableModel(((TableModel) pk).getId(), ((TableModel) pk).getType(),
                            ((TableModel) pk).getPrimaryForeignNoneKey(),((TableModel) pk).additional);
                    t.setAdditional("");
                    list.add(index, t);

                }
            });
        }
        int index = list.indexOf(this);
        list.remove(this);
        TableModel t = new TableModel(getId(),getType(),getPrimaryForeignNoneKey(),getAdditional());
        t.setPrimaryKey(isPrimaryKey());
        t.setForeignKey(isForeignKey());
        t.setUnique(isUnique());
        t.setNotNull(isNotNull());
        t.setAdditional("U/NN");
        list.add(index,t);
        ObservableList data = FXCollections.observableList(list);
        xTableView.setItems(data);
    }

    public void updateData(XTableView xTableView){
        List list = new ArrayList(xTableView.getItems());
        int index = list.indexOf(this);
        list.remove(this);
        TableModel t = new TableModel(getId(),getType(),getPrimaryForeignNoneKey(),getAdditional());
        t.setPrimaryKey(isPrimaryKey());
        t.setForeignKey(isForeignKey());
        t.setUnique(isUnique());
        t.setNotNull(isNotNull());
        list.add(index,t);
        ObservableList data = FXCollections.observableList(list);
        xTableView.setItems(data);
    }

    @Override
    public String toString() {
        return "TableModel{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", primaryForeignNoneKey=" + primaryForeignNoneKey +
                ", additional='" + additional + '\'' +
                ", primaryKey=" + primaryKey +
                ", foreignKey=" + foreignKey +
                ", isUnique=" + isUnique +
                ", isNotNull=" + isNotNull +
                '}';
    }
}

