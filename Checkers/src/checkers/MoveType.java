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
public class MoveType {
    private ArrayList<Integer> path;
    
    public MoveType(int startingIndex, int endingIndex){
        path = new ArrayList<>();
        path.add(startingIndex);
        path.add(endingIndex);
    }
    
    
    

}
