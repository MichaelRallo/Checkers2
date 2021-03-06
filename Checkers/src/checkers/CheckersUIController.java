/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import static checkers.State.*;
import static checkers.TileContent.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    @FXML
    private VBox vBox;
    @FXML
    private AnchorPane playerTopPane;
    @FXML
    private AnchorPane playerBottomPane;
    @FXML
    private Label playerTopLabel;
    @FXML
    private Button playerTopButton;
    @FXML
    private Label playerBottomLabel;
    @FXML
    private Button playerBottomButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDefaultColor();
        setDefaultRowsCols();
        checkerBoardStateSpace = new CheckerBoardStateSpace(new CheckerBoard(numRows, numCols));
//        checkerBoardStateSpace.getBoard().placeChecker(26, TileContent.PLAYER1KING);
//        checkerBoardStateSpace.getBoard().placeChecker(1, TileContent.PLAYER1KING);
//        checkerBoardStateSpace.getBoard().placeChecker(19, TileContent.PLAYER2KING);
//        checkerBoardStateSpace.getBoard().placeChecker(21, TileContent.PLAYER2KING);
//        checkerBoardStateSpace.getBoard().placeChecker(37, TileContent.PLAYER2KING);
//        checkerBoardStateSpace.getBoard().placeChecker(35, TileContent.PLAYER2KING);
//
//        checkerBoardStateSpace.getBoard().placeChecker(26, TileContent.PLAYER1KING);
//        checkerBoardStateSpace.getBoard().placeChecker(1, TileContent.PLAYER1KING);
//        checkerBoardStateSpace.getBoard().placeChecker(19, TileContent.PLAYER2CHECKER);
//        checkerBoardStateSpace.getBoard().placeChecker(21, TileContent.PLAYER2CHECKER);
//        checkerBoardStateSpace.getBoard().placeChecker(37, TileContent.PLAYER2KING);
//        checkerBoardStateSpace.getBoard().placeChecker(35, TileContent.PLAYER2CHECKER);



            checkerBoardStateSpace.getBoard().populateDefault();

