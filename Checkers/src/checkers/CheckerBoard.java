/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author mike
 */
public class CheckerBoard {
    
    private int numRows;
    private int numCols;
    private double boardWidth;
    private double boardHeight;
    private double rectangleWidth;
    private double rectangleHeight;
    private Color lightColor = Color.RED;
    private Color darkColor = Color.BLACK;
    private AnchorPane anchorPane;
    
    public CheckerBoard(int numRows, int numCols, double boardWidth, double boardHeight) {
       this.numRows = numRows;
       this.numCols = numCols;
       this.boardWidth = boardWidth;
       this.boardHeight = boardHeight;
       
       rectangleWidth = boardWidth / numCols;
       rectangleHeight = boardHeight / numRows;
    }
    
    public CheckerBoard(int numRows, int numCols, double boardWidth, double boardHeight, Color lightColor, Color darkColor) {
       this(numRows, numCols, boardWidth, boardHeight);
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
        
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++){
                //Position and Size Rectangles
                Rectangle rectangle = new Rectangle(horizontalPadding + (col * rectangleWidth), verticalPadding + (row * rectangleHeight), rectangleWidth, rectangleHeight);
                Color color = darkColor;
                if ((col %2) == (row % 2)) {
                    color = lightColor;
                }
                rectangle.setFill(color);
                anchorPane.getChildren().add(rectangle);
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
