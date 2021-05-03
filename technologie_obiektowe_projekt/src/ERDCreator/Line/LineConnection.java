package ERDCreator.Line;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import models.TableModel;

public class LineConnection {
    private String connectionType;
    private AnchorPane tableFirst;
    private AnchorPane tableSecond;
    private Line line;
    private Circle circle;
    private Circle startCircle;
    private TableModel tableModel;
    private TableModel connectedKey;
    private TableModel connectedKeySecond;

    public LineConnection() {
        this.line = new Line();
        this.circle = new Circle();
        this.circle.setRadius(6);
        this.startCircle = new Circle();
        this.startCircle.setRadius(6);
    }

    public TableModel getConnectedKeySecond() {
        return connectedKeySecond;
    }

    public void setConnectedKeySecond(TableModel connectedKeySecond) {
        this.connectedKeySecond = connectedKeySecond;
    }

    public TableModel getConnectedKey() {
        return connectedKey;
    }

    public void setConnectedKey(TableModel connectedKey) {
        this.connectedKey = connectedKey;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        this.tableModel = tableModel;
    }

    public Circle getStartCircle() {
        return startCircle;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setStartX(double x) {
        line.setStartX(x);
        if (connectionType.equals("* do *")){
            this.startCircle.setLayoutX(x);
        }
    }

    public void setStartY(double y) {
        line.setStartY(y);
        if (connectionType.equals("* do *")){
            this.startCircle.setLayoutY(y);
        }
    }

    public void setEndX(double x) {
        line.setEndX(x);
        if (connectionType.equals("1 do *")||connectionType.equals("* do *"))
            this.circle.setLayoutX(x);
    }

    public void setEndY(double y) {
        line.setEndY(y);
        if (connectionType.equals("1 do *")||connectionType.equals("* do *"))
            this.circle.setLayoutY(y);
    }

    public double getStartX() {
        return line.getStartX();
    }

    public double getStartY() {
        return line.getStartY();
    }

    public double getEndX() {
        return line.getEndX();
    }

    public double getEndY() {
        return line.getEndY();
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public AnchorPane getTableFirst() {
        return tableFirst;
    }

    public void setTableFirst(AnchorPane tableFirst) {
        this.tableFirst = tableFirst;
    }

    public AnchorPane getTableSecond() {
        return tableSecond;
    }

    public void setTableSecond(AnchorPane tableSecond) {
        this.tableSecond = tableSecond;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

}
