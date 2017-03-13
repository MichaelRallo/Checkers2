/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Mike
 */
public class ActionsList {
    ArrayList<Integer> moves;
    ArrayList<JumpType> jumps;
    
    public ActionsList(ArrayList<Integer> moves, ArrayList<JumpType> jumps){
        this.moves = moves;
        this.jumps = jumps;
    }
    
    public ArrayList<Integer> getMoves(){
        return this.moves;
    }
    public ArrayList<JumpType> getJumps(){
        return this.jumps;
    }
    
}
