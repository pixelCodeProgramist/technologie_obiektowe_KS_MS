package ERDCreator;

import Config.Configuration;
import ERDCreator.LeftPanel.LeftPanelCreator;
import ERDCreator.TableManagement.TableManagament;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.MoveableNodeModel;
import sample.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ERDCreatorController {
    @FXML
    private TreeView<String> idTabels;
    private Set<MoveableNodeModel> nodes;
    private TableManagament tableManagament;
    private LeftPanelCreator leftPanelCreator;
    @FXML
    private AnchorPane workingPane;


    public void initialize() throws FileNotFoundException {
        nodes = new HashSet<>();
        leftPanelCreator = new LeftPanelCreator(idTabels);
        leftPanelCreator.loadTreeItems();
        tableManagament = new TableManagament();
        tableManagament.setParameters(workingPane,nodes,leftPanelCreator.getChosenModel());
        workingPane.setOnMouseMoved(tableManagament::paneOnMouseMovedEventHandler);
    }


    @FXML
    public void backToMenu(MouseEvent mouseEvent) throws IOException {
        Configuration configuration = new Configuration();
        configuration.changeScene("../sample/sample.fxml", mouseEvent, new Controller());
    }

    @FXML
    public void mouseClick(MouseEvent mouseEvent) {
        leftPanelCreator.addNodeToWorkingPane();
    }

    @FXML
    public void addComponentClick(MouseEvent mouseEvent) throws IOException {
        if (leftPanelCreator.isActivatedToAddPane()) {
            tableManagament.setParameters(workingPane,nodes,leftPanelCreator.getChosenModel());
            tableManagament.addComponentClick();
        }
    }
}
