package ERDCreator;

import Config.Configuration;
import DirectoryExtender.DirectoryExtender;
import ERDCreator.resources.XTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Model;
import models.MoveableNodeModel;
import models.TableModel;
import sample.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ERDCreatorController {
    @FXML
    private TreeView<String> idTabels;
    private TreeItem<String> tabels;
    private TreeItem<String> relarionships;
    private TreeItem<String> others;
    private String[] relationsType = {"1 do 1", "1 do *", "* do *"};
    private String[] othersType = {"dziedziczenie"};
    private String[] tableType = {"tabela", "klasa"};
    private List<Model> models;
    private boolean activatedToAddPane = false;
    private Optional<Model> chosenModel;
    private int classNumber = 0;
    private int tabelNumber = 0;
    @FXML
    private AnchorPane workingPane;

    public void initialize() throws FileNotFoundException {
        models = new ArrayList<>();
        loadTreeItems();
        workingPane.setOnMouseMoved(this::paneOnMouseMovedEventHandler);

    }


    public void loadTreeItems() throws FileNotFoundException {
        TreeItem<String> root = new TreeItem<String>("");
        tabels = new TreeItem<String>("Tabele");
        relarionships = new TreeItem<String>("Relacje");
        others = new TreeItem<String>("Inne");
        TreeItem<String> others = new TreeItem<String>("Inne");
        root.setExpanded(true);

        loadModel("images/tabels", tabels, tableType);
        loadModel("images/relations", relarionships, relationsType);
        loadModel("images/others", others, othersType);

        idTabels.setShowRoot(false);
        idTabels.setRoot(root);
        root.getChildren().addAll(tabels, relarionships, others);
    }

    private void loadModel(String url, TreeItem<String> item, String[] type) throws FileNotFoundException {
        for (int i = 1; i < DirectoryExtender.countFilesInDirectory(url) + 1; i++) {
            Model model = new Model(url + "/" + i + ".png", type[i - 1]);
            item.getChildren().add(
                    new TreeItem<String>(model.getDescription(),
                            model.getImageView()));
            models.add(model);
        }
    }

    @FXML
    public void backToMenu(MouseEvent mouseEvent) throws IOException {
        Configuration configuration = new Configuration();
        configuration.changeScene("../sample/sample.fxml", mouseEvent, new Controller());
    }

    @FXML
    public void mouseClick(MouseEvent mouseEvent) {

        TreeItem<String> item = idTabels.getSelectionModel().getSelectedItem();
        if (item != null) {
            chosenModel = models.stream().filter(e ->
                    e.getDescription().equals(item.getValue())).findFirst();
            if (chosenModel.isPresent()) {
                activatedToAddPane = true;
            } else {
                activatedToAddPane = false;
            }
        }
    }


    @FXML
    public void addComponentClick(MouseEvent mouseEvent) throws IOException {
        if (activatedToAddPane) {
            AnchorPane newLoadedPane = FXMLLoader.load(getClass().getResource("../ERDCreator/resources/MoveableNode.fxml"));
            HBox hBox = (HBox) newLoadedPane.getChildren().get(0);
            Model model = new Model("images/keys/gold.png","");

            TableModel tableModel = new TableModel("id","INT",model.getImageView(20,20));
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
    }



    public String getFirstTextToLabel() {
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


    public Background getColorToMovableNode() {
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

    private Set<MoveableNodeModel> nodes = new HashSet<>();
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;


    public void paneOnMouseMovedEventHandler(MouseEvent event) {
        nodes.forEach(moveableNodeModel -> {
            moveableNodeModel.getAnchorPane().setOnMousePressed(ep -> {
                setNodePositionIfPressed(event,ep);
                setNodePositionIfDragged(moveableNodeModel);
                setLabelTextIfClicked(moveableNodeModel);
            });
            moveableNodeModel.getxTableView().setOnMouseClicked(ec->{

            });
        });
    }


    private void setLabelTextIfClicked(MoveableNodeModel e) {
        e.getLabel().setOnMouseClicked(event -> {
            TextField textField = new TextField();
            e.gethBox().getChildren().add(textField);
            Label label = new Label();
            e.gethBox().getChildren().remove(e.getLabel());

            workingPane.setOnMouseClicked(ec->{
                label.setText(textField.getText());
                String helpString = label.getText().replaceAll("\\s", "");
                if(!label.getText().trim().equals("")&&helpString.equals(label.getText())) {
                    if(!e.gethBox().getChildren().contains(label)) {
                        e.gethBox().getChildren().remove(textField);
                        label.setMinWidth(label.getText().length()*6.5);
                        e.getxTableView().setMinWidth(label.getText().length()*6.5);
                        e.gethBox().getChildren().add(label);
                        e.setLabel(label);

                    }
                }
            });
        });
    }


    public void setNodePositionIfPressed(MouseEvent event,MouseEvent ep){
        orgSceneX = ep.getSceneX();
        orgSceneY = ep.getSceneY();
        orgTranslateX=-event.getX();
        orgTranslateY=-event.getY();
    }

    public void setNodePositionIfDragged(MoveableNodeModel e){
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
