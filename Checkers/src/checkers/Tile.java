/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;
import static checkers.TileContent.*;

/**
 *
 * @author Mike
 */
public class Tile {
    
    TileContent content;
    
    public Tile(TileContent content){
        this.content = content;
    }
    
    public TileContent getContent(){
        return content;
    }
    
    public void setContent(TileContent content){
        this.content = content;
    }
    
    public boolean isOpen(){
        return (content == EMPTY);
    }
    
    
    public boolean isEnemy(TileContent startingChecker){

        if(startingChecker == PLAYER1CHECKER || startingChecker == PLAYER1KING){
            return(content == PLAYER2CHECKER || this.content == PLAYER2KING);
        }

        if(startingChecker == PLAYER2CHECKER || startingChecker == PLAYER2KING){
            return (this.content == PLAYER1CHECKER || this.content == PLAYER1KING);
        }
            
        return false;
    } 
    
    public boolean isValid(){
        return(content != NOTVALID);
    }
    
}
