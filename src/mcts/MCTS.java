package mcts;
// code for mcts package referenced from:

// https://www.baeldung.com/java-monte-carlo-tree-search
// https://github.com/eugenp/tutorials/tree/master/algorithms-searching/src/main/java/com/baeldung/algorithms/mcts
// https://www.ice.ci.ritsumei.ac.jp/~ftgaic/index-2h.html
// https://github.com/TeamFightingICE/FightingICE/tree/master/data/ai

import java.util.List;


import java.util.ArrayList;
import java.util.LinkedList;

import aiinterface.CommandCenter;
import enumerate.Action;
import enumerate.State;
import simulator.Simulator;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import struct.MotionData;
public class MCTS {
  
  private Simulator simulator;
  private Key key;
  private CommandCenter commandCenter;
  private boolean playerNumber;
  private GameData gameData;
  private Node rootNode;


  /** Main FrameData */
  public LinkedList<TrainingExample> trainingExample;
  
  public int listTracker = 0;
  private FrameData frameData;
  
  private FrameData simulatorAheadFrameData;
  
  private LinkedList<Action> myActions;
  
  private LinkedList<Action> oppActions;
  
  private CharacterData myCharacter;
  
  private CharacterData oppCharacter;
  
  private static final int FRAME_AHEAD = 14;

  private ArrayList<MotionData> myMotion;

  private ArrayList<MotionData> oppMotion;

  private Action[] actionAir;

  private Action[] actionGround;

  private Action spSkill;
  
  Node start;
  private int timer = 100;
  Tree tree;
  int numExpansions;
  
  // constructors for use depending on whether we have or another tree we want it 
  // to start with, rather than starting from scratch
  
  public MCTS(Node start) {
    this.start = start;
    numExpansions = 10;
  }
  
  public MCTS(Tree tree) {
    this.tree = tree;
    start = tree.getRoot();
    numExpansions = 10;
  }
  
  public MCTS(GameData gameData, boolean playerNumber) {
    this.playerNumber = playerNumber;
    this.gameData = gameData;

    this.key = new Key();
    this.frameData = new FrameData();
    this.commandCenter = new CommandCenter();

    this.myActions = new LinkedList<Action>();
    this.oppActions = new LinkedList<Action>();

    simulator = gameData.getSimulator();

    actionAir = new Action[] { Action.AIR_GUARD, Action.AIR_A, Action.AIR_B, Action.AIR_DA, Action.AIR_DB,
        Action.AIR_FA, Action.AIR_FB, Action.AIR_UA, Action.AIR_UB, Action.AIR_D_DF_FA, Action.AIR_D_DF_FB,
        Action.AIR_F_D_DFA, Action.AIR_F_D_DFB, Action.AIR_D_DB_BA, Action.AIR_D_DB_BB };
    actionGround = new Action[] { Action.STAND_D_DB_BA, Action.BACK_STEP, Action.FORWARD_WALK, Action.DASH,
        Action.JUMP, Action.FOR_JUMP, Action.BACK_JUMP, Action.STAND_GUARD, Action.CROUCH_GUARD, Action.THROW_A,
        Action.THROW_B, Action.STAND_A, Action.STAND_B, Action.CROUCH_A, Action.CROUCH_B, Action.STAND_FA,
        Action.STAND_FB, Action.CROUCH_FA, Action.CROUCH_FB, Action.STAND_D_DF_FA, Action.STAND_D_DF_FB,
        Action.STAND_F_D_DFA, Action.STAND_F_D_DFB, Action.STAND_D_DB_BB };
    spSkill = Action.STAND_D_DF_FC;

    myMotion = gameData.getMotionData(this.playerNumber);
    oppMotion = gameData.getMotionData(!this.playerNumber);
    trainingExample = new LinkedList<TrainingExample>();
    numExpansions = 1;

  }
  
  public int getNumExpansions() {
    return numExpansions;
  }
  
  public void setNumExpansions(int exp) {
    numExpansions = exp;
  }
  
  // this method will later be updated to return the game state returned by various functions found within
  // fightingICE, for right now it will just return the prototype value for testing purposes
  public Action selectBestMove(FrameData fd, Node start) {
    trainingExample.clear();
    mctsPrepare();
    start.setMyActions(myActions);
    start.setOppActions(oppActions);
    long timeLimit = System.currentTimeMillis() + timer;
      tree = new Tree(start);
      rootNode = tree.Root;
      rootNode.setGameState(new GameState(fd, gameData));
    
    int j = 0;
    while (System.currentTimeMillis() < timeLimit && j < numExpansions) {
      
      Node selectedNode = selectNode(rootNode);
      
      expandNode(selectedNode);
      
      double result = simulateRandomState(selectedNode);
      
      backProp(selectedNode, result);
      j++;
      
    }
    
    Node Selection = tree.getRoot();
    trainingExample.add(new TrainingExample(frameData));
    trainingExample.getLast().setAction(Selection.getBestScoreAction());
    return Selection.getBestScoreAction();
  }
  