//            checkerBoardStateSpace.getBoard().placeChecker(26, TileContent.PLAYER1KING);
//            checkerBoardStateSpace.getBoard().placeChecker(17, TileContent.PLAYER2KING);
//            checkerBoardStateSpace.getBoard().placeChecker(19, TileContent.PLAYER2KING);
//            checkerBoardStateSpace.getBoard().placeChecker(21, TileContent.PLAYER2KING);
//            checkerBoardStateSpace.getBoard().placeChecker(37, TileContent.PLAYER2KING);
//            checkerBoardStateSpace.getBoard().placeChecker(56, TileContent.PLAYER2KING);
//            checkerBoardStateSpace.getBoard().placeChecker(35, TileContent.PLAYER2KING);
//            
                
    }   
    
    public void ready(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
        
        
        ChangeListener<Number> listener = (ObservableValue<? extends Number> observable, Number oldValue, final Number newValue) -> {
            renderBoard();
        };
        
        vBox.getStyleClass().add("mainPane");
        playerTopLabel.getStyleClass().add("playerLabels");
        playerBottomLabel.getStyleClass().add("playerLabels");
        
        scene.widthProperty().addListener(listener);
        scene.heightProperty().addListener(listener);
        
        checkerBoardStateSpace.setPlayerFirstToMove(1);
        checkerBoardStateSpace.setAIPlayer(2);
       
        renderBoard();
    }
    
    EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent t) {
            int selectedChecker = checkerBoard.getCircleIndex((Circle)t.getSource());
            //If a Checker is Selected, Display Actions the Checker can make (If it is of the current player's turn type).
            if(checkerBoardStateSpace.getState() == IDLE && checkerBoardStateSpace.getBoard().matchesPlayer(checkerBoardStateSpace.getPlayerTurn(), selectedChecker)){
                checkerBoardStateSpace.setActiveChecker(selectedChecker);
                checkerBoardStateSpace.setActiveActions(selectedChecker);
                checkerBoardStateSpace.setState(CHECKERACTIVE);
                renderBoard();
            }
            //If the Same Checker that is Selected is Selected Again, UnSelect and Return.
            else if(checkerBoardStateSpace.getState() == CHECKERACTIVE && selectedChecker == checkerBoardStateSpace.getActiveChecker()){
                checkerBoardStateSpace.clearActiveActions();
                checkerBoardStateSpace.setState(IDLE);
                renderBoard();
            }
        }
    };
          
    EventHandler<MouseEvent> tileOnMousePressedEventHandler = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent t){

            int selectedTile = checkerBoard.getRectangleIndex((Rectangle)t.getSource());
            System.out.println("Tile Clicked: " + selectedTile);
            if(checkerBoardStateSpace.getState() == CHECKERACTIVE || checkerBoardStateSpace.getState() == JUMPING){

                        System.out.println("Boop");
                //Ensures no Normal Movement can be done while in a jump and that Move is Valid
                if(checkerBoardStateSpace.getState() != JUMPING && checkerBoardStateSpace.getActiveActions().getMoves().contains(selectedTile)){

                    //Perform the Move        
                    checkerBoardStateSpace.getBoard().doMove(checkerBoardStateSpace.getActiveChecker(), selectedTile);
                    checkerBoardStateSpace.setState(INMOTION);
                    TranslateTransition translate = checkerBoard.animateMove(stackPane, checkerBoardStateSpace.getActiveChecker(), selectedTile);
                    translate.setOnFinished((final ActionEvent arg0) -> {
                        checkerBoardStateSpace.clearActiveActions();
                        checkerBoardStateSpace.togglePlayerTurn();
                        renderBoard();
                        if(checkerBoardStateSpace.getAIPlayer() == checkerBoardStateSpace.getPlayerTurn()){
                            checkerBoardStateSpace.setState(THINKING);
                            AIMan ai = new AIMan();
                            doAiMove(ai.processBoard(checkerBoardStateSpace));
                        }
                        else{
                            checkerBoardStateSpace.setState(IDLE);
                        }
                    });
                    translate.play();
                    return;
                }

             
                for(int j = 0; j < checkerBoardStateSpace.getActiveActions().getJumps().size(); j++){
                    System.out.println("Jumps be: " + checkerBoardStateSpace.getActiveActions().getJumps().get(j).getPath());
                        
                    if(checkerBoardStateSpace.getActiveActions().getJumps().get(j).getPath().get(1) == selectedTile){ 
                        //Pop Jump from JumpPath
                        checkerBoardStateSpace.getActiveActions().getJumps().get(j).getPath().remove(0); 
                        System.out.println("Active Checker: " + checkerBoardStateSpace.getActiveChecker() + " to " + selectedTile);
                        checkerBoardStateSpace.getBoard().doJump(checkerBoardStateSpace.getActiveChecker(), selectedTile);
                        checkerBoard.getCheckerCircleByIndex(stackPane, checkerBoardStateSpace.getActiveChecker()).toFront();
                        checkerBoardStateSpace.setState(INMOTION);
                        TranslateTransition translate = checkerBoard.animateJump(stackPane, checkerBoardStateSpace.getActiveChecker(), selectedTile);
                        translate.setOnFinished((final ActionEvent arg0) -> {
                            checkerBoardStateSpace.setState(JUMPING);
                            checkerBoardStateSpace.setActiveChecker(selectedTile);
                            checkerBoardStateSpace.setActiveActions(selectedTile);
                            checkerBoardStateSpace.clearActiveMoves();
                            renderBoard();
                            if(checkerBoardStateSpace.getActiveActions().getJumps().isEmpty()){
                                checkerBoardStateSpace.clearActiveActions();
                                checkerBoardStateSpace.togglePlayerTurn();
                                renderBoard();
                                if(checkerBoardStateSpace.getAIPlayer() == checkerBoardStateSpace.getPlayerTurn()){
                                    checkerBoardStateSpace.setState(THINKING);
                                    AIMan ai = new AIMan();
                                    doAiMove(ai.processBoard(checkerBoardStateSpace));
                                }
                                else{
                                    checkerBoardStateSpace.setState(IDLE);
                                }
                            }
                        });
                        translate.play();
                        return;
                    } 
                }   
                
            }
        }
    };
          
          
    
    public void doAiMove(CheckerBoardStateSpace idealState){
        if(idealState == null){
            System.out.println("SOMETHINGS WAY WRONG!!!");
            return;
        }
        if(idealState.getIdealMove() !=null && !idealState.getIdealMove().isEmpty()){
            //System.out.println("Found ideal move: " + idealState.getIdealMove().get(0) + ", " + idealState.getIdealMove().get(1));
            TranslateTransition translate = checkerBoard.animateMove(stackPane, idealState.getIdealMove().get(0), idealState.getIdealMove().get(1));
            translate.setOnFinished((final ActionEvent arg0) -> {
                checkerBoardStateSpace.getBoard().doMove(idealState.getIdealMove().get(0), idealState.getIdealMove().get(1));
                checkerBoardStateSpace.clearActiveActions();
                checkerBoardStateSpace.togglePlayerTurn();
                checkerBoardStateSpace.setState(IDLE);
                renderBoard(); 
            });
            translate.play();
        }
        if(idealState.getIdealJump() !=null && idealState.getIdealJump().getPath().size() > 1){
            //System.out.println("Found Ideal Jump!" + idealState.getIdealJump().getPath().get(0) + ", " + idealState.getIdealJump().getPath().get(1));
            TranslateTransition translate = checkerBoard.animateJump(stackPane, idealState.getIdealJump().getPath().get(0), idealState.getIdealJump().getPath().get(1));
            translate.setOnFinished((final ActionEvent arg1) -> {
                checkerBoardStateSpace.getBoard().doJump(idealState.getIdealJump().getPath().get(0), idealState.getIdealJump().getPath().get(1));
                idealState.getIdealJump().getPath().remove(0);
                renderBoard(); 
                doAiMove(idealState);
                if(idealState.getIdealJump().getPath().size() == 1){
                    checkerBoardStateSpace.clearActiveActions();
                    checkerBoardStateSpace.togglePlayerTurn();
                    checkerBoardStateSpace.setState(IDLE);
                    renderBoard(); 
                }
            });
            translate.play();        
        }
    }
          
    private void renderBoard() {
        boardWidth = scene.getWidth();
        boardHeight = scene.getHeight() - menuBar.getHeight() - playerTopPane.getHeight()- playerBottomPane.getHeight();
        
        if(checkerBoardStateSpace.getPlayerTurn() == 1){
            if(checkerBoardStateSpace.getBoard().hasWinner()){
                playerBottomLabel.setText("Player 1 - Loser!");
                playerTopLabel.setText("Player 2 - Winner!");
                return;
            }
            playerBottomLabel.setText("Player 1 - Your Move!");
            playerTopLabel.setText("Player 2");
        }
        else{
            if(checkerBoardStateSpace.getBoard().hasWinner()){
                playerBottomLabel.setText("Player 1 - Winner!");
                playerTopLabel.setText("Player 2 - Loser!");
                return;
            }
            playerTopLabel.setText("Player 2 - Your Move!");
            playerBottomLabel.setText("Player 1");
        }
        
        if(boardHeight > boardWidth/2){
            AnchorPane.setRightAnchor(playerTopButton, 10.0);
            AnchorPane.setLeftAnchor(playerTopLabel, 10.0);
            playerTopLabel.setAlignment(Pos.CENTER_LEFT);
            AnchorPane.setRightAnchor(playerBottomButton, 10.0);
            AnchorPane.setLeftAnchor(playerBottomLabel, 10.0);
            playerBottomLabel.setAlignment(Pos.CENTER_LEFT);
        }
        else{
            AnchorPane.setRightAnchor(playerTopButton, 30.0);
            AnchorPane.setLeftAnchor(playerTopLabel, 0.0);
            playerTopLabel.setAlignment(Pos.CENTER);
            AnchorPane.setRightAnchor(playerBottomButton, 30.0);
            AnchorPane.setLeftAnchor(playerBottomLabel, 0.0);
            playerBottomLabel.setAlignment(Pos.CENTER);
        }
        stackPane.getChildren().clear();
        
        checkerBoard = new CheckerBoardUI(checkerBoardStateSpace, boardWidth, boardHeight, lightColor, darkColor);
        AnchorPane workingBoard = checkerBoard.build();
        stackPane.getChildren().add(workingBoard);
        
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
