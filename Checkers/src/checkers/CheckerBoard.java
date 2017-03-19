/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import static checkers.TileContent.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Mike
 */
public class CheckerBoard {
    
    Tile[] tiles;
    private int numRows;
    private int numCols;
    private final int maxIndex;
    
    //Rows and Cols
    public int getNumRows(){return this.numRows;}
    public void setNumRows(int rows){this.numRows = rows;} 
    public int getNumCols(){return this.numCols;}  
    public void setNumCols(int cols){this.numCols = cols;} 
    
    //Tiles for Rows and Cols
    public Tile[] getTiles(){return this.tiles;}
    
    
    //Constructors
    public CheckerBoard(int rows, int cols){
        this.numRows = rows;
        this.numCols = cols;
        this.maxIndex = rows*cols;
        tiles = new Tile[maxIndex];
        
        for(int i = 0; i < maxIndex; i++){
            if(((i/rows)%2 == 0 && i%2 == 0) || (i/rows)%2 == 1 && i%2 == 1){
                tiles[i] = new Tile(NOTVALID);
            }
            else{
                tiles[i] = new Tile(EMPTY);
            }
        }
    }
    //Copies board
    public CheckerBoard(CheckerBoard boardToCopy){
        this(boardToCopy.getNumRows(), boardToCopy.getNumCols());
        for(int i = 0; i < this.maxIndex; i++){
            this.tiles[i] = new Tile(boardToCopy.getTiles()[i].getContent());
        }
    }
    
    //Fills in a Tile's Content
    public void setTile(int tileIndex, TileContent content){
        tiles[tileIndex].setContent(content);
    }
    
    //Caulculate Scores for players
    public int getScore(int player){
        int player1Score = 0;
        int player2Score = 0;
        for(int row = 0; row < numRows; row++){
            for(int col = 0; col < numCols; col++){
                if(tiles[row + col*numRows].getContent() == PLAYER1CHECKER){
                    player1Score += 1;
                    player2Score -= 1;
                }
                if(tiles[row + col*numRows].getContent() == PLAYER1KING){
                    player1Score += 3;
                    player2Score -= 3;
                }
                if(tiles[row + col*numRows].getContent() == PLAYER2CHECKER){
                    player2Score += 1;
                    player1Score -= 1;
                }
                if(tiles[row + col*numRows].getContent() == PLAYER2KING){
                    player2Score += 3;
                    player1Score -= 3;
                }
            }
        }
        //System.out.println("Score for Player1: " + player1Score + ", Player2:" + player2Score);
        if(player == 1){return player1Score;}
        else{return player2Score;}
            
    }
    
    //Builds the Default Board
    public void populateDefault(){
        
        for(int i = 0; i < maxIndex; i++){
            
            //Player 2, Top of Board - Top Three Rows
            if(i < numRows*3){
                //If Even Row, Populate Odd Squares
                if((i/numRows)%2 == 0 && i%2 == 1){
                    tiles[i].setContent(PLAYER2CHECKER);
                }
                //If Odd Row, Populate Even Squares
                if((i/numRows)%2 == 1 && i%2 == 0){
                    tiles[i].setContent(PLAYER2CHECKER);
                }
            }
            
            //Player 1, Bottom of Board - Bottom Three Rows
            if(i >= maxIndex-numRows*3){
                //If Even Row, Populate Odd Squares
                if((i/numRows)%2 == 0 && i%2 == 1){
                    tiles[i].setContent(PLAYER1CHECKER);
                }
                //If Odd Row, Populate Even Squares
                if((i/numRows)%2 == 1 && i%2 == 0){
                    tiles[i].setContent(PLAYER1CHECKER);
                }
            }
        } 
    }
    
    //Determine if the Board has a winner
    public boolean hasWinner(){
        int winner1Flag = 1;
        int winner2Flag = 1;
        for(int i = 0; i < maxIndex; i++){
            if(this.matchesPlayer(2, i)){
                winner1Flag = 0;
            }
            if(this.matchesPlayer(1, i)){
                winner2Flag = 0;
            }     
        }
        return(winner1Flag == 1 || winner2Flag == 1);
    }
    
    //Check if Tile is a Valid Index
    public boolean isValidTileIndex(int tileIndex){
        return((tileIndex >= 0) && (tileIndex < maxIndex) && tiles[tileIndex].isValid());
    }
    
