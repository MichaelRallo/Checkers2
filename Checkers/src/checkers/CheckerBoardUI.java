/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.Collections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author mike
 */
public class CheckerBoardUI {
    double orgSceneX, orgSceneY;
                    double orgTranslateX, orgTranslateY;
    private int numRows;
    private int numCols;
    private double boardWidth;
    private double boardHeight;
    private double rectangleWidth;
    private double rectangleHeight;
    private Color lightColor = Color.RED;
    private Color darkColor = Color.BLACK;
    private Color player1Color = Color.WHITE;
    private Color player2Color = Color.GOLD;
    private AnchorPane anchorPane;
    private CheckerBoard board;
    private ActionsList possibles;
    
//                EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
//
//                    @Override
//                    public void handle(MouseEvent t) {
//                        
//                        double verticalPadding = (boardHeight - (numRows*rectangleHeight))/2;
//                        double horizontalPadding = (boardWidth - (numCols*rectangleWidth))/2;
//                        int tileIndex = ((int)((t.getX()-horizontalPadding)/rectangleWidth) + (int)((t.getY()-verticalPadding)/rectangleHeight) * numRows);
//                        
//                        System.out.println("Index: " + tileIndex);
//
//                        possibles = board.getValidMoves(tileIndex);
//                        build();
//                    
//                        orgSceneX = t.getSceneX();
//                        orgSceneY = t.getSceneY();
//                        orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
//                        orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
//                    }
//                };

//                EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
//
//                    @Override
//                    public void handle(MouseEvent t) {
//                        double offsetX = t.getSceneX() - orgSceneX;
//                        double offsetY = t.getSceneY() - orgSceneY;
//                        double newTranslateX = orgTranslateX + offsetX;
//                        double newTranslateY = orgTranslateY + offsetY;
//                        
//                        double verticalPadding = (boardHeight - (numRows*rectangleHeight))/2;
//                        double horizontalPadding = (boardWidth - (numCols*rectangleWidth))/2;
//                        int tileIndexBefore = ((int)((t.getX()-horizontalPadding)/rectangleWidth) + (int)((t.getY()-verticalPadding)/rectangleHeight) * numRows);
//                        int tileIndexAfter = ((int)((t.getSceneX()-horizontalPadding)/rectangleWidth) + (int)((t.getSceneY()-verticalPadding)/rectangleHeight) * numRows);
//                        
//                        System.out.println("Before: " + tileIndexBefore + " | After: " + tileIndexAfter);
//                         System.out.println("X: " + t.getX() + " Y: " + t.getY() + "X: " + t.getSceneX() + " Y: " + t.getSceneY());    
//                            ((Circle)(t.getSource())).setTranslateX(newTranslateX);
//                            ((Circle)(t.getSource())).setTranslateY(newTranslateY);
//                        
//                    }
//                };
                
    public void setPossibilities(ActionsList possibles){
        this.possibles = possibles;
    }
    
    public CheckerBoardUI(CheckerBoardStateSpace checkerBoardStateSpace, double boardWidth, double boardHeight) {
       
       this.board = checkerBoardStateSpace.getBoard();
       this.numRows = board.getNumRows();
       this.numCols = board.getNumcols();
       this.boardWidth = boardWidth;
       this.boardHeight = boardHeight;
       this.possibles = checkerBoardStateSpace.getActiveActions();
       rectangleWidth = boardWidth / numCols;
       rectangleHeight = boardHeight / numRows;
    }
    
    public CheckerBoardUI(CheckerBoardStateSpace checkerBoardStateSpace, double boardWidth, double boardHeight, Color lightColor, Color darkColor) {
       this(checkerBoardStateSpace, boardWidth, boardHeight);
       this.lightColor = lightColor;
       this.darkColor = darkColor;
    }
    
    // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/AnchorPane.html#AnchorPane--
    // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/shape/Rectangle.html#Rectangle--
    
