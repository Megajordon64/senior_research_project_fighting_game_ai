package mcts;

import java.util.ArrayList;
import java.util.List;

public class Node {
  State state;
  Node parent;
  List<Node> children;
  
  public Node() {
    this.state = new State();
    children = new ArrayList<Node>();
  }
  
  public Node(State state) {
    this.state = state;
    children = new ArrayList<Node>();
  }
  
  public Node(State state, Node parent, List<Node> children) {
    this.state = state;
    this.parent = parent;
    this.children = children;
  }
  
}
