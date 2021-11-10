package mcts;
// code for mcts package referenced from:


// https://www.baeldung.com/java-monte-carlo-tree-search
// https://github.com/eugenp/tutorials/tree/master/algorithms-searching/src/main/java/com/baeldung/algorithms/mcts
// https://www.ice.ci.ritsumei.ac.jp/~ftgaic/index-2h.html
// https://github.com/TeamFightingICE/FightingICE/tree/master/data/ai

import java.util.List;

import java.util.ArrayList;
import java.util.LinkedList;

import aiinterface.AIInterface;
import aiinterface.CommandCenter;
import enumerate.Action;
import enumerate.State;
import simulator.Simulator;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import struct.MotionData;
class MCTS implements AIInterface {
  
  private Simulator simulator;
  private Key key;
  private CommandCenter commandCenter;
  private boolean playerNumber;
  private GameData gameData;

  /** Main FrameData */
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
      node.children.add(leafNode);
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

  @Override
  public void close() {
    // TODO Auto-generated method stub
    
  }

  public void getInformation(FrameData frameData) {
    this.frameData = frameData;
    this.commandCenter.setFrameData(this.frameData, playerNumber);

    myCharacter = frameData.getCharacter(playerNumber);
    oppCharacter = frameData.getCharacter(!playerNumber);
    
  }
    @Override
    public int initialize(GameData gameData, boolean playerNumber) {
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

      return 0;
    }

  @Override
  public Key input() {
    return key;
  }

  @Override
  public void processing() {
    if (canProcessing()) {
      if (commandCenter.getSkillFlag()) {
        key = commandCenter.getSkillKey();
      } else {
        key.empty();
        commandCenter.skillCancel();

        mctsPrepare(); 
        rootNode = new Node(simulatorAheadFrameData, null, myActions, oppActions, gameData, playerNumber,
            commandCenter);
        rootNode.createNode();

        Action bestAction = rootNode.mcts(); 
        if (MctsAi.DEBUG_MODE) {
          rootNode.printNode(rootNode);
        }

        commandCenter.commandCall(bestAction.name()); 
      }
    }
    
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

  @Override
  public void roundEnd(int arg0, int arg1, int arg2) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getInformation(FrameData arg0, boolean arg1) {
    // TODO Auto-generated method stub
    
  }
}
