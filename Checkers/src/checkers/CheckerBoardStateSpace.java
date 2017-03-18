/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import static checkers.State.IDLE;
import static checkers.State.JUMPING;
import static checkers.State.MOVING;
import static checkers.TileContent.PLAYER1CHECKER;
import static checkers.TileContent.PLAYER1KING;
import static checkers.TileContent.PLAYER2CHECKER;
import static checkers.TileContent.PLAYER2KING;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Mike
 */
public class CheckerBoardStateSpace {
    
    CheckerBoard board;
    ArrayList<CheckerBoardStateSpace> children;
    ActionsList activeActions;
    private int activeChecker;
    State state;
    public int playerTurn = 2;
    public int playerAI = 0;
    public boolean aiEnabled;
    
   
    private void enableAI(){this.aiEnabled = true;}
    public boolean aiIsEnabled(){return this.aiEnabled;}
    public int getAIPlayer(){return this.playerAI;}
    public void setAIPlayer(int value){this.playerAI = value;enableAI();}
    
    public JumpType idealJump;
    public ArrayList<Integer> idealMove;
    
    public void setIdealJump(JumpType path){this.idealJump = path;}
    public JumpType getIdealJump(){return this.idealJump;}
    
    public void setIdealMove(ArrayList<Integer> path){this.idealMove = path;}
    public ArrayList<Integer> getIdealMove(){return this.idealMove;}
    
    
    
    public State getState(){
        return this.state;
    }
    
    public int getPlayerTurn(){return this.playerTurn;}
    public void togglePlayerTurn(){
        if(this.playerTurn == 1){playerTurn = 2;}
        else{playerTurn = 1;}
    }
        
    public void setState(State state){this.state = state;}
    
    public CheckerBoardStateSpace(CheckerBoard board){
        this.board = board;
        this.state = IDLE;
        this.children = new ArrayList<>();
    }
    
    public void generateChildren(int playerID){
        this.children.clear();
        System.out.println("Generating!");
        int numRows = board.getNumRows();
        int numCols = board.getNumCols();
        
        Tile[] tiles = board.getTiles();
        
        
        //For each Ai Checker, Calculate Move!
        for(int row = 0; row < numRows; row++){
            for(int col = 0; col < numCols; col++){
                int index = row + col*numRows;
                //Target Ai's Checker
                if((playerID == 1) && (tiles[index].getContent() == PLAYER1CHECKER || tiles[index].getContent() == PLAYER1KING)){
                    //If It has moves, it is a possible StateSpace!
                    ActionsList workingActions = new ActionsList(board.getValidMoves(index), board.getValidJumps(index, board.getTiles()[index].getContent(),  new JumpType(index)));
                    for(int i = 0; i < workingActions.getMoves().size(); i++){
                        CheckerBoardStateSpace childStateSpace= new CheckerBoardStateSpace(new CheckerBoard(board));
                        childStateSpace.getBoard().doMove(activeChecker,  workingActions.getMoves().get(i));
                        childStateSpace.setActiveChecker(workingActions.getMoves().get(i));
                        childStateSpace.setActiveActions(workingActions.getMoves().get(i));
                        childStateSpace.setIdealMove(new ArrayList<Integer>(Arrays.asList(index, workingActions.getMoves().get(i))));
                        childStateSpace.setState(MOVING);
                        this.children.add(childStateSpace);
                    }
                    //If it has jumps, it is a possible StateSpace!
                    for(int i = 0; i < workingActions.getJumps().size(); i++){
                        System.out.println("JUMPZ: " + workingActions.getJumps().get(i).getPath());
                        CheckerBoardStateSpace childStateSpace= new CheckerBoardStateSpace(new CheckerBoard(board));
                        for(int k = 0; k < workingActions.getJumps().get(i).getPath().size()-1; k++){
                            System.out.println("Jumpinh from:" + workingActions.getJumps().get(i).getPath().get(k) + " to:" + workingActions.getJumps().get(i).getPath().get(k+1));
                            childStateSpace.getBoard().doJump(workingActions.getJumps().get(i).getPath().get(k), workingActions.getJumps().get(i).getPath().get(k+1));
                        }
                        childStateSpace.setState(JUMPING);
                        childStateSpace.setIdealJump(workingActions.getJumps().get(i));
                        childStateSpace.setActiveChecker(workingActions.getJumps().get(i).getPath().get(1));
                        this.children.add(childStateSpace);
                    }
                }
            }
        }
    }
    
    public CheckerBoard getBoard(){
        return this.board;
    }
    
    public ArrayList<CheckerBoardStateSpace> getChildren(){
        return this.children;
    }
    
    public ActionsList getActiveActions(){
        return this.activeActions;
        
    }
    
    public void setActiveChecker(int index){this.activeChecker = index;}
    public int getActiveChecker(){return this.activeChecker;}
    public void setActiveActions(int index){
        ArrayList<Integer> activeMoves = board.getValidMoves(index);
        ArrayList<JumpType> activeJumps = board.getValidJumps(index, board.getTiles()[index].getContent(),  new JumpType(index));
        this.activeActions = new ActionsList(activeMoves, activeJumps);
    }
    public void clearActiveActions(){
        this.activeActions = null;
    }
    public void clearActiveMoves(){
        this.activeActions.getMoves().clear();
    }
    
    
}
