package Simulation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Controller {

    private static int COUNTER = 1;
    private boolean edit = false;
    private State state = new State();
    @FXML
    private Pane pane;
    @FXML
    private JFXTabPane hiddenTabPane;
    @FXML
    private Tab tabHome, tabEdit;

    @FXML
    private void addNode(){
        JFXButton btn = new JFXButton(String.valueOf(COUNTER++));
        btn.getStyleClass().add("node");
        btn.setLayoutX(200);
        btn.setLayoutY(200);

        makeDraggable(btn);
        makeRightClickDeletable(btn);

        pane.getChildren().add(btn);
        state.addNode(new Node(btn));
    }

    @FXML
    private void clearAll(){
        pane.getChildren().clear();
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
                        btn.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                        btn.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
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

    private void makeRightClickDeletable(final JFXButton btn){
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if(edit) {
                    if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        state.removeNode(Integer.parseInt(btn.getText()));
                        pane.getChildren().remove(btn);
                    }
                }
            }
        });
    }

    class Delta{
        double x, y;
        boolean dragStarted = false;
    }

}
