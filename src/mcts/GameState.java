package mcts;

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
  private int prototype;
  private double visitCount;
  private double winCount;
  
  public GameState() {
    prototype = 1;
    visitCount = 0;
    winCount = 0;
  }
  public GameState(int prototype) {
    this.prototype = prototype;
    if(prototype == 0) {
      prototype = 1;
    }
    visitCount = 0;
    winCount = 0;
  }
  
  public int getGameState() {
    return prototype;
  }
  
  public void setGameState(int prototype) {
    this.prototype = prototype;
  }
  
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
}
