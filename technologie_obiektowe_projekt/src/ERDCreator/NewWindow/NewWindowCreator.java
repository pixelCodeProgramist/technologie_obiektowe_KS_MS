package ERDCreator.NewWindow;

import ERDCreator.Line.LineConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class NewWindowCreator {
    public void start(LineConnection lineConnection, Pane ERDCreatorPane){

        Stage stage = null;
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewWindow.fxml"));
            fxmlLoader.setController(new NewWindowController(lineConnection,ERDCreatorPane));

            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage = new Stage();
            stage.setTitle("Connection field");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            ERDCreatorPane.setDisable(false);
            System.out.println("ERROR " +e);
            if(stage!=null) stage.close();

        }
    }
}
