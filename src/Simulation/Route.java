package Simulation;

import java.util.Comparator;

/**
 * Created by yusiang on 13/9/16.
 */
public class Route implements Comparator<Route>,Comparable<Route>{
    private static int COUNTER =1;
    private double x1,y1,x2,y2;
    protected final Node n1,n2;
    private double weight;
    public final int routeID;

    public Route(Node n1, Node n2, double weight) {
        if(n1.equals(n2)) throw new RuntimeException("WTF Loop");
        this.n1 = n1;
        this.n2 = n2;
        updateXY();
        this.weight = weight;
        routeID = COUNTER++;
    }
    public void updateXY(){
        this.x1 = n1.x;
        this.y1 = n1.y;
        this.x2 = n2.x;
        this.y2 = n2.y;
    }

    public int getID(){return routeID;}
    public boolean has(int nodeID){
        return (nodeID==0||n1.getID()==nodeID||n2.getID()==nodeID); //Route always contain the null node
    }
    public boolean has(Node n){
        return (n==null||n1.equals(n)||n2.equals(n));
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

}
