package mctsPrototype;

import java.util.ArrayList;

import java.util.List;

/**
 * class that will represent a node found in MCTS, this node should contain a GameState
 * class which should information concerning the game state and have children nodes deriving from it 
 * that will represent the actions that a player may be able to take in that state
 * @author Jordon
 *
 */
public class Node {
  GameState state;
  Node parent;
  public List<Node> children;
  
  public Node() {
    this.state = new GameState();
    children = new ArrayList<Node>();
    parent = null;
  }
  
  public Node(GameState state) {
    this.state = state;
    children = new ArrayList<Node>();
    parent = null;
  }
  
  public Node(GameState state, Node parent, List<Node> children) {
    this.state = state;
    this.parent = parent;
    this.children = children;
  }
  
  public GameState getGameState() {
    return state;
  }
  
  public void setGameState(GameState state) {
    this.state = state;
  }
  
  public Node getParentNode() {
    return parent;
  }
  
  public void setParentNode(Node parent) {
    this.parent = parent;
  }
  
  public List<Node> getChildren(){
    return children;
  }
  
  public void setChildren(List<Node> children) {
    this.children = children;
  }
  
  public Node getRandomChild() {
    return children.get((int) Math.random() * children.size());
  }
  
  // rework new version to use visitCount
  public Node findBestChildNode() {
    int nodePosition = 0;
    int currentHighest = 0;
    for(int i = 1; i < children.size(); i++) {
      if(children.get(i).state.getGameStateValue() > currentHighest && children.get(i).state.getGameStateValue() < 11) {
        currentHighest = children.get(i).state.getGameStateValue();
        nodePosition = i;
      }
    }
    
    return children.get(nodePosition);
  }
  
  
  
}