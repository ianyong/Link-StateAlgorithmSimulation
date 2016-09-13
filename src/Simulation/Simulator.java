package Simulation;

import java.util.ArrayList;

/**
 * Created by yusiang on 13/9/16.
 */
public class Simulator {
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private ArrayList<Route> routes = new ArrayList<Route>();

    public ArrayList<Route> getMST(Node origin){
        ArrayList<Route> mst = new ArrayList<Route>();
        ArrayList<Node> unfoundNodes = new ArrayList<Node>(nodes);//Copy of nodes. Will remove once connected.
        if(!nodes.contains(origin)) return null;//WTF

        return mst;
    }
}
