package ERDCreator.TableManagement;

import ERDCreator.resources.XTableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import models.Model;
import models.MoveableNodeModel;
import models.TableModel;

import java.io.FileNotFoundException;
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
        tableModel.setPrimaryKey(true);
        tableModel.setForeignKey(false);
        tableModel.setNotNull(true);
        tableModel.setUnique(true);
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
            TableModel selectedTableModel = (TableModel) model.getxTableView().getSelectionModel().getSelectedItem();
            ((CheckMenuItem) model.getContextMenu().getItems().get(3)).setSelected(selectedTableModel.isPrimaryKey());
            ((CheckMenuItem) model.getContextMenu().getItems().get(4)).setSelected(selectedTableModel.isForeignKey());
            ((CheckMenuItem) model.getContextMenu().getItems().get(5)).setSelected(selectedTableModel.isUnique());
            ((CheckMenuItem) model.getContextMenu().getItems().get(6)).setSelected(selectedTableModel.isNotNull());
            model.getContextMenu().show(workingPane, mp.getScreenX(), mp.getScreenY());
            boolean primaryKeyChecked = ((CheckMenuItem) model.getContextMenu().getItems().get(3)).isSelected();
            boolean foreignKeyChecked = ((CheckMenuItem) model.getContextMenu().getItems().get(4)).isSelected();
            if(primaryKeyChecked) model.getContextMenu().getItems().get(6).setVisible(false);
            else model.getContextMenu().getItems().get(6).setVisible(true);
            if (primaryKeyChecked || foreignKeyChecked) {
                model.getContextMenu().getItems().get(5).setVisible(false);
            }else {
                model.getContextMenu().getItems().get(5).setVisible(true);
            }
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
                if (i == 2) {
                    model.getContextMenu().getItems().get(i).setOnAction(e -> {
                        Pane pane = (Pane) workingPane.getContent();
                        pane.getChildren().remove(model.getAnchorPane());
                        nodes.remove(model);
                    });
                }
                if (i == 3) {
                    model.getContextMenu().getItems().get(i).setOnAction(e -> {


                         model.getxTableView().getItems().forEach(pk->{
                             if(!pk.equals(selectedTableModel)&& ((TableModel) pk).isPrimaryKey()){
                                 ((TableModel) pk).setPrimaryForeignNoneKey(null);
                                 ((TableModel) pk).setPrimaryKey(false);
                             }
                         });

                        selectedTableModel.setPrimaryKey(!selectedTableModel.isPrimaryKey());
                        if(selectedTableModel.isPrimaryKey()) {
                            try {
                                Model model2 = new Model("images/keys/gold.png", "");
                                selectedTableModel.setPrimaryForeignNoneKey(model2.getImageView(20, 20));

                                selectedTableModel.setForeignKey(false);
                                selectedTableModel.setNotNull(true);
                                selectedTableModel.setUnique(true);
                            } catch (FileNotFoundException fileNotFoundException) {
                                fileNotFoundException.printStackTrace();
                            }
                        }else {
                            selectedTableModel.setPrimaryForeignNoneKey(null);
                        }
                    });
                }
                if (i == 4) {
                    model.getContextMenu().getItems().get(i).setOnAction(e -> {
                        selectedTableModel.setForeignKey(!selectedTableModel.isForeignKey());
                        if(selectedTableModel.isForeignKey()) {
                            try {
                                Model model2 = new Model("images/keys/gray.png", "");
                                selectedTableModel.setPrimaryForeignNoneKey(model2.getImageView(20, 20));
                                selectedTableModel.setPrimaryKey(false);
                                selectedTableModel.setNotNull(false);
                                selectedTableModel.setUnique(false);
                            } catch (FileNotFoundException fileNotFoundException) {
                                fileNotFoundException.printStackTrace();
                            }
                        }else {
                            selectedTableModel.setPrimaryForeignNoneKey(null);
                        }
                    });
                }
                if (i == 5) {
                    model.getContextMenu().getItems().get(i).setOnAction(e -> {
                        selectedTableModel.setUnique(!selectedTableModel.isUnique());
                    });
                }

                if (i == 6) {

                    model.getContextMenu().getItems().get(i).setOnAction(e -> {
                        selectedTableModel.setNotNull(!selectedTableModel.isNotNull());
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

    private void setOtherNodesInPane(MoveableNodeModel e, int val, boolean isX) {
        ((Pane) workingPane.getContent()).getChildren().forEach(p -> {
            if (!p.equals(e.getAnchorPane())) {
                if (isX)
                    p.setLayoutX(p.getLayoutX() + val);
                else
                    p.setLayoutY(p.getLayoutY() + val);
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
                setOtherNodesInPane(e, 3, true);
            }
            if (newTranslateY > 0) {
                newTranslateY = -5;
                setOtherNodesInPane(e, 3, false);
            }
            if (newTranslateY < -537) {
                newTranslateY = -532;
                setOtherNodesInPane(e, -3, false);
            }

            if (newTranslateX < -578) {
                newTranslateX = -578;
                setOtherNodesInPane(e, -3, true);
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
        menuItem4.setSelected(true);
        CheckMenuItem menuItem5 = new CheckMenuItem("Foreign key");
        menuItem5.setSelected(false);
        CheckMenuItem menuItem6 = new CheckMenuItem("Unique");
        menuItem6.setSelected(true);
        CheckMenuItem menuItem7 = new CheckMenuItem("Not null");
        menuItem7.setSelected(true);

        contextMenu.getItems().addAll(menuItem, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, menuItem7);
        m.setContextMenu(contextMenu);
    }


}
