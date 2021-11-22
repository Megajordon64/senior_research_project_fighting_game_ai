package mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;
import aiinterface.CommandCenter;
import enumerate.Action;
import simulator.Simulator;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;


/**
 * a class that will hold all the information of a current state of the game to use
 * in determining what move to perform
 * Warning: will later be updated to incorporate game data from fightingICE such as framedata,
 * player and opponent health, etc.
 * @author Jordon
 *
 */
public class GameState {
  /**as of right now the GameState class will use a basic
  // int value in place of the values that will be used in the
  // final version, it will use various values that the fightingICE 
  // program can provide and other standard values used for mcts
  // but for basic testing of mcts and NN a basic value
  // that is easier to produce and guess what it will do will be used
  // alongside the usual visit count and win count
  */
  private double visitCount;
  private double winCount;
  private FrameData frameData;
  private GameData gameData;
  
  public GameState(FrameData fd, GameData gd) {
    visitCount = 0;
    winCount = 0;
    frameData = fd;
    gameData = gd;

  }
  //public GameState() {
    
    //visitCount = 0;
    //winCount = 0;
  //}
  
  //public int getGameStateValue() {
   // return prototype;
  //}
  
  //public void setGameStateValue(int prototype) {
  //  this.prototype = prototype;
  //}
  
  public double getVisitCount() {
    return visitCount;
  }
  
  public void setVisitCount(double visitCount) {
    this.visitCount = visitCount;
  }
  
  public double getWinCount() {
    return winCount;
  }
  
  public void setWinCount(double winCount) {
    this.winCount = winCount;
  }
  
  public void setFD(FrameData fd) {
    frameData = fd;
  }
  
  public void setGD(GameData gd) {
    gameData = gd;
  }
  
  public FrameData getFD() {
    return frameData;
  }
  
  public GameData getGD() {
    return gameData;
  }
  
  // will be updated later to accept state info from fightingICE
  //public List<GameState> getPossibleStates(int numStates){
    //List<GameState> possibleStates = new ArrayList<>();
    
    //for(int i = 0; i < numStates; i++) {
      //GameState state = new GameState(i+2);
      //possibleStates.add(state);
    //}
    
    //return possibleStates;
    
  
  
  public void increaseVisitCount() {
    visitCount++;
  }
  
  public void increaseWinCount() {
    winCount++;
  }
}
