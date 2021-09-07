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
  // that is easier to produce and guess what it will do
  */
  private int prototype;
  
  public GameState() {
    prototype = 1;
  }
  public GameState(int prototype) {
    this.prototype = prototype;
    if(prototype == 0) {
      prototype = 1;
    }
  }
  
  public int getGameState() {
    return prototype;
  }
  
  public void setGameState(int prototype) {
    this.prototype = prototype;
  }
}
