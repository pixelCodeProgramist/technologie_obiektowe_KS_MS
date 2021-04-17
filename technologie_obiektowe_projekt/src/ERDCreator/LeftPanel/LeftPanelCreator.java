package ERDCreator.LeftPanel;

import DirectoryExtender.DirectoryExtender;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import models.Model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeftPanelCreator {
    private TreeItem<String> tabels;
    private TreeItem<String> relarionships;
    private TreeItem<String> others;
    private String[] relationsType = {"1 do 1", "1 do *", "* do *"};
    private String[] othersType = {"dziedziczenie"};
    private String[] tableType = {"tabela", "klasa"};
    private TreeView<String> idTabels;
    private List<Model> models;
    private Optional<Model> chosenModel;
    private boolean activatedToAddPane;

    public LeftPanelCreator(TreeView<String> idTabels) {
        this.idTabels = idTabels;
        models = new ArrayList<>();
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

    public void addNodeToWorkingPane(){
        try {
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
        }catch (Exception e){

        }

    }

    public Optional<Model> getChosenModel() {
        return chosenModel;
    }

    public boolean isActivatedToAddPane() {
        return activatedToAddPane;
    }
}
