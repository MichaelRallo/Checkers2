/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

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
    public double getOrgSceneX(){return this.orgSceneX;}
    public double getOrgSceneY(){return this.orgSceneY;}
    public double getOrgTranslateX(){return this.orgTranslateX;}
    public double getOrgTranslateY(){return this.orgTranslateY;}
    public void setOrgSceneX(double value){this.orgSceneX = value;}
    public void setOrgSceneY(double value){this.orgSceneY= value;}
    public void setOrgTranslateX(double value){this.orgTranslateX= value;}
    public void setOrgTranslateY(double value){this.orgTranslateY= value;}
    
    public CheckerBoardStateSpace(CheckerBoard board){
        this.board = board;
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
    
    
}
