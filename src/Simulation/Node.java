package Simulation;

import com.jfoenix.controls.JFXButton;

/**
 * Created by Ian on 13/9/2016.
 */
public class Node {
    protected double x, y;
    private JFXButton btn;

    public Node(JFXButton btn){
        this.btn = btn;
        x = btn.getLayoutX();
        y = btn.getLayoutY();
    }
}
