package ERDCreator;

import Config.Configuration;
import DirectoryExtender.DirectoryExtender;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import models.Model;
import sample.Controller;


import java.io.FileNotFoundException;
import java.io.IOException;

public class ERDCreatorController {
    @FXML
    private TreeView<String> idTabels;
    private TreeItem<String> tabels;
    private TreeItem<String> relarionships;
    private TreeItem<String> others;
    private  String [] relationsType = {"1 do 1","1 do *","* do *"};
    private String [] othersType = {"dziedziczenie"};
    private String [] tableType = {"tabela","klasa"};

    public void initialize() throws FileNotFoundException {
        loadTreeItems("initial 1", "initial 2", "initial 3");
    }

    public void loadTreeItems(String... rootItems) throws FileNotFoundException {
        TreeItem<String> root = new TreeItem<String>("");
        tabels = new TreeItem<String>("Tabele");
        relarionships = new TreeItem<String>("Relacje");
        others = new TreeItem<String>("Inne");
        TreeItem<String> others = new TreeItem<String>("Inne");
        root.setExpanded(true);

        loadModel("images/tabels",tabels,tableType);
        loadModel("images/relations",relarionships,relationsType);
        loadModel("images/others",others,othersType);

        idTabels.setShowRoot(false);
        idTabels.setRoot(root);
        root.getChildren().addAll(tabels,relarionships,others);
    }

    private void loadModel(String url, TreeItem<String> item,String [] type) throws FileNotFoundException {
        for(int i=1;i<DirectoryExtender.countFilesInDirectory(url)+1;i++) {
            Model model = new Model(url+"/"+i+".png", type[i-1]);
            item.getChildren().add(
                    new TreeItem<String>(model.getDescription(),
                            model.getImageView()));

        }
    }

    @FXML
    public void backToMenu(MouseEvent mouseEvent) throws IOException {
        Configuration configuration = new Configuration();
        configuration.changeScene("../sample/sample.fxml",mouseEvent,new Controller());
    }




}
