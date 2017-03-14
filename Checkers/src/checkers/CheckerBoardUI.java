/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
    double verticalPadding;
    double horizontalPadding;
    
    public Circle getCheckerCircleByIndex(StackPane stackPane, int index){
        Node nodeOut = stackPane.getChildren().get(0);
        if(nodeOut instanceof AnchorPane){
            for(Node nodeIn:((AnchorPane)nodeOut).getChildren()){
                if(nodeIn instanceof Circle){
                    if(getCircleIndex((Circle)nodeIn) == index){
                        return (Circle)nodeIn;
                    }
                }
            }
        } 
        return null;
    }
    
    public Rectangle getRectangleByIndex(StackPane stackPane, int index){
        Node nodeOut = stackPane.getChildren().get(0);
        if(nodeOut instanceof AnchorPane){
            for(Node nodeIn:((AnchorPane)nodeOut).getChildren()){
                if(nodeIn instanceof Rectangle){
                    if(getRectangleIndex((Rectangle)nodeIn) == index){
                        return (Rectangle)nodeIn;
                    }
                }
            }
        } 
        return null;
    }
    
    public int getCircleIndex(Circle circle){
        int index = ((int)((circle.getCenterX()-horizontalPadding)/rectangleWidth) + (int)((circle.getCenterY()-verticalPadding)/rectangleHeight) * numRows);
        return index;
    }
    
    public int getRectangleIndex(Rectangle rect){
        int tileIndex = ((int)((rect.getX()-horizontalPadding)/rectangleWidth) + (int)((rect.getY()-verticalPadding)/rectangleHeight) * numRows);
        return tileIndex;
    }
    
    public double getTransX(StackPane stackPane, int startingTile, int endingTile){
        return getRectangleByIndex(stackPane, endingTile).getX() - getRectangleByIndex(stackPane, startingTile).getX();
    }
    public double getTransY(StackPane stackPane, int startingTile, int endingTile){
        return getRectangleByIndex(stackPane, endingTile).getY() - getRectangleByIndex(stackPane, startingTile).getY();
    }
    
    public CheckerBoardUI(CheckerBoardStateSpace checkerBoardStateSpace, double boardWidth, double boardHeight) {
       
       this.board = checkerBoardStateSpace.getBoard();
       this.numRows = board.getNumRows();
       this.numCols = board.getNumcols();
       this.boardWidth = boardWidth;
       this.boardHeight = boardHeight;
       this.possibles = checkerBoardStateSpace.getActiveActions();
       this.rectangleWidth = boardWidth / numCols;
       this.rectangleHeight = boardHeight / numRows;
    }
    
    public CheckerBoardUI(CheckerBoardStateSpace checkerBoardStateSpace, double boardWidth, double boardHeight, Color lightColor, Color darkColor) {
       this(checkerBoardStateSpace, boardWidth, boardHeight);
       this.lightColor = lightColor;
       this.darkColor = darkColor;
    }
    
    
    
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
        System.out.println("HB:" + horizontalPadding);
         verticalPadding = (boardHeight - (numRows*rectangleHeight))/2;
         horizontalPadding = (boardWidth - (numCols*rectangleWidth))/2;
         System.out.println("HA:" + horizontalPadding);
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
