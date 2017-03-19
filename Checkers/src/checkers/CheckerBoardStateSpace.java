/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import static checkers.State.*;
import static checkers.TileContent.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Mike
 */
public class CheckerBoardStateSpace {
    
    //Board State Stuff
    State state;
    CheckerBoard board;
    ArrayList<CheckerBoardStateSpace> children;
    
    //Player Turns
    public int playerAI = 0;
    public int playerTurn = 2;
    
    //To Hold Player Decisions
    ActionsList activeActions;
    private int activeChecker;

    //To Hold AI's Decisions
    public JumpType idealJump;
    public ArrayList<Integer> idealMove;
   
   //Constructor
    public CheckerBoardStateSpace(CheckerBoard board){
        this.board = board;
        this.state = IDLE;
        this.children = new ArrayList<>();
        this.activeActions = new ActionsList(new ArrayList<>(), new ArrayList<>());
    }
    
    //********************Getters And Setters********************//
    //Board
    public CheckerBoard getBoard(){return this.board;}
    
    //State
    public State getState(){return this.state;}
    public void setState(State state){this.state = state;}
    public int getPlayerTurn(){return this.playerTurn;}
    public ActionsList getActiveActions(){return this.activeActions;}
    
    //Player Selections
    public int getActiveChecker(){return this.activeChecker;}
    public void setActiveChecker(int index){this.activeChecker = index;}
    
    //AI Player Id
    public int getAIPlayer(){return this.playerAI;}
    public void setAIPlayer(int value){this.playerAI = value;}
    
    //AI Jumps/Moves
    public JumpType getIdealJump(){return this.idealJump;}
    public void setIdealJump(JumpType path){this.idealJump = path;}
    public ArrayList<Integer> getIdealMove(){return this.idealMove;}
    public void setIdealMove(ArrayList<Integer> path){this.idealMove = path;}
    
    //StateSpace Childrens (For AI)
    public ArrayList<CheckerBoardStateSpace> getChildren(){return this.children;}
    //**********************************************************************//
    
    //Toggle Player Turn
    public void togglePlayerTurn(){
        if(this.playerTurn == 1){playerTurn = 2;}
        else{playerTurn = 1;}
    }
    
    //Set Player First to Move
    public void setPlayerFirstToMove(int player){
        this.playerTurn = player;
    }
    
    //Set Actions for selected checker
    public void setActiveActions(int index){
        ArrayList<Integer> activeMoves = board.getValidMoves(index);
        ArrayList<JumpType> activeJumps = board.getValidJumps(index, board.getTiles()[index].getContent(),  new JumpType(index));
        this.activeActions = new ActionsList(activeMoves, activeJumps);
    }   
    
    //Clear Active Actions
    public void clearActiveMoves(){this.activeActions.getMoves().clear();}
    public void clearActiveJumps(){this.activeActions.getJumps().clear();}
    public void clearActiveActions(){clearActiveMoves();clearActiveJumps();}
    
    
    //Children States (For AI)
    public void generateChildren(int playerID){
        this.children.clear();
        
        //For each Ai Checker, Calculate Move!
        for(int i = 0; i < board.getNumCols()*board.getNumRows(); i++){
            if(this.board.matchesPlayer(playerID, i)){
                
                //Get All Actions for the checker
                ActionsList workingActions = new ActionsList(board.getValidMoves(i), 
                            board.getValidJumps(i, board.getTiles()[i].getContent(),  new JumpType(i)));
                //If It has moves, it is a possible StateSpace!
                for(int m = 0; m < workingActions.getMoves().size(); m++){
                    CheckerBoardStateSpace childStateSpace= new CheckerBoardStateSpace(new CheckerBoard(board));
                    childStateSpace.getBoard().doMove(i,  workingActions.getMoves().get(m));
                    childStateSpace.setIdealMove(new ArrayList<>(Arrays.asList(i, workingActions.getMoves().get(m))));
                    this.children.add(childStateSpace);
                }

                //If It has jumps, it is a possible StateSpace!
                for(int j = 0; j < workingActions.getJumps().size(); j++){
                    CheckerBoardStateSpace childStateSpace= new CheckerBoardStateSpace(new CheckerBoard(board));
                    for(int p = 0; p < workingActions.getJumps().get(j).getPath().size()-1; p++){
                        childStateSpace.getBoard().doJump(workingActions.getJumps().get(j).getPath().get(p), workingActions.getJumps().get(j).getPath().get(p+1));
                    }
                    childStateSpace.setIdealJump(workingActions.getJumps().get(j));
                    this.children.add(childStateSpace);
                }
            }
        }   
    }

}