    //Check if Move is Valid
    public boolean tryMoving(int tileIndex){
        return (this.isValidTileIndex(tileIndex) && tiles[tileIndex].isOpen());
    }
    
    //Check if Jump is Valid
    public boolean tryJumping(int startingIndex, int jumpingOverIndex, int landingIndex, TileContent checkerType, JumpType jump){
        return(this.isValidTileIndex(startingIndex) && this.isValidTileIndex(jumpingOverIndex) && this.isValidTileIndex(landingIndex) 
                && (tiles[jumpingOverIndex].isEnemy(checkerType) && !jump.getEnemiesDestroyed().contains(jumpingOverIndex))
                && (tiles[landingIndex].isOpen() || jump.getPath().contains(landingIndex)));    
    }
    
    //Checks and Sets checkers that have made if to other side of the board to Kings
    private void checkKings(int index){
        if(index/numRows == 0 || index/numRows == numRows-1){
            if(tiles[index].getContent() == PLAYER1CHECKER){
                tiles[index].setContent(PLAYER1KING);
            }
            if(tiles[index].getContent() == PLAYER2CHECKER){
                tiles[index].setContent(PLAYER2KING);
            }
        }
    }
    
    //Compute Move on Board
    public void doMove(int checkerBefore, int checkerAfter){
        tiles[checkerAfter].setContent(tiles[checkerBefore].getContent());
        tiles[checkerBefore].setContent(EMPTY);
        checkKings(checkerAfter);
    }
    
    //Compute Jump on Board
    public void doJump(int checkerBefore, int checkerAfter){
        int checkerBetween;
        if(checkerBefore < checkerAfter){
            if(checkerBefore+(2*numCols) < checkerAfter){checkerBetween = checkerBefore+numCols+1;}
            else{checkerBetween = checkerBefore+numCols-1;}
        }
        else{
            if(checkerBefore-(2*numCols) < checkerAfter){checkerBetween = checkerBefore-numCols+1;}
            else{checkerBetween = checkerBefore-numCols-1;}
        }

        tiles[checkerAfter].setContent(tiles[checkerBefore].getContent());
        tiles[checkerBetween].setContent(EMPTY);
        tiles[checkerBefore].setContent(EMPTY);
        checkKings(checkerAfter);
    }
        

    //Returns of list of jumps a checker can make
    public ArrayList<JumpType> getValidJumps(int checkerIndex, TileContent checkerType, JumpType previousJump){

        ArrayList<JumpType> validJumps = new ArrayList<>();
        if(!this.isValidTileIndex(checkerIndex)){return validJumps;}
        
        int upLeftIndex = (checkerIndex - numCols - 1);
        int upRightIndex = (checkerIndex - numCols + 1);
        int downLeftIndex = (checkerIndex + numCols - 1);
        int downRightIndex = (checkerIndex + numCols + 1);

        int upLeftJumpIndex = (checkerIndex - (2*numCols) - 2);
        int upRightJumpIndex = (checkerIndex - (2*numCols) + 2);
        int downLeftJumpIndex = (checkerIndex + (2*numCols) - 2);
        int downRightJumpIndex = (checkerIndex + (2*numCols) + 2);
     
        
        //Player 1 + Kings
        if(checkerType == PLAYER1CHECKER || checkerType == PLAYER1KING || checkerType == PLAYER2KING){
            if(tryJumping(checkerIndex, upLeftIndex, upLeftJumpIndex, checkerType, previousJump)){
               JumpType jump = new JumpType(upLeftIndex,upLeftJumpIndex, previousJump);
               validJumps.add(jump);
            }
            if(tryJumping(checkerIndex, upRightIndex, upRightJumpIndex, checkerType, previousJump)){
               JumpType jump = new JumpType(upRightIndex,upRightJumpIndex, previousJump);
               validJumps.add(jump);
            }
        }
        //Player 2 + Kings
        if(checkerType == PLAYER2CHECKER || checkerType == PLAYER1KING || checkerType == PLAYER2KING){
            if(tryJumping(checkerIndex, downLeftIndex, downLeftJumpIndex, checkerType, previousJump)){
               JumpType jump = new JumpType(downLeftIndex,downLeftJumpIndex, previousJump);
               validJumps.add(jump);
            }
            if(tryJumping(checkerIndex, downRightIndex, downRightJumpIndex, checkerType, previousJump)){
               JumpType jump = new JumpType(downRightIndex,downRightJumpIndex, previousJump);
               validJumps.add(jump);
            }
        }

        
        ArrayList<JumpType> finalValidJumps = new ArrayList<>();
        //Once Jumped, we need to see if we can make any other jumps!
        for(int i = 0; i < validJumps.size(); i++){
            //System.out.println("Jump at: " + validJumps.get(i).getPath() + "");
            //If we jump and we can jump again, remove current jump and add future jumps!
            if(!getValidJumps(validJumps.get(i).getPath().get(validJumps.get(i).getPath().size()-1), checkerType, validJumps.get(i)).isEmpty()){
                finalValidJumps.addAll(getValidJumps(validJumps.get(i).getPath().get(validJumps.get(i).getPath().size()-1), checkerType, validJumps.get(i)));
            } 
            else{
                finalValidJumps.add(validJumps.get(i));
            }
        }
        
        return finalValidJumps;   
    }
    
