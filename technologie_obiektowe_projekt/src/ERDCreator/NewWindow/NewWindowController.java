package ERDCreator.NewWindow;

import ERDCreator.Line.LineConnection;
import ERDCreator.resources.XTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.TableModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewWindowController implements Initializable {
    LineConnection lineConnection;
    Pane erdCreatorPane;
    @FXML
    Label foreignKeyLabel;
    @FXML
    Pane newWindowPane;
    @FXML
    ScrollPane scrollPane;
    ToggleGroup radioGroup;
    List<RadioButton> radioButtons;
    List<TableModel> tableModels;
    public NewWindowController(LineConnection lineConnection, Pane erdCreatorPane) {
        radioGroup = new ToggleGroup();
        radioButtons = new ArrayList<>();
        this.lineConnection = lineConnection;
        this.erdCreatorPane = erdCreatorPane;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HBox hBox = (HBox)lineConnection.getTableFirst().getChildren().get(0);
        Label label = (Label) hBox.getChildren().get(0);
        foreignKeyLabel.setText(foreignKeyLabel.getText()+label.getText());
        fillScrollPane();
    }

    private void fillScrollPane(){
        XTableView xTableView = (XTableView) lineConnection.getTableFirst().getChildren().get(1);
        tableModels = xTableView.getItems();
        VBox vBox = new VBox();
        tableModels.forEach(tableModel -> {
            if(tableModel.getAdditional().contains("U")) {
                RadioButton radioButton = new RadioButton(tableModel.getId() + " " + tableModel.getType() + " " + tableModel.getAdditional());
                if (lineConnection.getConnectedKey().getId().equals(tableModel.getId())) radioButton.setSelected(true);
                radioButton.setToggleGroup(radioGroup);
                radioButton.setId(tableModel.getId());
                vBox.getChildren().add(radioButton);
            }
        });
        scrollPane.setContent(vBox);
    }

    @FXML
    public void save(MouseEvent mouseEvent) {
        erdCreatorPane.setDisable(false);
        RadioButton chosenRadioButton = (RadioButton) radioGroup.getSelectedToggle();
        TableModel tableModel = findTableModelAfterId(chosenRadioButton.getId());
        if(tableModel!=null){
            lineConnection.setConnectedKey(tableModel);
        }
        Stage stage = (Stage) newWindowPane.getScene().getWindow();
        stage.close();
    }

    private TableModel findTableModelAfterId(String id){
        Optional<TableModel> chosenTableModel = tableModels.stream().filter(tableModel -> {
            return tableModel.getId().equals(id);
        }).findFirst();
        return chosenTableModel.get();
    }
}
