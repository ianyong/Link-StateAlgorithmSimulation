package Simulation;

import com.jfoenix.controls.JFXButton;

/**
 * Created by Ian on 13/9/2016.
 */
public class Node {
    protected double x, y;
    private JFXButton btn;

    public Node(double x, double y, JFXButton b){
        this.x = x;
        this.y = y;
        btn = b;
    }
}