    public ArrayList<Integer> getValidMoves(int checkerIndex){
        
        ArrayList<Integer> validMoves = new ArrayList<>();
        if(!this.isValidTileIndex(checkerIndex)){return validMoves;}
        TileContent checkerType = tiles[checkerIndex].getContent();
        
        int upLeftIndex = (checkerIndex - numCols - 1);
        int upRightIndex = (checkerIndex - numCols + 1);
        int downLeftIndex = (checkerIndex + numCols - 1);
        int downRightIndex = (checkerIndex + numCols + 1);
        
        //Check if Trying to Move From a Valid Tile
        if(checkerType == NOTVALID || checkerType == EMPTY){
            return validMoves;
        }

        if(checkerType == PLAYER1CHECKER || checkerType == PLAYER1KING || checkerType == PLAYER2KING){
            //Forward Move Up Left
            if(tryMoving(upLeftIndex)){validMoves.add(upLeftIndex);}
            //Forward Move Up Right
            if(tryMoving(upRightIndex)){validMoves.add(upRightIndex);}
        }
        
        if(checkerType == PLAYER2CHECKER || checkerType == PLAYER1KING || checkerType == PLAYER2KING){
            //Forward Move Down Left
            if(tryMoving(downLeftIndex)){validMoves.add(downLeftIndex);}
            //Forward Move Down Right
            if(tryMoving(downRightIndex)){validMoves.add(downRightIndex);}
        }
        
        return validMoves;
    }
    
    
    public void placeChecker(int checkerIndex, TileContent checkerType){
        tiles[checkerIndex].setContent(checkerType);
    }
    
    public boolean matchesPlayer(int playerID, int index){
        if(playerID == 1 && (tiles[index].getContent() == PLAYER1CHECKER || tiles[index].getContent() == PLAYER1KING)){
            return true;
        }
        else if(playerID == 2 && (tiles[index].getContent() == PLAYER2CHECKER || tiles[index].getContent() == PLAYER2KING)){
            return true;
        }
        else{
            return false;
        }
    }

    
    public void printBoard(){
        for(int i = 0; i < tiles.length; i++){
            if(i%numCols == 0){
                System.out.println("");
            }
            if(i < 10){
                System.out.print("0" + i + tiles[i].getContent().toString() + " ");
            }
            else{
                System.out.print(i + tiles[i].getContent().toString() + " ");
            } 
        }
        System.out.print("\n");
    }
    
    public void printValidActions(int checkerIndex){
        
        ArrayList<Integer> validMoves = this.getValidMoves(checkerIndex);
        Collections.reverse(validMoves);
        System.out.print("\nValid moves Are for "+checkerIndex+": " + validMoves);
      
        System.out.print("\n");
        
        ArrayList<JumpType> validJumps = this.getValidJumps(checkerIndex, tiles[checkerIndex].getContent(), new JumpType(checkerIndex));
        System.out.println("\nValid Jumps Are for "+checkerIndex+": ");
        System.out.println("SIZE: " + validJumps.size());
        for(JumpType jump : validJumps){
            Collections.reverse(jump.getPath());
            Collections.reverse(jump.getPath());
            System.out.println("Path: " + jump.getPath() + " | Enemies Killed at: " + jump.getEnemiesDestroyed());
            
        }
        System.out.print("\n");
        
    }
    
    public void removeChecker(int index){
        tiles[index].setContent(EMPTY);
    }
    public void removeCheckers(ArrayList<Integer> indexes){
        indexes.stream().forEach((i) -> {
            this.tiles[i].setContent(EMPTY);
        });
    }
}