  public Node selectNodeVer2(Node start) {
    Node node = start;
    
    while (node.getChildren().size() != 0) {
      trainingExample.add(new TrainingExample(frameData));
      node = UCT.BestUCTNode(node);
    }
    return node;
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
    List<Action> states = node.getPossibleActions();
    
    for(int i = listTracker; i < states.size() + listTracker; i++) {

        LinkedList<Action> my = new LinkedList<Action>();
        for (Action act : node.getMySelectedActions()) {
          my.add(act);
        }

      my.add(myActions.get(i));
      Node leafNode = new Node(node.frameData, node, node.myActions, node.oppActions, 
          node.gameData, node.playerNumber, node.commandCenter, my);
      node.children.add(leafNode);
    }
  }
  
  // this method represents the simulation phase
  // will likely be reworked when the fightingICE functionality is added in
  public double simulateRandomState(Node node) {
    Node bestChildNode = node.findBestChildNode();
    return bestChildNode.playout();
  }
  
  // may be considered obsolete due to mcts being used only having depth of 1 and resetting each time
  // this method will represent the backpropagation phase
  public void backProp(Node start, double result) {
    Node bottom = start;
    while (bottom != null) {
      bottom.getGameState().increaseVisitCount();
      if (result > 0)
          bottom.getGameState().increaseWinCount();
      bottom = bottom.getParentNode();
  }
  
  
}


  public void retrieveInformation(FrameData frameData) {
    this.frameData = frameData;
    this.commandCenter.setFrameData(this.frameData, playerNumber);

    myCharacter = frameData.getCharacter(playerNumber);
    oppCharacter = frameData.getCharacter(!playerNumber);
    
  }
    


  

  
  public boolean canProcessing() {
    return !frameData.getEmptyFlag() && frameData.getRemainingFramesNumber() > 0;
  }
  
  public void mctsPrepare() {
    simulatorAheadFrameData = simulator.simulate(frameData, playerNumber, null, null, FRAME_AHEAD);

    myCharacter = simulatorAheadFrameData.getCharacter(playerNumber);
    oppCharacter = simulatorAheadFrameData.getCharacter(!playerNumber);

    setMyAction();
    setOppAction();
  }
  
  public void setMyAction() {
    myActions.clear();

    int energy = myCharacter.getEnergy();
    if (myCharacter.getState() == State.AIR) {
      for (int i = 0; i < actionAir.length; i++) {
        if (Math.abs(myMotion.get(Action.valueOf(actionAir[i].name()).ordinal())
            .getAttackStartAddEnergy()) <= energy) {
          myActions.add(actionAir[i]);
        }
      }
    } else {
      if (Math.abs(
          myMotion.get(Action.valueOf(spSkill.name()).ordinal()).getAttackStartAddEnergy()) <= energy) {
        myActions.add(spSkill);
      }

      for (int i = 0; i < actionGround.length; i++) {
        if (Math.abs(myMotion.get(Action.valueOf(actionGround[i].name()).ordinal())
            .getAttackStartAddEnergy()) <= energy) {
          myActions.add(actionGround[i]);
        }
      }
    }

  }
  
  public void setOppAction() {
    oppActions.clear();

    int energy = oppCharacter.getEnergy();

    if (oppCharacter.getState() == State.AIR) {
      for (int i = 0; i < actionAir.length; i++) {
        if (Math.abs(oppMotion.get(Action.valueOf(actionAir[i].name()).ordinal())
            .getAttackStartAddEnergy()) <= energy) {
          oppActions.add(actionAir[i]);
        }
      }
    } else {
      if (Math.abs(oppMotion.get(Action.valueOf(spSkill.name()).ordinal())
          .getAttackStartAddEnergy()) <= energy) {
        oppActions.add(spSkill);
      }

      for (int i = 0; i < actionGround.length; i++) {
        if (Math.abs(oppMotion.get(Action.valueOf(actionGround[i].name()).ordinal())
            .getAttackStartAddEnergy()) <= energy) {
          oppActions.add(actionGround[i]);
        }
      }
    }
  }
  
  public LinkedList<TrainingExample> getTrainingExamples(){
    return trainingExample;
  }
  
}
