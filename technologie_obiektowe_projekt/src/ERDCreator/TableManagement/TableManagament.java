package ERDCreator.TableManagement;

import ERDCreator.resources.XTableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import models.Model;
import models.MoveableNodeModel;
import models.TableModel;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


public class TableManagament extends TableApperance {
    private ScrollPane workingPane;
    private AnchorPane newLoadedPane;
    private Set<MoveableNodeModel> nodes = new HashSet<>();
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private Pane content;

    public TableManagament() {
    }

    public void setParameters(Pane content, ScrollPane workingPane, Set<MoveableNodeModel> nodes,
                              Optional<Model> chosenModel) {
        this.content = content;
        this.workingPane = workingPane;
        this.nodes = nodes;
        setChosenModel(chosenModel);
    }

    public void addComponentClick() throws IOException {
        newLoadedPane = FXMLLoader.load(getClass().getResource("../../ERDCreator/resources/MoveableNode.fxml"));
        HBox hBox = (HBox) newLoadedPane.getChildren().get(0);
        Model model = new Model("images/keys/gold.png", "");
        TableModel tableModel = new TableModel("id", "INT", model.getImageView(20, 20));
        XTableView xTableView = XTableView.generateXTableView(tableModel);
        tableModel.assignPrimaryKey(xTableView);
        newLoadedPane.getChildren().add(xTableView);
        Label label = new Label(getFirstTextToLabel());
        hBox.getChildren().add(label);
        hBox.setBackground(getColorToMovableNode());
        content.getChildren().addAll(newLoadedPane);
        MoveableNodeModel moveableNodeModel = new MoveableNodeModel(newLoadedPane, label, hBox, xTableView);
        setInitialContextMenu(moveableNodeModel);
        nodes.add(moveableNodeModel);
    }

    public void paneOnMouseMovedEventHandler(MouseEvent event) {
        nodes.forEach(moveableNodeModel -> {
            moveableNodeModel.getAnchorPane().setOnMousePressed(ep -> {
                setNodePositionIfPressed(event, ep);
                setNodePositionIfDragged(moveableNodeModel);
                setLabelTextIfClicked(moveableNodeModel, workingPane);
            });

            moveableNodeModel.getxTableView().setOnMouseClicked(ec -> {
                setResize(moveableNodeModel);
            });

            moveableNodeModel.getxTableView().setOnMousePressed(mp -> {
                expandContextMenu(mp, moveableNodeModel);
            });

        });
    }

    private void expandContextMenu(MouseEvent mp, MoveableNodeModel model) {
        if (mp.isSecondaryButtonDown()) {
            model.getContextMenu().show(workingPane, mp.getScreenX(), mp.getScreenY());
            for (int i = 0; i < model.getContextMenu().getItems().size(); i++) {
                if (i == 0) {
                    model.getContextMenu().getItems().get(i).setOnAction(e -> {
                        TableModel tableModel = new TableModel("<nazwa>", "<typ>", null);
                        tableModel.assignPrimaryKey(model.getxTableView());
                        model.getAnchorPane().setMinHeight(model.getAnchorPane().getMinWidth() + 30);
                        model.getAnchorPane().setMaxHeight(model.getAnchorPane().getMaxHeight() + 30);
                        model.getxTableView().setMinHeight(model.getxTableView().getMinWidth() + 70);
                        model.getxTableView().setMaxHeight(model.getxTableView().getMaxHeight() + 70);
                    });
                }
                if (i == 1) {
                    model.getContextMenu().getItems().get(i).setOnAction(e -> {
                        if (model.getxTableView().getItems().size() > 1) {
                            TableModel tableModel = (TableModel) model.getxTableView().getSelectionModel().getSelectedItem();
                            model.getxTableView().getItems().remove(tableModel);
                            model.getAnchorPane().setMinHeight(model.getAnchorPane().getMinWidth() - 30);
                            model.getAnchorPane().setMaxHeight(model.getAnchorPane().getMaxHeight() - 30);
                            model.getxTableView().setMinHeight(model.getxTableView().getMinWidth() - 70);
                            model.getxTableView().setMaxHeight(model.getxTableView().getMaxHeight() - 70);
                            model.getContextMenu().getItems().get(1).setVisible(true);
                        } else {
                            model.getContextMenu().getItems().get(1).setVisible(false);
                        }
                    });
                }
                if(i == 2) {
                    model.getContextMenu().getItems().get(i).setOnAction(e ->{
                        Pane pane = (Pane) workingPane.getContent();
                        pane.getChildren().remove(model.getAnchorPane());
                        nodes.remove(model);
                    });
                }
            }
        }
        workingPane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            model.getContextMenu().hide();
        });
    }


    private void setNodePositionIfPressed(MouseEvent event, MouseEvent ep) {
        orgSceneX = ep.getSceneX();
        orgSceneY = ep.getSceneY();
        orgTranslateX = -event.getX();
        orgTranslateY = -event.getY();
    }

    private void setOtherNodesInPane(MoveableNodeModel e,int val,boolean isX){
        ((Pane) workingPane.getContent()).getChildren().forEach(p -> {
            if(!p.equals(e.getAnchorPane())) {
                if(isX)
                    p.setLayoutX(p.getLayoutX()+val);
                else
                    p.setLayoutY(p.getLayoutY()+val);
            }
        });
    }

    private void setNodePositionIfDragged(MoveableNodeModel e) {
        e.getAnchorPane().setOnMouseDragged(ed -> {
            double offsetX = ed.getSceneX() - orgSceneX;
            double offsetY = ed.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX - offsetX;
            double newTranslateY = orgTranslateY - offsetY;
            if (newTranslateX > 0) {
                newTranslateX = -5;
                setOtherNodesInPane(e,3,true);
            }
            if (newTranslateY > 0) {
                newTranslateY = -5;
                setOtherNodesInPane(e,3,false);
            }
            if (newTranslateY < -537) {
                newTranslateY = -532;
                setOtherNodesInPane(e,-3,false);
            }

            if (newTranslateX < -578){
                newTranslateX = -578;
                setOtherNodesInPane(e,-3,true);
            }
            ((AnchorPane) (ed.getSource())).setTranslateX(-newTranslateX);
            ((AnchorPane) (ed.getSource())).setTranslateY(-newTranslateY);
        });
    }

    public void setInitialContextMenu(MoveableNodeModel m) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Dodaj wiersz");
        MenuItem menuItem2 = new MenuItem("Usuń wiersz");
        MenuItem menuItem3 = new MenuItem("Usuń tabele");
        CheckMenuItem menuItem4 = new CheckMenuItem("Primary key");
        CheckMenuItem menuItem5 = new CheckMenuItem("Foreign key");
        CheckMenuItem menuItem6 = new CheckMenuItem("Unique");
        CheckMenuItem menuItem7 = new CheckMenuItem("Not null");


        contextMenu.getItems().addAll(menuItem, menuItem2,menuItem3,menuItem4,menuItem5,menuItem6,menuItem7);
        m.setContextMenu(contextMenu);
    }


}
