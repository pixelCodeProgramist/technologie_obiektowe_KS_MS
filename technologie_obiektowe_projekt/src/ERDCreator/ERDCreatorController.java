package ERDCreator;

import Config.Configuration;
import ERDCreator.LeftPanel.LeftPanelCreator;
import ERDCreator.Line.LineConnection;
import ERDCreator.TableManagement.TableManagament;
import ERDCreator.Time.TimerToLog;
import SQLCreator.SQLCreatorController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import models.MoveableNodeModel;
import models.TableModel;
import sample.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ERDCreatorController {
    @FXML
    private TreeView<String> idTabels;
    private Set<MoveableNodeModel> nodes;
    private List<LineConnection> lineConnections;
    private TableManagament tableManagament;
    private LeftPanelCreator leftPanelCreator;
    private Pane content;
    @FXML
    private TextArea logTextAreaID;
    @FXML
    private ScrollPane workingPane;
    @FXML
    private Button addComponentButton;
    public ERDCreatorController(Set<MoveableNodeModel> nodes,List<LineConnection> lineConnections) {
        this.nodes = nodes;
        this.lineConnections = lineConnections;
    }

    public ERDCreatorController() {
    }

    public void initialize() throws FileNotFoundException {
        if(nodes==null) nodes = new HashSet<>();
        if(lineConnections==null) lineConnections = new ArrayList<>();
        leftPanelCreator = new LeftPanelCreator(idTabels);
        leftPanelCreator.loadTreeItems();
        tableManagament = new TableManagament();
        content = new Pane();
        content.autosize();
        workingPane.setContent(content);
        this.workingPane.setContent(content);
        this.workingPane.setPannable(true);
        tableManagament.setParameters(content,workingPane,nodes,leftPanelCreator.getChosenModel(),logTextAreaID,lineConnections);
        this.nodes.forEach(e->{
            content.getChildren().add(e.getAnchorPane());
        });
        this.lineConnections.forEach(e->{
            content.getChildren().add(e.getLine());
            e.getLine().toBack();
            if(e.getConnectionType().equals("1 do *")) content.getChildren().add(e.getCircle());
        });
        workingPane.setOnMouseMoved(tableManagament::paneOnMouseMovedEventHandler);
        workingPane.setOnMouseClicked(tableManagament::workingPaneClickHandler);
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
        try {
            if (leftPanelCreator.isActivatedToAddPane() && leftPanelCreator.getChosenModel().isPresent()) {
                tableManagament.setParameters(content, workingPane, nodes, leftPanelCreator.getChosenModel(), logTextAreaID,lineConnections);
                if (leftPanelCreator.getChosenModel().get().getDescription().equals("tabela")
                        || leftPanelCreator.getChosenModel().get().getDescription().equals("klasa")) {
                    tableManagament.addTableClick();
                } else {
                    tableManagament.setStateToConnectTables(leftPanelCreator.getChosenModel().get().getDescription(), addComponentButton);
                    addComponentButton.setDisable(true);
                }
            }
        }catch (Exception e){
            
        }
    }

    @FXML
    public void generateSQL(MouseEvent mouseEvent) throws IOException {
        if(nodes.size()>0) {
            Configuration configuration = new Configuration();
            configuration.changeScene("../SQLCreator/SQLCreator.fxml", mouseEvent, new SQLCreatorController(nodes,lineConnections));
        }else{
            logTextAreaID.setText(logTextAreaID.getText()+"["+ TimerToLog.getTime()+"] " +"Brak tabel w panelu roboczym\n");
        }
    }
}
