package mcts;

/**
 * class that acts as starting point of tree used in mcts and holds the base of the tree
 * it also holds the most recently used or visited node and features a method to add a new child node 
 * to the tree
 * @author Jordon
 *
 */
public class Tree {
  Node Root;
  Node Current;
 
  public Tree(Node Root) {
    this.Root = Root;
  }
  
  public Node getRoot() {
    return Root;
  }
  
  public void setRoot(Node Root) {
    this.Root = Root;
  }
  
  public Node getCurrent() {
    return Current;
  }
  
  public void setCurrent(Node Current) {
    this.Current = Current;
  }
  
  public void addChild(Node Parent, Node Child) {
    Parent.getChildren().add(Child);
  }
}
