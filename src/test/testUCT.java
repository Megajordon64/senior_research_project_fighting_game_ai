package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import mcts.Node;
import mcts.Tree;

public class testUCT {
  
  @Before
  public void preparation() {
    Tree testTree = new Tree();
    for (int i = 0; i < 3; i++) {
      Node node = new Node();
      testTree.getRoot().getChildren().add(node);
    }
  }
  

  @Test
  public void test() {
    fail("Not yet implemented");
  }

}
