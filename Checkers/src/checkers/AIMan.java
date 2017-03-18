/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import static checkers.TileContent.PLAYER1CHECKER;
import static checkers.TileContent.PLAYER1KING;
import static checkers.TileContent.PLAYER2CHECKER;
import static checkers.TileContent.PLAYER2KING;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Mike
 */
public class AIMan {
    
    public int playerID;
    public CheckerBoardStateSpace processBoard(CheckerBoardStateSpace checkerBoardStateSpace){
        
        this.playerID = checkerBoardStateSpace.getAIPlayer();
        int numRows = checkerBoardStateSpace.getBoard().getNumRows();
        int numCols = checkerBoardStateSpace.getBoard().getNumCols();
        Tile[] tiles = checkerBoardStateSpace.getBoard().getTiles();
        
        System.out.println("Children's Scores: ");
        checkerBoardStateSpace.generateChildren(playerID);
        for(CheckerBoardStateSpace child : checkerBoardStateSpace.getChildren()){
            System.out.println("Child: " + child + " Score: " + child.getBoard().getScore(playerID));
        }
        
        System.out.println("Max Tile is: " + getMaxChild(checkerBoardStateSpace.getChildren(), playerID).getActiveChecker());
         System.out.println("Max Path is: " + getMaxChild(checkerBoardStateSpace.getChildren(), playerID));
        
        return getMaxChild(checkerBoardStateSpace.getChildren(), playerID);
    }
    
    public CheckerBoardStateSpace getMaxChild(ArrayList<CheckerBoardStateSpace> children, int player){
        if(children.isEmpty()){return null;}
        CheckerBoardStateSpace max = children.get(0);
        for(int i = 0; i < children.size(); i++){
            if(max.getBoard().getScore(player) < children.get(i).getBoard().getScore(player)){
                max = children.get(i);
            } if(max.getBoard().getScore(player) == children.get(i).getBoard().getScore(player)){
                
                Random rand = new Random();
                int  n = rand.nextInt(40);
                if(n%2 == 0){
                    max = children.get(i);
                }
            }
        }
        return max;
        
    }
}
