/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Mike
 */
public class CheckerPiece {
    private Circle checkerPiece;
    private int tileIndex;
    
    
    public CheckerPiece(Circle checkerPiece, int tileIndex){
        this.checkerPiece = checkerPiece;
        this.tileIndex = tileIndex;
                        
    }
    
    public int getIndex(){return this.tileIndex;}
    public Circle getCheckerPiece(){return this.checkerPiece;}
}
