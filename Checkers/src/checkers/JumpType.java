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
public class JumpType {
    private ArrayList<Integer> enemiesDestroyed;
    private ArrayList<Integer> path;
    
    public JumpType(){
        enemiesDestroyed = new ArrayList<>();
        path = new ArrayList<>();
    }
    public JumpType(int pathIndex){
        this();
        this.path.add(pathIndex);
    }
    
    public JumpType(int enemyIndex, int pathIndex){
        this();
        this.enemiesDestroyed.add(enemyIndex);
        this.path.add(pathIndex);
    } 
    public JumpType(int enemyIndex, int pathIndex, JumpType previousJump){
        this();
        this.enemiesDestroyed.addAll(previousJump.getEnemiesDestroyed());
        this.path.addAll(previousJump.getPath());
        this.enemiesDestroyed.add(enemyIndex);
        this.path.add(pathIndex);
    }
    
    public void addToPath(int index){
        path.add(index);
    }
    public void addToPath(ArrayList<Integer> path){
        this.path.addAll(path);
    }
    
    public ArrayList<Integer> getPath(){return this.path;}
    
    public void addToEnemiesDestroyed(int index){
        this.enemiesDestroyed.add(index);
    }
    public void addToEnemiesDestroyed(ArrayList<Integer> enemies){
        this.enemiesDestroyed.addAll(enemies);
    }
    
    public ArrayList<Integer> getEnemiesDestroyed(){return this.enemiesDestroyed;}
}
