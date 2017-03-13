/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mike
 */
public class CheckersUIController implements Initializable {
    @FXML
    private MenuBar menuBar;
    
    @FXML
    private StackPane stackPane;
    
    private Stage stage;
    private Scene scene;
    
    private double boardWidth;
    private double boardHeight;

    private int numRows;
    private int numCols;
    
    private Color darkColor;
    private Color lightColor;
    
    private CheckerBoardUI checkerBoard;
    private CheckerBoardStateSpace checkerBoardStateSpace;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDefaultColor();
        setDefaultRowsCols();
        checkerBoardStateSpace = new CheckerBoardStateSpace(new CheckerBoard(numRows, numCols));
        checkerBoardStateSpace.getBoard().placeChecker(26, TileContent.PLAYER1KING);
        checkerBoardStateSpace.getBoard().placeChecker(1, TileContent.PLAYER1KING);
        checkerBoardStateSpace.getBoard().placeChecker(19, TileContent.PLAYER2KING);
        checkerBoardStateSpace.getBoard().placeChecker(21, TileContent.PLAYER2KING);
        checkerBoardStateSpace.getBoard().placeChecker(37, TileContent.PLAYER2KING);
        checkerBoardStateSpace.getBoard().placeChecker(35, TileContent.PLAYER2KING);
                
                
                
    }   
    
    public void ready(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
        
        
        ChangeListener<Number> listener = (ObservableValue<? extends Number> observable, Number oldValue, final Number newValue) -> {
            renderBoard();
        };
        
        scene.widthProperty().addListener(listener);
        scene.heightProperty().addListener(listener);
        
        
        
        renderBoard();
    }
          EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if(checkerBoardStateSpace.getActiveActions() == null || checkerBoardStateSpace.getActiveActions().getMoves() == null || checkerBoardStateSpace.getActiveActions().getJumps() == null){
                    System.out.println("ON");
                    double rectangleHeight = checkerBoard.getRectangleHeight();
                    double rectangleWidth = checkerBoard.getRectangleWidth();
                    double verticalPadding = (boardHeight - (numRows*rectangleHeight))/2;
                    double horizontalPadding = (boardWidth - (numCols*rectangleWidth))/2;
                    int tileIndex = ((int)((t.getX()-horizontalPadding)/rectangleWidth) + (int)((t.getY()-verticalPadding)/rectangleHeight) * numRows);

                    System.out.println("Index: " + tileIndex);
                    checkerBoardStateSpace.setActiveActions(checkerBoardStateSpace.getBoard().getValidMoves(tileIndex), 
                            checkerBoardStateSpace.getBoard().getValidJumps(tileIndex, checkerBoardStateSpace.getBoard().getTiles()[tileIndex].getContent(),  new JumpType(tileIndex)));
                    checkerBoardStateSpace.setActiveChecker(tileIndex);
                    renderBoard();
                }
                else{
                    System.out.println("OFF");
                    checkerBoardStateSpace.setActiveActions(null, null);
                    renderBoard();
                }
            }
        };
          
          EventHandler<MouseEvent> tileOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                double rectangleHeight = checkerBoard.getRectangleHeight();
                double rectangleWidth = checkerBoard.getRectangleWidth();
                double verticalPadding = (boardHeight - (numRows*rectangleHeight))/2;
                double horizontalPadding = (boardWidth - (numCols*rectangleWidth))/2;
                
                    int tileIndex = ((int)((t.getX()-horizontalPadding)/rectangleWidth) + (int)((t.getY()-verticalPadding)/rectangleHeight) * numRows);
                
                
                System.out.println("Square Index: " + tileIndex);

                if(checkerBoardStateSpace.getActiveActions() == null || checkerBoardStateSpace.getActiveActions().getMoves() == null || checkerBoardStateSpace.getActiveActions().getJumps() == null){
                    System.out.println("Turn me on First!");
                    
                }
                else{
                    System.out.println("Lists Moves: " + checkerBoardStateSpace.getActiveActions().getMoves() + "Jumps: " + checkerBoardStateSpace.getActiveActions().getJumps());
                    
                        for(int i = 0; i < checkerBoardStateSpace.getActiveActions().getMoves().size(); i++){
                            if(checkerBoardStateSpace.getActiveActions().getMoves().get(i) == tileIndex){
                                
                                checkerBoardStateSpace.getBoard().doMove(checkerBoardStateSpace.getActiveChecker(), tileIndex);
                                checkerBoardStateSpace.setActiveActions(null, null);
                                renderBoard();
                                return;
                            }
                        }
                    
                       
                        for(int i = 0; i < checkerBoardStateSpace.getActiveActions().getJumps().size(); i++)
                        {
                            if(checkerBoardStateSpace.getActiveActions().getJumps().get(i).getPath().get(checkerBoardStateSpace.getActiveActions().getJumps().get(i).getPath().size()-2) == tileIndex){
                               checkerBoardStateSpace.getBoard().doJump(checkerBoardStateSpace.getActiveChecker(), tileIndex);
                                checkerBoardStateSpace.setActiveActions(null, null);
                                renderBoard();
                                return;
                            }
                        }
                    
                    renderBoard();
                }
            }
        };

          
    private void renderBoard() {
        boardWidth = scene.getWidth();
        boardHeight = scene.getHeight() - menuBar.getHeight();
        
        stackPane.getChildren().clear();
        checkerBoard = new CheckerBoardUI(checkerBoardStateSpace, boardWidth, boardHeight, lightColor, darkColor);
        stackPane.getChildren().add(checkerBoard.build());
        
        Node nodeOut = stackPane.getChildren().get(0);
        if(nodeOut instanceof AnchorPane){
            for(Node nodeIn:((AnchorPane)nodeOut).getChildren()){
                if(nodeIn instanceof Circle){
                    nodeIn.setOnMousePressed(circleOnMousePressedEventHandler);
                }
                if(nodeIn instanceof Rectangle){
                    nodeIn.setOnMousePressed(tileOnMousePressedEventHandler);
                }
            }
        } 
    }
    
    @FXML
    public void selectSize(ActionEvent event) {
        MenuItem menuItem = (MenuItem)(event.getSource());
        switch(menuItem.getId()) {
            case "16 x 16":
                numRows = 16;
                numCols = 16;
                break;
            case "10 x 10":
                numRows = 10;
                numCols = 10;
                break;
            case "8 x 8":
                numRows = 8;
                numCols = 8;
                break;
            case "3 x 3":
                numRows = 3;
                numCols = 3;
                break;
            default:
                setDefaultRowsCols();
        }
        renderBoard();
    }
    
    @FXML
    public void selectColor(ActionEvent event) {
        MenuItem menuItem = (MenuItem)(event.getSource());
        switch(menuItem.getId()) {
            case "Blue":
                setBlueColor();
                break;
            default:
                setDefaultColor();
        }
        renderBoard();
    }

    
    private void setDefaultColor() {
        darkColor = Color.BLACK;
        lightColor = Color.RED;
    }
    
    private void setBlueColor() {
        darkColor = Color.DARKBLUE;
        lightColor = Color.SKYBLUE;
    }
    
    private void setDefaultRowsCols() {
        numRows = 8;
        numCols = 8;
    }
}
