package Simulation;

/**
 * Created by yusiang on 13/9/16.
 */
public class Route {
    private double x1,y1,x2,y2;
    private Node n1,n2;

    public Route(Node n1, Node n2) {
        this.x1 = n1.x;
        this.y1 = n1.y;
        this.x2 = n2.x;
        this.y2 = n2.y;
        this.n1 = n1;
        this.n2 = n2;
    }
}
