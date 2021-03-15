package ERDCreator.resources;

import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import models.TableModel;

public class XTableView extends TableView {
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        Pane header = (Pane) lookup("TableHeaderRow");
        header.setMinHeight(0);
        header.setPrefHeight(0);
        header.setMaxHeight(0);
        header.setVisible(false);
    }

    public static XTableView generateXTableView(TableModel tableModel){
        XTableView xTableView = new XTableView();
        xTableView.setEditable(true);
        xTableView.setLayoutY(22);
        xTableView.setPrefHeight(58);
        xTableView.setPrefWidth(Config.WIDTH_TABLE);
        xTableView.getColumns().setAll(tableModel.getColumns());
        return xTableView;
    }
}
