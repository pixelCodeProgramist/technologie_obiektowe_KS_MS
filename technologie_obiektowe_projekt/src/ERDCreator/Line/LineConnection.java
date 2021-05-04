package ERDCreator.Line;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;
import models.TableModel;

import java.util.ArrayList;
import java.util.List;

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
    private Line firstLine;
    private Line secondLine;
    private Line thirdLine;

    public LineConnection() {
        this.line = new Line();
        this.circle = new Circle();
        this.circle.setRadius(6);
        this.startCircle = new Circle();
        this.startCircle.setRadius(6);
        this.firstLine = new Line();
        this.secondLine = new Line();
        this.thirdLine = new Line();
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

    public Line getFirstLine() {
        return firstLine;
    }

    public Line getSecondLine() {
        return secondLine;
    }

    public Line getThirdLine() {
        return thirdLine;
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
        if(connectionType.equals(("dziedziczenie"))){
            Pair<Point2D,Point2D> pointAndEquation = calculateABOfPerpendicularLine();
            Point2D pointFirst = calculateNewPointLine(pointAndEquation.getKey(),pointAndEquation.getValue(),20);
            Point2D pointSecond = calculateNewPointLine(pointAndEquation.getKey(),pointAndEquation.getValue(),-20);
            Point2D top = new Point2D(line.getEndX(),line.getEndY());
            setPositionOfLineForTriangle(firstLine,pointFirst,pointSecond);
            setPositionOfLineForTriangle(secondLine,pointFirst,top);
            setPositionOfLineForTriangle(thirdLine,top,pointSecond);
        }
    }

    private void setPositionOfLineForTriangle(Line line,Point2D start,Point2D end){
        line.setStartX(start.getX());
        line.setStartY(start.getY());
        line.setEndX(end.getX());
        line.setEndY(end.getY());
    }

    private Pair<Point2D,Point2D> calculateABOfPerpendicularLine(){
        Point2D start = new Point2D(line.getStartX(),line.getStartY());
        Point2D end = new Point2D(line.getEndX(),line.getEndY());
        double mainW =  start.getX()*1-1*end.getX();
        double aW = start.getY()*1-1*end.getY();
        double bW = start.getX()*end.getY()-start.getY()*end.getX();
        Point2D equation = new Point2D(aW/mainW,bW/mainW);
        Point2D basePoint = calculateNewPointLine(equation,end,-20);
        double a2 = -1/equation.getX();
        double b2 = basePoint.getY()-basePoint.getX()*a2;
        return new Pair<Point2D,Point2D> (new Point2D(a2,b2),basePoint);
    }
    private Point2D calculateNewPointLine(Point2D equation,Point2D end,double offset){
        double x =end.getX()+offset;
        return new Point2D(x,equation.getX()*x+equation.getY());
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
