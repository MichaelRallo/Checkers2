/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import static checkers.State.IDLE;
import java.util.ArrayList;

/**
 *
 * @author Mike
 */
public class CheckerBoardStateSpace {
    
    CheckerBoard board;
    ArrayList<CheckerBoard> children;
    ActionsList activeActions;
    private int activeChecker;
    double orgSceneX;
    double orgSceneY;
    double orgTranslateX;
    double orgTranslateY;
    State state;
    
    public State getState(){
        return this.state;
    }
    public void setState(State state){this.state = state;}
    
    public CheckerBoardStateSpace(CheckerBoard board){
        this.board = board;
        this.state = IDLE;
    }
    
    public void generateChildren(){
       
    }
    
    public CheckerBoard getBoard(){
        return this.board;
    }
    
    public ArrayList<CheckerBoard> getChildren(){
        return this.children;
    }
    
    public ActionsList getActiveActions(){
        return this.activeActions;
    }
    
    public void setActiveChecker(int index){this.activeChecker = index;}
    public int getActiveChecker(){return this.activeChecker;}
    public void setActiveActions(ArrayList<Integer> activeMoves, ArrayList<JumpType> activeJumps){
        this.activeActions = new ActionsList(activeMoves, activeJumps);
    }
    public void clearActiveActions(){
        this.activeActions = null;
    }
    
    
}
