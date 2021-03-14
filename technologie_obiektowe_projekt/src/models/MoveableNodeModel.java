package models;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;



public class MoveableNodeModel {
    private AnchorPane anchorPane;
    private Label label;
    private HBox hBox;
    private ListView listView;

    public MoveableNodeModel(AnchorPane anchorPane, Label label, HBox hBox, ListView listView) {
        this.anchorPane = anchorPane;
        this.label = label;
        this.hBox = hBox;
        this.listView = listView;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public Label getLabel() {
        return label;
    }

    public HBox gethBox() {
        return hBox;
    }

    public ListView getListView() {
        return listView;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void sethBox(HBox hBox) {
        this.hBox = hBox;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
