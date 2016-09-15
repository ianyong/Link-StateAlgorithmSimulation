package Simulation;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.Comparator;

/**
 * Created by yusiang on 13/9/16.
 */
public class Route implements Comparator<Route>,Comparable<Route>{
    private static int COUNTER =1;
    private double x1,y1,x2,y2;
    protected final Node n1,n2;
    private Line line;
    private Rectangle rec;
    private Label label;
    private double weight;
    public final int routeID;

    public Route(Node n1, Node n2, final Line line, final Rectangle rec, final Label label, double weight) {
        if(n1.equals(n2)) throw new RuntimeException("WTF Loop");
        this.n1 = n1;
        this.n2 = n2;
        this.line = line;
        this.rec = rec;
        this.label = label;
        this.weight = weight;
        label.setText(String.valueOf(weight));
        updateXY();
        routeID = COUNTER++;
        new Thread() { //need to draw UI once then update again to get the right positioning of components
            public void run() {
                try {
                    Thread.sleep(20);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    public void run() {
                        updateXY();
                        line.setVisible(true);
                        rec.setVisible(true);
                        label.setVisible(true);
                    }
                });
            }
        }.start();
    }

    public Route(Node n1, Node n2, Line line, Rectangle rec, Label label){
        this(n1, n2, line, rec, label, 1);
    }

    @Deprecated
    public Route(Node n1, Node n2, double weight){
        //this(n1, n2, null, null, null, weight);
        this.n1=n1;this.n2=n2;
        this.weight=weight;
        routeID = COUNTER++;
        System.out.println("FOR DEBUG USE ONLY");
    }

    public void updateXY(){
        this.x1 = n1.x;
        this.y1 = n1.y;
        this.x2 = n2.x;
        this.y2 = n2.y;

        line.setStartX(x1 + n1.getComponent().getWidth() / 2);
        line.setStartY(y1 + n1.getComponent().getHeight() / 2);
        line.setEndX(x2 + n2.getComponent().getWidth() / 2);
        line.setEndY(y2 + n2.getComponent().getHeight() / 2);

        rec.setX((x1 + x2) / 2 + n1.getComponent().getWidth() / 2 - rec.getWidth() / 2);
        rec.setY((y1 + y2) / 2 + n1.getComponent().getHeight() / 2 - rec.getHeight() / 2);

        label.setLayoutX((x1 + x2) / 2 + n1.getComponent().getWidth() / 2 - label.getWidth() / 2);
        label.setLayoutY((y1 + y2) / 2 + n1.getComponent().getHeight() / 2 - label.getHeight() / 2);
    }

    public Line getLine(){
        return line;
    }

    public Rectangle getRectangle(){
        return rec;
    }

    public Label getLabel(){
        return label;
    }

    public int getID(){return routeID;}
    public boolean has(int nodeID){
        return (nodeID==0||n1.getID()==nodeID||n2.getID()==nodeID); //Route always contain the null node
    }
    public boolean has(Node n){
        return (n==null||n1.equals(n)||n2.equals(n));
    }

    public void setWeight(double weight) {
        this.weight = weight;
        label.setText(String.valueOf(weight));
        rec.setX((x1 + x2) / 2 + n1.getComponent().getWidth() / 2 - rec.getWidth() / 2);
        label.setLayoutX((x1 + x2) / 2 + n1.getComponent().getWidth() / 2 - label.getWidth() / 2);
    }

    public double getWeight() {
        return weight;
    }

    public int compareTo(Route that) {
        return compare(this,that);
    }

    public int compare(Route r1, Route r2) {
        if(r1.weight>r2.weight) return 1;
        if(r1.weight==r2.weight) return 0;
        if(r1.weight<r2.weight) return -1;
        throw new RuntimeException("RouteCompareWTF");
    }
    public boolean equals(Object o){
        if(o==null) return false;
        if(!Route.class.isAssignableFrom(o.getClass())) return false;
        return this.getID()==((Route)o).getID();
    }
    public String toString(){
        return "Route "+routeID+" btw nodes "+n1.getID()+"&"+n2.getID()+", weight="+weight;
    }
    public String toStringShort(){
        return "n"+n1.getID()+"->n"+n2.getID()+"@w="+weight;
    }

    public void setHighlighted(boolean b){
        line.setStyle(b?"-fx-stroke: #0000FF":"-fx-stroke: #4DB6AC");
        rec.setStyle(b?"-fx-fill: #0000FF":"-fx-fill: #009688");//TODO
    }
}
