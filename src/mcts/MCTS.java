package mcts;
// code for mcts package referenced from:
// https://www.baeldung.com/java-monte-carlo-tree-search
// https://github.com/eugenp/tutorials/tree/master/algorithms-searching/src/main/java/com/baeldung/algorithms/mcts
// https://www.ice.ci.ritsumei.ac.jp/~ftgaic/index-2h.html
// https://github.com/TeamFightingICE/FightingICE/tree/master/data/ai

import java.util.List;

public class MCTS {
  
  Node start; 
  private int timer = 500;
  Tree tree;
  int numExpansions;
  
  // constructors for use depending on whether we have or another tree we want it 
  // to start with, rather than starting from scratch
  public MCTS() {
    start = new Node();
    numExpansions = 10;
  };
  
  public MCTS(Node start) {
    this.start = start;
    numExpansions = 10;
  }
  
  public MCTS(Tree tree) {
    this.tree = tree;
    start = tree.getRoot();
    numExpansions = 10;
  }
  
  public int getNumExpansions() {
    return numExpansions;
  }
  
  public void setNumExpansions(int exp) {
    numExpansions = exp;
  }
  
  // this method will later be updated to return the game state returned by various functions found within
  // fightingICE, for right now it will just return the prototype value for testing purposes
  public int selectBestMove(GameState state) {
    
    long timeLimit = System.currentTimeMillis() + timer;
    if (tree == null) {
      tree = new Tree(start);
    }
    if (tree.getRoot().getGameState() != state) {
      tree.getRoot().setGameState(state);
    }
    
    while (System.currentTimeMillis() < timeLimit) {
      
      Node selectedNode = selectNode(tree.getRoot());
      
      // using 0 and 10 as temporary signifiers of end state of the game
      if (selectedNode.getGameState().getGameStateValue() != 0 &&
          selectedNode.getGameState().getGameStateValue() != 10) {
        expandNode(selectedNode);
      }
      
      Node simNode = selectedNode;
      
      if(selectedNode.getChildren().size() > 0) {
        simNode = selectedNode.getRandomChild();
      }
      
      int result = simulateRandomState(simNode);
      
      backProp(simNode, result);
      
    }
    
    Node finalSelection = tree.getRoot().findBestChildNode();
    return finalSelection.getGameState().getGameStateValue();
  }
  
  // this method refers to the selection phase of mcts
  public Node selectNode(Node start) {
    Node node = start;
    while (node.getChildren().size() != 0) {
      node = UCT.BestUCTNode(node);
    }
    return node;
  }
  
  // this method refers to the expansion phase of mcts
  public void expandNode(Node node) {
    List<GameState> states = node.getGameState().getPossibleStates(getNumExpansions());
    
    for(int i = 0; i < states.size(); i++) {
      Node leafNode = new Node(states.get(i));
      leafNode.setParentNode(node);
    }
  }
  
  // this method represents the simulation phase
  // will likely be reworked when the fightingICE functionality is added in
  public int simulateRandomState(Node node) {
    Node bestChildNode = node.findBestChildNode();
    return bestChildNode.getGameState().getGameStateValue();
  }
  
  // this method will represent the backpropagation phase
  public void backProp(Node start, int result) {
    Node bottom = start;
    while (bottom != null) {
      bottom.getGameState().increaseVisitCount();
      if (bottom.getGameState().getGameStateValue() == 10 || result == 10)
          bottom.getGameState().increaseWinCount();
      bottom = bottom.getParentNode();
  }
  
  
}
}
