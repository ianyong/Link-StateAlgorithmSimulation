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
    public Node (int nodeID){
        System.out.println("FOR DEBUG USE ONLY NOT FOR PRODUCTION");
        this.nodeID = nodeID;
        x=y=0;
    }
    public Node(JFXButton b){
        btn = b;
        x = btn.getLayoutX();
        y = btn.getLayoutY();
        nodeID=COUNTER++;
    }

    public void updateXY(){
        x = btn.getLayoutX();
        y = btn.getLayoutY();
    }

    public JFXButton getComponent(){
        return btn;
    }

    public int getID(){return nodeID;}
    @Override
    public boolean equals(Object o){
        if(o==null) return false;
        if(!Node.class.isAssignableFrom(o.getClass())) return false;
        return this.getID()==((Node)o).getID();
    }
}
