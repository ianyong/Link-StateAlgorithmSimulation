package Simulation;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yusiang on 13/9/16.
 */
public class Simulator {

    private State state;

    public Simulator(State s){
        state = s;
    }

    public ArrayList<Route> getMST(Node origin){
        state.updateRoutes();//In case sth changed.
        ArrayList<Route> mst = new ArrayList<Route>();
        ArrayList<Route> unusedRoutes = new ArrayList<Route>(state.getRoutes());
        ArrayList<Route> nextRoutes = new ArrayList<Route>();
        ArrayList<Node> unfoundNodes = new ArrayList<Node>(state.getNodes());//Copy of nodes. Will remove once connected.
        if(!state.getNodes().contains(origin)) return null;//WTF
        Node currNode = origin;
        for(Route r:unusedRoutes)
            if(r.has(currNode))nextRoutes.add(r);
        state.removeRoute(currNode,null);
        while(unfoundNodes.size()!=0){
            //Find next node
            Collections.sort(nextRoutes);
            //Pick first route that goes to any node in unfoundNodes
            //Route is in MST, and that node will be nextNode
            Node nextNode=null;
            for(Route r:nextRoutes){
                if(unfoundNodes.contains(r.n1)){
                    nextNode=r.n1;
                    if(!mst.contains(r))mst.add(r); //KLUDGE
                    break;
                }
                else if(unfoundNodes.contains(r.n2)){
                    nextNode=r.n2;
                    if(!mst.contains(r))mst.add(r); //KLUDGE
                    break;
                }
            }
            if (nextNode==null) break;//No matches. Tree is not connected
            //Update currNode
            for(int i=0;i<unfoundNodes.size();){
                if(unfoundNodes.get(i).getID()==currNode.getID()) unfoundNodes.remove(i);
                else i++;
            }
            currNode = nextNode;
            //Update nextRoutes & unusedRoutes
            for(Route r:unusedRoutes)
                if(r.has(currNode))nextRoutes.add(r);
            for(int i=0;i<unusedRoutes.size();){                                   //0 means a wildcard, i.e. n,0 will remove all routes with n
                if(unusedRoutes.get(i).has(currNode.getID())&&unusedRoutes.get(i).has(null)) unusedRoutes.remove(i);
                else i++;
            }
        }
        ;

        return mst;

    }
}
