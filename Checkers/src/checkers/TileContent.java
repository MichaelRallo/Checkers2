/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

/**
 *
 * @author Mike
 */
public enum TileContent {
    EMPTY(         "EE"), 
    NOTVALID(      "NV"),
    PLAYER1CHECKER("P1"), 
    PLAYER2CHECKER("P2"), 
    PLAYER1KING(   "K1"),  
    PLAYER2KING(   "K2");
    
    private final String name; 
    private TileContent(String s) {
        name = s;
    }
    @Override
    public String toString() {
       return this.name;
    }
    
}
