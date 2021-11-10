package mctsPrototype;

import java.util.List;

public class UCT {
  
  public static double getUCTValue(double nodeWins, double nodeVisits, double parentVisits) {
    if(nodeVisits == 0) {
      return 100;
    }
    return (nodeWins/nodeVisits)+1.00*Math.sqrt(Math.log(parentVisits)/nodeVisits);
  }
  
  public static Node BestUCTNode(Node node) {
    double parentVisits = node.state.getVisitCount();
    double currentHighest = 0;
    Node selectedNode = null;
    List<Node> children = node.getChildren();
    
    for(Node child: children) {
      double current = getUCTValue(child.state.getWinCount(), child.state.getVisitCount(), parentVisits);
      
      if(currentHighest < current) {
        currentHighest = current;
        selectedNode = child;
      }
    }
    if (currentHighest == 0 || selectedNode == null) {
      return node.getRandomChild();
    }
    
    return selectedNode;
    
    
  }

}
