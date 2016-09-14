package Simulation;

import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * Created by Ian on 14/9/2016.
 */
public class State {
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private ArrayList<Route> routes = new ArrayList<Route>();

    protected void addNode(Node n){
        nodes.add(n);
    }

    protected void addRoute(Route r){
        routes.add(r);
    }

    protected ArrayList<Node> getNodes(){
        return nodes;
    }

    protected ArrayList<Route> getRoutes(){
        return routes;
    }

    protected Node getNode(int nodeID){
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).getID() == nodeID)
                return nodes.get(i);
        }
        return null;
    }

    protected void updateRoutes(int nodeID){
        for(int i = 0; i < routes.size(); i++){
            if(routes.get(i).has(nodeID))
                routes.get(i).updateXY();
        }
    }

    protected ArrayList<javafx.scene.Node> removeRoutes(int nodeID){
        ArrayList<javafx.scene.Node> nodes = new ArrayList<javafx.scene.Node>();
        for(int i = 0; i < routes.size(); i++){
            if(routes.get(i).has(nodeID)) {
                nodes.add(routes.get(i).getLine());
                nodes.add(routes.get(i).getRectangle());
                routes.remove(i);
                i--;
            }
        }
        return nodes;
    }

    protected boolean routeExists(int n1, int n2){
        for(int i = 0; i < routes.size(); i++){
            if(routes.get(i).has(n1) && routes.get(i).has(n2))
                return true;
        }
        return false;
    }

    protected void removeNode(Node n){ removeNode(n.getID());}

    protected void removeNode(int nodeID){
        for(int i=0;i<nodes.size();){
            if(nodes.get(i).getID()==nodeID) nodes.remove(i);
            else i++;
        }
    }

    protected void removeRoute(Route r){removeRoute(r.getID());}

    protected void removeRoute(int routeID){
        for(int i=0;i<routes.size();){
            if(routes.get(i).getID()==routeID) routes.remove(i);
            else i++;
        }
    }

    protected void removeRoute(Node n1,Node n2){
        removeRoute(n1==null?0:n1.getID(),n2==null?0:n2.getID());
    }

    protected ArrayList<javafx.scene.Node> removeRoute(int n1, int n2){    //Removes a route with n1 and n2
        ArrayList<javafx.scene.Node> nodes = new ArrayList<javafx.scene.Node>();
        for(int i=0;i<routes.size();){                                   //0 means a wildcard, i.e. n,0 will remove all routes with n
            if(routes.get(i).has(n1)&&routes.get(i).has(n2)){
                nodes.add(routes.get(i).getLine());
                nodes.add(routes.get(i).getRectangle());
                routes.remove(i);
            }
            else i++;
        }
        return nodes;
    }

    protected void updateRoutes(){
        for(Route r:routes) r.updateXY();
    }

}
