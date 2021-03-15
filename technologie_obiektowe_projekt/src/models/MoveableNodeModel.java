package models;

import ERDCreator.resources.XTableView;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;



public class MoveableNodeModel {
    private AnchorPane anchorPane;
    private Label label;
    private HBox hBox;
    private XTableView xTableView;

    public MoveableNodeModel(AnchorPane anchorPane, Label label, HBox hBox, XTableView xTableView) {
        this.anchorPane = anchorPane;
        this.label = label;
        this.hBox = hBox;
        this.xTableView = xTableView;
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

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void sethBox(HBox hBox) {
        this.hBox = hBox;
    }

    public XTableView getxTableView() {
        return xTableView;
    }

    public void setxTableView(XTableView xTableView) {
        this.xTableView = xTableView;
    }
}
