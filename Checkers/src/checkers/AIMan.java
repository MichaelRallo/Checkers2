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
        
        //System.out.println("Children's Scores: ");
        checkerBoardStateSpace.generateChildren(playerID);
        for(CheckerBoardStateSpace child : checkerBoardStateSpace.getChildren()){
            //System.out.println("Child: " + child + " Score: " + child.getBoard().getScore(playerID));
        }
    
        return getMaxChild(checkerBoardStateSpace.getChildren(), playerID);
    }
    
    
    //Get the Max Child
    public CheckerBoardStateSpace getMaxChild(ArrayList<CheckerBoardStateSpace> children, int player){
        if(children.isEmpty()){return null;}
        ArrayList<CheckerBoardStateSpace> maxList = new ArrayList<>();
        maxList.add(children.get(0));
        for(int i = 0; i < children.size(); i++){
            
            if(maxList.get(0).getBoard().getScore(player) < children.get(i).getBoard().getScore(player)){
                maxList.clear();
                maxList.add(children.get(i));
            } 
            if(maxList.get(0).getBoard().getScore(player) == children.get(i).getBoard().getScore(player)){
                maxList.add(children.get(i));
            }
        }
        
        Random rand = new Random();
        return maxList.get(rand.nextInt(maxList.size()-1));
        
    }
}
