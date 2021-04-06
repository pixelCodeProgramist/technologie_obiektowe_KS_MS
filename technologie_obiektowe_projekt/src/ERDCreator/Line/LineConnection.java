package ERDCreator.Line;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

public class LineConnection {
    private String connectionType;
    private AnchorPane tableFirst;
    private AnchorPane tableSecond;
    private Line line;

    public LineConnection() {
       this.line = new Line();
       
    }

    public void setStartX(double x){
        line.setStartX(x);
    }
    public void setStartY(double y){
        line.setStartY(y);
    }

    public void setEndX(double x){
        line.setEndX(x);
    }
    public void setEndY(double y){
        line.setEndY(y);
    }

    public double getStartX(){
        return line.getStartX();
    }
    public double getStartY(){
        return line.getStartY();
    }

    public double getEndX(){
        return line.getEndX();
    }
    public double getEndY(){
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
