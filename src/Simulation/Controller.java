package Simulation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTabPane;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Controller implements Initializable{

    private boolean edit = false, link = false, simulation = false;
    private State state = new State();
    private Simulator sim = new Simulator(state);
    private JFXButton linkNode = null;
    @FXML
    private Pane pane, map;
    @FXML
    private JFXTabPane hiddenTabPane;
    @FXML
    private Tab tabHome, tabEdit, tabSimulation;
    @FXML
    private JFXButton buttonClearAll, buttonBack, buttonAddNode, buttonAddRoute;
    @FXML
    private JFXSnackbar snackbar;

    public void initialize(URL url, ResourceBundle rb){
        hiddenTabPane.getSelectionModel().select(tabHome);
        snackbar.registerSnackbarContainer(pane);
        final EventHandler handler = new EventHandler() {
            public void handle(Event event) {
                clearAll();
            }
        };
        //initialise clear all button behaviour
        buttonClearAll.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @SuppressWarnings("unchecked")
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    snackbar.show("Are you sure you want to clear all?", "Yes", 3000, handler);
                    new Thread(){
                        public void run(){
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    buttonClearAll.setDisable(true); //disable clear all button to prevent spamming
                                    buttonBack.setDisable(true);
                                    buttonAddNode.setDisable(true);
                                    buttonAddRoute.setDisable(true);
                                }
                            });
                            try{
                                Thread.sleep(3000);
                            }
                            catch(InterruptedException e){
                                e.printStackTrace();
                            }
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    buttonClearAll.setDisable(false);
                                    buttonBack.setDisable(false);
                                    buttonAddNode.setDisable(false);
                                    buttonAddRoute.setDisable(false);
                                }
                            });
                        }
                    }.start();
                }
            }
        });
        //make clicking on empty space reset linking
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                linkNode = null;
                state.lct = null;
                decolouriseNodes();
                decolouriseRoutes();
            }
        });
    }

    @FXML
    private void addNode(){
        JFXButton btn = new JFXButton("");
        btn.getStyleClass().add("node");
        btn.setLayoutX(pane.getWidth() / 2 - 25); //getWidth() and getHeight() for btn return 0.0 here for some reason
        btn.setLayoutY(pane.getHeight() / 2 - 25);

        makeDraggable(btn);
        makeButtonDoStuff(btn);

        map.getChildren().add(btn);
        Node n=new Node(btn);
        state.addNode(n);
        btn.setText(n.getID()+"");
    }

    private void clearAll(){
        map.getChildren().clear();
        state = new State();
    }

    @FXML
    private void setEditTab(){
        hiddenTabPane.getSelectionModel().select(tabEdit);
        edit = true;
    }

    @FXML
    private void setHomeTab(){
        hiddenTabPane.getSelectionModel().select(tabHome);
        edit = false;
        simulation = false;
        state.lct = null;
        decolouriseRoutes();
        decolouriseNodes();
    }

    @FXML
    private void setSimulationTab(){
        hiddenTabPane.getSelectionModel().select(tabSimulation);
        simulation = true;
    }

    @FXML
    private void toggleLinkMode(){
        if(link){
            link = false;
            edit = true;
            buttonBack.setDisable(false);
            buttonAddNode.setDisable(false);
            buttonClearAll.setDisable(false);
            snackbar.show("Linking disabled", 1000);
            buttonAddRoute.setText("Add Route");
        }else{
            link = true;
            edit = false;
            buttonBack.setDisable(true);
            buttonAddNode.setDisable(true);
            buttonClearAll.setDisable(true);
            snackbar.show("Linking enabled", 1000);
            buttonAddRoute.setText("Cancel");
        }
    }

    private void makeDraggable(final JFXButton btn){
        final Delta dragDelta = new Delta();
        /* onMousePressed and onMouseReleased don't work due to the ripple effect overriding
        btn.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                System.out.println("!");
                // record a delta distance for the drag and drop operation.
                dragDelta.x = btn.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = btn.getLayoutY() - mouseEvent.getSceneY();
                btn.setCursor(Cursor.MOVE);
            }
        });
        btn.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                btn.setCursor(Cursor.HAND);
            }
        });*/
        btn.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if(edit) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (!dragDelta.dragStarted) { //hack workaround
                            dragDelta.x = btn.getLayoutX() - mouseEvent.getSceneX();
                            dragDelta.y = btn.getLayoutY() - mouseEvent.getSceneY();
                            dragDelta.dragStarted = true;
                        }
                        btn.setLayoutX(boundaryCorrectionX(mouseEvent.getSceneX() + dragDelta.x));
                        btn.setLayoutY(boundaryCorrectionY(mouseEvent.getSceneY() + dragDelta.y));
                        state.getNode(Integer.parseInt(btn.getText())).updateXY();
                        state.updateRoutes(Integer.parseInt(btn.getText()));
                    }
                }
            }
        });
        btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if(edit) {
                    btn.setCursor(Cursor.HAND);
                }
            }
        });
        btn.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if(edit) {
                    dragDelta.dragStarted = false; //hack workaround
                }
            }
        });
    }

    private double boundaryCorrectionX(double d){
        return max(0, min(d, pane.getWidth() - 50));
    }

    private double boundaryCorrectionY(double d){
        return max(0, min(d, pane.getHeight() - 50));
    }

    private void makeButtonDoStuff(final JFXButton btn){
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if(edit) {
                    if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        map.getChildren().removeAll(state.removeRoutes(Integer.parseInt(btn.getText())));
                        state.removeNode(Integer.parseInt(btn.getText()));
                        map.getChildren().remove(btn);
                    }
                }
                if(link) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if(linkNode == null){
                            linkNode = btn;
                            state.getNode(Integer.parseInt(btn.getText())).setHighlighted(true);
                        }else{
                            if(!linkNode.equals(btn)) {
                                if(!state.routeExists(Integer.parseInt(btn.getText()), Integer.parseInt(linkNode.getText()))) {
                                    createRoute(btn, linkNode);
                                }else{
                                    removeRoute(btn, linkNode);
                                }
                            }
                            linkNode = null;
                            decolouriseNodes();
                        }
                    }
                }
                if(simulation) {
                    decolouriseRoutes();
                    int ID=Integer.parseInt(btn.getText());
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        decolouriseNodes();
                        state.getNode(ID).setHighlighted(true);
                        state.lct=sim.getDijkstraRoute(state.getNode(ID));
                        for(Node n:state.getNodes()){
                            if(state.lct.get(n)!=null)
                                for(Route r:state.lct.get(n)){
                                    r.setHighlighted(true);
                                }
                        }
                    }else if(mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        if(state.lct==null)
                            snackbar.show("Select origin first", 1000);
                        else{
                            if(state.lct.get(state.getNode(ID))!=null)
                                for(Route r:state.lct.get(state.getNode(ID))){
                                    r.setHighlighted(true);
                                }
                            snackbar.show(getTotalCost(state.lct.get(state.getNode(ID))), 1000);
                        }
                    }
                }
            }
        });
    }

    private void createRoute(JFXButton btn1, JFXButton btn2){
        Line line = new Line();
        line.setStyle("-fx-stroke: #4DB6AC");
        line.setVisible(false);

        Label label = new Label();
        label.setStyle("-fx-text-fill: white");
        label.setVisible(false);
        label.setMouseTransparent(true); //to allow clicks to register with the underlying rectangle

        Rectangle rec = new Rectangle();
        rec.setStyle("-fx-fill: #009688");
        rec.widthProperty().bind(label.widthProperty().add(10));
        rec.setHeight(20);
        rec.setVisible(false);

        Route route = new Route(state.getNode(Integer.parseInt(btn1.getText())), state.getNode(Integer.parseInt(btn2.getText())), line, rec, label);

        makeRouteCostEditable(rec, route);

        map.getChildren().addAll(line, rec, label);

        //always place nodes at front
        label.toBack();
        rec.toBack();
        line.toBack();

        state.addRoute(route);
    }

    private void makeRouteCostEditable(final javafx.scene.Node node, final Route route){
        final Delta dragDelta = new Delta();
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if(edit || link) {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = node.getLayoutX() + mouseEvent.getSceneX();
                    dragDelta.y = node.getLayoutY() + mouseEvent.getSceneY();
                    dragDelta.weight = route.getWeight();
                }
            }
        });
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if(edit || link) {
                    node.setCursor(Cursor.HAND);
                }
            }
        });
        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if(edit || link) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
                        route.setWeight((int)(dragDelta.weight + ((mouseEvent.getSceneX() - dragDelta.x) / 10) - (mouseEvent.getSceneY() - dragDelta.y) / 10));
                    else if(mouseEvent.getButton().equals(MouseButton.SECONDARY))
                        route.setWeight((int)(dragDelta.weight + ((mouseEvent.getSceneX() - dragDelta.x)) - (mouseEvent.getSceneY() - dragDelta.y)));
                }
            }
        });
    }

    private void removeRoute(JFXButton btn1, JFXButton btn2){
        map.getChildren().removeAll(state.removeRoute(Integer.parseInt(btn1.getText()), Integer.parseInt(btn2.getText())));
    }

    private void decolouriseRoutes(){
        for(Route r:state.getRoutes()) r.setHighlighted(false);
    }

    private void decolouriseNodes(){
        for(Node n:state.getNodes()) n.setHighlighted(false);
    }

    private String getTotalCost(ArrayList<Route> routes){
        if(routes==null) return "No route";
        int i=0;
        for(Route r:routes) i+=r.getWeight();
        return "Total cost: "+i;
    }

    private class Delta{
        double x, y;
        int weight;
        boolean dragStarted = false;
    }

}
