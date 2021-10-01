package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import mcts.GameState;
import mcts.Node;
import mcts.Tree;
import mcts.UCT;

public class testUCT {
  
  Tree testTree;
  
  @Before
  public void preparation() {
    testTree = new Tree(new Node(new GameState(0)));
    testTree.getRoot().getGameState().setVisitCount(1);
    for (int i = 0; i < 3; i++) {
      Node node = new Node(new GameState(i+1));
      node.getGameState().setVisitCount((double)i+1);
      if(i == 2) {
        node.getGameState().setWinCount(1);
      }
      testTree.getRoot().children.add(node);
      
      
    }
  }
  
  @Test
  public void test_Children_List() {
    List<Node> childrenTest = testTree.getRoot().getChildren();
    Node node1 = childrenTest.get(0);
    System.out.print(UCT.getUCTValue(node1.getGameState().getWinCount(), node1.getGameState().getVisitCount(), 
        testTree.getRoot().getGameState().getVisitCount()));
    assertEquals(1, node1.getGameState().getGameStateValue());
    Node node2 = childrenTest.get(1);
    System.out.print(UCT.getUCTValue(node2.getGameState().getWinCount(), node2.getGameState().getVisitCount(), 
        testTree.getRoot().getGameState().getVisitCount()));
    assertEquals(2, node2.getGameState().getGameStateValue());
    Node node3 = childrenTest.get(2);
    System.out.print(UCT.getUCTValue(node3.getGameState().getWinCount(), node3.getGameState().getVisitCount(), 
        testTree.getRoot().getGameState().getVisitCount()));
    assertEquals(3, node3.getGameState().getGameStateValue());
  }

  @Test
  public void test_UCT_Retrieval() {
    assertEquals(0, testTree.getRoot().getGameState().getGameStateValue());
  }
  
  @Test
  public void test_UCT_Retrieval2() {
    Node node1 = UCT.BestUCTNode(testTree.getRoot());
    assertEquals(3, node1.getGameState().getGameStateValue());
  }

}
