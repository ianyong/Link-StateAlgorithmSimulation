package Simulation;

import com.jfoenix.controls.JFXButton;

/**
 * Created by Ian on 13/9/2016.
 */
public class Node {
    private static int COUNTER =1;
    protected double x, y;
    private JFXButton btn;
    public final int nodeID;

    public Node(JFXButton b){
        x = btn.getLayoutX();
        y = btn.getLayoutY();
        btn = b;
        nodeID=COUNTER++;
    }
    public int getID(){return nodeID;}
    @Override
    public boolean equals(Object o){
        if(o==null) return false;
        if(!Node.class.isAssignableFrom(o.getClass())) return false;
        return this.getID()==((Node)o).getID();
    }
}
