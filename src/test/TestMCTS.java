package test;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import mcts.GameState;
import mcts.MCTS;
import mcts.MCTS_Test;
import mcts.MCTS_Test2;
import mcts.Node;
import mcts.Tree;

public class TestMCTS {
  
  Tree testTree;
  Tree testTree2;
  MCTS_Test mctsTest;
  MCTS_Test2 mctsTest2;
  @Before
  public void preparation() {
    testTree = new Tree(new Node(new GameState()));
    mctsTest = new MCTS_Test(testTree);
    testTree2 = new Tree(new Node(new GameState()));
    mctsTest2 = new MCTS_Test2(testTree2);
  }

  @Test
  public void test() {
    Node testNode = mctsTest.selectNode(mctsTest.tree.getRoot());
    assertEquals(testNode.getGameState().getGameStateValue(), 1);
    mctsTest.expandNode(mctsTest.tree.getRoot());
    int testInt = mctsTest.simulateRandomState(mctsTest.tree.getRoot());
    assertEquals(testInt, 10);
    mctsTest.backProp(mctsTest.tree.getRoot().findBestChildNode(), 10);
    assertEquals((int)mctsTest.tree.getRoot().findBestChildNode().getGameState().getVisitCount(), 1);
    assertEquals((int)mctsTest.tree.getRoot().findBestChildNode().getGameState().getWinCount(), 1);
    assertEquals((int)mctsTest.tree.getRoot().getGameState().getVisitCount(), 1);
  }

}
