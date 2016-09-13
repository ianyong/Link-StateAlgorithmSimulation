package Simulation;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yusiang on 13/9/16.
 */
public class Simulator {
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private ArrayList<Route> routes = new ArrayList<Route>();

    public void addNode(Node n){
        nodes.add(n);
    }

    public ArrayList<Route> getMST(Node origin){
        for(Route r:routes) r.updateXY();//In case sth changed.
        ArrayList<Route> mst = new ArrayList<Route>();
        ArrayList<Route> unusedRoutes = new ArrayList<Route>(routes);
        ArrayList<Route> nextRoutes = new ArrayList<Route>();
        ArrayList<Node> unfoundNodes = new ArrayList<Node>(nodes);//Copy of nodes. Will remove once connected.
        if(!nodes.contains(origin)) return null;//WTF
        Node currNode = origin;
        for(Route r:unusedRoutes)
            if(r.has(currNode))nextRoutes.add(r);
        removeRoute(unusedRoutes,currNode,null);
        while(unfoundNodes.size()!=0){
            //Find next node
            Collections.sort(nextRoutes);
            //Pick first route that goes to any node in unfoundNodes
            //Route is in MST, and that node will be nextNode
            Node nextNode=null;
            for(Route r:nextRoutes){
                if(unfoundNodes.contains(r.n1)){
                    nextNode=r.n1;
                    mst.add(r);
                    break;
                }
                else if(unfoundNodes.contains(r.n2)){
                    nextNode=r.n2;
                    mst.add(r);
                    break;
                }
            }
            if (nextNode==null) break;//No matches. Tree is not connected
            //Update currNode
            removeNode(unfoundNodes,currNode);
            currNode = nextNode;
            //Update nextRoutes & unusedRoutes
            for(Route r:unusedRoutes)
                if(r.has(currNode))nextRoutes.add(r);
            removeRoute(unusedRoutes,currNode,null);
        }
        ;

        return mst;

    }
    private void removeNode(ArrayList<Node> l, Node n){ removeNode(l,n.getID());}
    private void removeNode(ArrayList<Node> l, int nodeID){
        for(int i=0;i<l.size();){
            if(l.get(i).getID()==nodeID) l.remove(i);
            else i++;
        }
    }
    private void removeRoute(ArrayList<Route> l, Route r){removeRoute(l,r.getID());}
    private void removeRoute(ArrayList<Route> l, int routeID){
        for(int i=0;i<l.size();){
            if(l.get(i).getID()==routeID) l.remove(i);
            else i++;
        }
    }
    private void removeRoute(ArrayList<Route> l,Node n1,Node n2){
        removeRoute(l,n1==null?0:n1.getID(),n2==null?0:n2.getID());
    }
    private void removeRoute(ArrayList<Route> l, int n1,int n2){    //Removes a route with n1 and n2
        for(int i=0;i<l.size();){                                   //0 means a wildcard, i.e. n,0 will remove all routes with n
            if(l.get(i).has(n1)&&l.get(i).has(n2)) l.remove(i);
            else i++;
        }
    }

}