    public AnchorPane build() {
        anchorPane = new AnchorPane();
        anchorPane.setPrefSize(boardWidth, boardHeight);
        
        //Ensure Rects are Always Square
        if(boardHeight > boardWidth){
            rectangleHeight = rectangleWidth;
        }
        else{
            rectangleWidth = rectangleHeight;
        }

        //Calculate the Padding Values
        double verticalPadding = (boardHeight - (numRows*rectangleHeight))/2;
        double horizontalPadding = (boardWidth - (numCols*rectangleWidth))/2;
        
        
        
        //Tiles
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++){
                //Position and Size Rectangles
                Rectangle rectangle = new Rectangle(horizontalPadding + (col * rectangleWidth), verticalPadding + (row * rectangleHeight), rectangleWidth, rectangleHeight);
                Color color = darkColor;
                
                
                
                rectangle.setOnMouseClicked((MouseEvent t) -> {
                    System.out.println("Index: " + ((rectangle.getX()-horizontalPadding) / (this.rectangleWidth)+((rectangle.getY()-verticalPadding) / (this.rectangleHeight))*numRows));
                });
                
                if ((col %2) == (row % 2)) {
                    color = lightColor;
                }
                
                int tileIndex = col+(row*numCols);
                if(possibles !=null && possibles.getMoves() !=null && possibles.getJumps() != null){
                    if(possibles.getMoves().contains(tileIndex)){
                        color = Color.GOLD;
                    }
                    for(int i = 0; i < possibles.getJumps().size(); i++)
                    {
                        if(possibles.getJumps().get(i).getPath().get(possibles.getJumps().get(i).getPath().size()-2) == tileIndex){
                           color = Color.GOLD; 
                        }
                    }
                }
                rectangle.setFill(color);
                anchorPane.getChildren().add(rectangle);

            }
        }
        
        //Circles    
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++){
                Circle circle = new Circle(horizontalPadding + (col * rectangleWidth) + rectangleWidth/2, verticalPadding + (row * rectangleHeight) + rectangleHeight/2, rectangleWidth/3);

                circle.setCursor(Cursor.HAND);
                //circle.setOnMousePressed(circleOnMousePressedEventHandler);
                //circle.setOnMouseDragged(circleOnMouseDraggedEventHandler);
                CheckerPiece checkerPiece = new CheckerPiece(circle, col+(row*numCols));
                

                switch(this.board.getTiles()[col+(row*numCols)].getContent()){
                    case PLAYER1CHECKER:
                        circle.setFill(player1Color);
                        break;
                    case PLAYER2CHECKER:
                        circle.setFill(player2Color);
                        break;
                    case PLAYER1KING: 
                        circle.setFill(player1Color);
                        break;
                    case PLAYER2KING:
                        circle.setFill(player2Color);
                        break;
                    default:circle.setFill(null);break;
                }
                if(circle.getFill() != null){
                    anchorPane.getChildren().add(circle);
                }
            }
        }        
        
        

        return anchorPane;
    }
    
 
    public AnchorPane getBoard() {
        return anchorPane;
    }
    
    public int getNumRows() {
        return numRows;
    }
    
    public int getNumCols() {
        return numCols;
    }
    
    public double getWidth() {
        return boardWidth;
    }
    
    public double getHeight() {
        return boardHeight;
    }
    
    public double getRectangleWidth() {
        return rectangleWidth;
    }
    
    public double getRectangleHeight() {
        return rectangleHeight;
    }
    
    public Color getLightColor() {
        return lightColor;
    }
    
    public Color getDarkColor() {
        return darkColor;
    }
    
}
//                checkerPiece.getCheckerPiece().setOnMouseClicked((MouseEvent t) -> {
//                    System.out.println("Checker Index: " + checkerPiece.getIndex());
//                    
//                    ArrayList<Integer> validMoves = board.getValidMoves(checkerPiece.getIndex());
//                    ArrayList<JumpType> validJumps = board.getValidJumps(checkerPiece.getIndex(), board.getTiles()[checkerPiece.getIndex()].getContent(), new JumpType(checkerPiece.getIndex()));
//
//                    
//                    
//                    for(int i = 0; i < validMoves.size(); i++){
//                        System.out.print(validMoves.get(i) + " ");
//                    }
//                    for(int i = 0; i < validJumps.size(); i++){
//                        Collections.reverse(validJumps.get(i).getPath());
//                        System.out.print(validJumps.get(i).getPath().get(validJumps.get(i).getPath().size()-2) + " ");
//                    }
//                    
//                });