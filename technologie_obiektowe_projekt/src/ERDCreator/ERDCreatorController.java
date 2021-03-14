package ERDCreator;

import Config.Configuration;
import DirectoryExtender.DirectoryExtender;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.Model;
import models.MoveableNodeModel;
import sample.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
            ListView listView = (ListView) newLoadedPane.getChildren().get(1);
            Label label = new Label(getFirstTextToLabel());
            hBox.getChildren().add(label);
            hBox.setBackground(getColorToMovableNode());
            workingPane.getChildren().add(newLoadedPane);
            MoveableNodeModel moveableNodeModel = new MoveableNodeModel(newLoadedPane, label, hBox, listView);
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
        nodes.forEach(e -> {
            e.getAnchorPane().setOnMousePressed(ep -> {
                setNodePositionIfPressed(event,ep);
                setNodePositionIfDragged(e);
                setLabelTextIfClicked(e);
            });

        });
    }

    private void setLabelTextIfClicked(MoveableNodeModel e) {
        e.getLabel().setOnMouseClicked(event -> {
            String str = "jdjdjefjlfdjjkdfjkdklfdkdfkdfujjkjkkjjkjkhjjkhj";
            e.getLabel().setMinWidth(str.length()*6.5);
            e.getListView().setMinWidth(str.length()*6.5);
            e.getLabel().setText(str);
            e.getLabel().setAlignment(Pos.CENTER);

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
