package Simulation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by yusiang on 13/9/16.
 */
public class SimulatorTest {
    @Test
    public void testGetMST(){
        //TEST 1 See SimulatorTest.png
        State s = new State();
        Simulator sim = new Simulator(s);
        Node n1 = new Node(1);
        s.addNode(n1);
        Node n2 = new Node(2);
        s.addNode(n2);
        Node n3 = new Node(3);
        s.addNode(n3);
        Node n4 = new Node(4);
        s.addNode(n4);
        Node n5 = new Node(5);
        s.addNode(n5);
        Node n6 = new Node(6);
        s.addNode(n6);
        Route r1 = new Route(n1,n2,1);
        s.addRoute(r1);
        Route r2 = new Route(n1,n5,2);
        s.addRoute(r2);
        Route r3 = new Route(n3,n2,3);
        s.addRoute(r3);
        Route r4 = new Route(n5,n3,4);
        s.addRoute(r4);
        Route r5 = new Route(n3,n4,5);
        s.addRoute(r5);
        Route r6 = new Route(n4,n5,6);
        s.addRoute(r6);
        ArrayList<Route> mst = sim.getMST(n3);
        for (Route r:mst)
            ;//System.out.println(r.toString());
        //System.out.println(mst.size());
        assert(mst.contains(r1));
        assert(mst.contains(r2));
        assert(mst.contains(r3));
        assert(mst.contains(r5));
        assert(!mst.contains(r4));
        assert(!mst.contains(r6));
        System.out.println("Test 1 pass");
        //Test 2 - A random unconnected node shall not affect mst
        Node n7 = new Node(7);
        s.addNode(n7);
        mst = sim.getMST(n3);
        for (Route r:mst)
            ;//System.out.println(r.toString());
        assert(mst.contains(r1));
        assert(mst.contains(r2));
        assert(mst.contains(r3));
        assert(mst.contains(r5));
        assert(!mst.contains(r4));
        assert(!mst.contains(r6));
        System.out.println("Test 2 pass");
        //Test 3 - MST depends on source
        Node n8 = new Node(8);
        Node n9 = new Node(9);
        Node n10 = new Node(10);
        s.addNode(n8);
        s.addNode(n9);
        s.addNode(n10);
        Route rA = new Route(n8,n9,1);
        Route rB = new Route(n8,n10,1);
        Route rC = new Route(n10,n9,1);
        s.addRoute(rA);
        s.addRoute(rB);
        s.addRoute(rC);
        mst = sim.getMST(n8);
        assert(mst.contains(rA));
        assert(mst.contains(rB));
        assert(!mst.contains(rC));
        System.out.println("Test 3.1 pass");
        mst = sim.getMST(n9);
        assert(mst.contains(rA));
        assert(!mst.contains(rB));
        assert(mst.contains(rC));
        System.out.println("Test 3.2 pass");
        mst = sim.getMST(n10);
        assert(!mst.contains(rA));
        assert(mst.contains(rB));
        assert(mst.contains(rC));
        System.out.println("Test 3.3 pass");
    }

    @Test
    public void testGetDijkstraRoute(){
        State s = new State();
        Simulator sim = new Simulator(s);
        Map<Node,ArrayList<Route>> map;
        Node n1 = new Node(1);
        s.addNode(n1);
        Node n2 = new Node(2);
        s.addNode(n2);
        Node n3 = new Node(3);
        s.addNode(n3);
        Node n4 = new Node(4);
        s.addNode(n4);
        Node n5 = new Node(5);
        s.addNode(n5);
        Node n6 = new Node(6);
        s.addNode(n6);
        Node n7 = new Node(7);
        s.addNode(n7);
        Route r1 = new Route(n1,n2,1);
        s.addRoute(r1);
        Route r2 = new Route(n1,n5,2);
        s.addRoute(r2);
        Route r3 = new Route(n3,n2,3);
        s.addRoute(r3);
        Route r4 = new Route(n5,n3,4);
        s.addRoute(r4);
        Route r5 = new Route(n3,n4,5);
        s.addRoute(r5);
        Route r6 = new Route(n4,n5,6);
        s.addRoute(r6);
        Node n8 = new Node(8);
        Node n9 = new Node(9);
        Node n10 = new Node(10);
        s.addNode(n8);
        s.addNode(n9);
        s.addNode(n10);
        Route rA = new Route(n8,n9,1);
        Route rB = new Route(n8,n10,1);
        Route rC = new Route(n10,n9,1);
        s.addRoute(rA);
        s.addRoute(rB);
        s.addRoute(rC);
        map = sim.getDijkstraRoute(n8);
        assert(!map.get(n8).contains(rA));
        assert(!map.get(n8).contains(rB));
        assert(!map.get(n8).contains(rC));

        assert(map.get(n9).contains(rA));
        assert(!map.get(n9).contains(rB));
        assert(!map.get(n9).contains(rC));

        assert(!map.get(n10).contains(rA));
        assert(map.get(n10).contains(rB));
        assert(!map.get(n10).contains(rC));
        System.out.println("DijkstraTest 1.1 OK");

        map = sim.getDijkstraRoute(n9);
        assert(map.get(n8).contains(rA));
        assert(!map.get(n8).contains(rB));
        assert(!map.get(n8).contains(rC));

        assert(!map.get(n9).contains(rA));
        assert(!map.get(n9).contains(rB));
        assert(!map.get(n9).contains(rC));

        assert(!map.get(n10).contains(rA));
        assert(!map.get(n10).contains(rB));
        assert(map.get(n10).contains(rC));
        System.out.println("DijkstraTest 1.2 OK");

        map = sim.getDijkstraRoute(n2);
        assertArrayEquals(map.get(n5).toArray(),new Route[]{r1,r2});
        System.out.println("DijkstraTest 2.1 OK");
        assertArrayEquals(map.get(n4).toArray(),new Route[]{r3,r5});
        System.out.println("DijkstraTest 2.2 OK");

        map = sim.getDijkstraRoute(n4);
        assertArrayEquals(map.get(n5).toArray(),new Route[]{r6});
        System.out.println("DijkstraTest 3.1 OK");
        assertArrayEquals(map.get(n2).toArray(),new Route[]{r5,r3});
        System.out.println("DijkstraTest 3.2 OK");

        map = sim.getDijkstraRoute(n7);
        assertNull(map.get(n2));
        System.out.println("DijkstraTest 4.1 OK");
        assertNull(map.get(n9));
        System.out.println("DijkstraTest 4.2 OK");
    }
}
