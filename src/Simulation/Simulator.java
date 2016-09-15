package Simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yusiang on 13/9/16.
 */
public class Simulator {
    private final State state;

    public Simulator(State s){
        state=s;
    }

    public ArrayList<Route> getMST(Node origin){
        ArrayList<Route> routes = state.getRoutes();
        ArrayList<Node> nodes = state.getNodes();
        //for(Route r:routes) r.updateXY();//In case sth changed.
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
            removeNode(unfoundNodes,currNode);
            currNode = nextNode;
            //Update nextRoutes & unusedRoutes
            for(Route r:unusedRoutes)
                if(r.has(currNode))nextRoutes.add(r);
            removeRoute(unusedRoutes,currNode,null);
        }
        return mst;

    }

    public Map<Node,ArrayList<Route>> getDijkstraRoute(Node origin){
        System.out.println("New D");
        ArrayList<Route> routes = state.getRoutes();
        Map<Node,ArrayList<Route>> map = new HashMap<Node,ArrayList<Route>>();
        ArrayList<Route> unusedRoutes = new ArrayList<Route>(routes);
        ArrayList<Route> nextRoutes = new ArrayList<Route>();
        //Populate Map
        ////Not needed, since get() returns null if not exist.
        //Initial origin
        map.put(origin,new ArrayList<Route>());//0 length route
        //Add routes from origin to anywhere into nextRoutes, rmv from unusedRoutes
        for(Route r:unusedRoutes)
            if(r.has(origin))nextRoutes.add(r);
        removeRoute(unusedRoutes,origin,null);
        //Sort nextRoutes
        Collections.sort(nextRoutes);
        Map<Node,ArrayList<Route>> oldMap = new HashMap<Node, ArrayList<Route>>() {
        };
        while(!oldMap.equals(map)) {
            oldMap=new HashMap<Node, ArrayList<Route>>(map);
            while (nextRoutes.size() > 0) {
                //foreach route in nextroutes. (Routes in nextroutes guaranteed to go from known node to elsewhere
                //pick shortest route to anywhere
                Route currRoute = nextRoutes.get(0);
                System.out.println(currRoute.toStringShort());
                //If dn from initial to N1               + N1 to N2              < curr dn to N2 in map, then it's better route, use it
                if (getTotalWeight(map.get(currRoute.n1)) + currRoute.getWeight() < getTotalWeight(map.get(currRoute.n2))) {
                    //New route for n2 is origin to N1 plus N1 to N2
                    ArrayList<Route> betterRoute = new ArrayList<Route>(map.get(currRoute.n1));
                    betterRoute.add(currRoute);
                    map.put(currRoute.n2, betterRoute);
                    //n2 discovered, add routes starting from n2 (from unusedRoutes)
                    for (Route r : unusedRoutes)
                        if (r.has(currRoute.n2)) nextRoutes.add(r);
                    removeRoute(unusedRoutes, currRoute.n2, null);
                }
                //If dn from initial to N2               + N2 to N1              < curr dn to N1 in map, then it's better route, use it (Reverse as unweighted graph)
                else if (getTotalWeight(map.get(currRoute.n2)) + currRoute.getWeight() < getTotalWeight(map.get(currRoute.n1))) {
                    //New route for n1 is origin to N2 plus  N2 to  N1
                    ArrayList<Route> betterRoute = new ArrayList<Route>(map.get(currRoute.n2));
                    betterRoute.add(currRoute);
                    map.put(currRoute.n1, betterRoute);
                    //n1 discovered, add routes starting from n2 (from unusedRoutes)
                    for (Route r : unusedRoutes)
                        if (r.has(currRoute.n1)) nextRoutes.add(r);
                    removeRoute(unusedRoutes, currRoute.n1, null);
                }
                //Remove this route from nextRoutes
                removeRoute(nextRoutes, currRoute);
                Collections.sort(nextRoutes);
            }
            nextRoutes=new ArrayList<Route>(routes);
            //
        }
        map.put(origin,new ArrayList<Route>());
        return map;
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
    private int getTotalWeight(ArrayList<Route> routes){
        if(routes==null) return Integer.MAX_VALUE/2;//No route. Dn is infinity or thereabouts.
        int i=0;
        for(Route r:routes) i+=r.getWeight();
        return i;
    }

}