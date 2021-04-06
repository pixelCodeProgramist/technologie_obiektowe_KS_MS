package models;

import ERDCreator.Line.LineConnection;
import ERDCreator.resources.XTableView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;


public class MoveableNodeModel {
    private AnchorPane anchorPane;
    private Label label;
    private HBox hBox;
    private XTableView xTableView;
    private ContextMenu contextMenu;
    private Map<LineConnection,String> lineConnectionStringMap;

    public MoveableNodeModel(AnchorPane anchorPane, Label label, HBox hBox, XTableView xTableView) {
        lineConnectionStringMap = new HashMap<>();
        this.anchorPane = anchorPane;
        this.label = label;
        this.hBox = hBox;
        this.xTableView = xTableView;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public Map<LineConnection, String> getLineConnectionStringMap() {
        return lineConnectionStringMap;
    }

    public void setLineConnectionStringMap(Map<LineConnection, String> lineConnectionStringMap) {
        this.lineConnectionStringMap = lineConnectionStringMap;
    }

    public void addLineConnection(LineConnection lineConnection, String input) {
        lineConnectionStringMap.put(lineConnection,input);
    }


    public void setContextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
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
