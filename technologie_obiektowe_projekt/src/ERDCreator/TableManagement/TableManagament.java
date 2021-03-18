package ERDCreator.TableManagement;

import ERDCreator.resources.XTableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import models.Model;
import models.MoveableNodeModel;
import models.TableModel;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


public class TableManagament extends TableApperance{
    private AnchorPane workingPane;
    private Set<MoveableNodeModel> nodes = new HashSet<>();
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;

    public void setParameters(AnchorPane workingPane, Set<MoveableNodeModel> nodes,
                           Optional<Model> chosenModel) {
        this.workingPane = workingPane;
        this.nodes = nodes;
        setChosenModel(chosenModel);
    }

    public void addComponentClick() throws IOException {
        AnchorPane newLoadedPane = FXMLLoader.load(getClass().getResource("../../ERDCreator/resources/MoveableNode.fxml"));
        HBox hBox = (HBox) newLoadedPane.getChildren().get(0);
        Model model = new Model("images/keys/gold.png", "");
        TableModel tableModel = new TableModel("id", "INT", model.getImageView(20, 20));
        XTableView xTableView = XTableView.generateXTableView(tableModel);
        tableModel.assignPrimaryKey(xTableView);
        newLoadedPane.getChildren().add(xTableView);
        Label label = new Label(getFirstTextToLabel());
        hBox.getChildren().add(label);
        hBox.setBackground(getColorToMovableNode());
        workingPane.getChildren().add(newLoadedPane);
        MoveableNodeModel moveableNodeModel = new MoveableNodeModel(newLoadedPane, label, hBox, xTableView);
        nodes.add(moveableNodeModel);
    }

    public void paneOnMouseMovedEventHandler(MouseEvent event) {
        nodes.forEach(moveableNodeModel -> {
            moveableNodeModel.getAnchorPane().setOnMousePressed(ep -> {
                setNodePositionIfPressed(event, ep);
                setNodePositionIfDragged(moveableNodeModel);
                setLabelTextIfClicked(moveableNodeModel,workingPane);
            });

            moveableNodeModel.getxTableView().setOnMouseClicked(ec -> {
                setResize(moveableNodeModel);
            });
        });
    }

    private void setNodePositionIfPressed(MouseEvent event, MouseEvent ep) {
        orgSceneX = ep.getSceneX();
        orgSceneY = ep.getSceneY();
        orgTranslateX = -event.getX();
        orgTranslateY = -event.getY();
    }

    private void setNodePositionIfDragged(MoveableNodeModel e) {
        e.getAnchorPane().setOnMouseDragged(ed -> {
            double offsetX = ed.getSceneX() - orgSceneX;
            double offsetY = ed.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX - offsetX;
            double newTranslateY = orgTranslateY - offsetY;
            ((AnchorPane) (ed.getSource())).setTranslateX(-newTranslateX);
            ((AnchorPane) (ed.getSource())).setTranslateY(-newTranslateY);
        });
    }


}
