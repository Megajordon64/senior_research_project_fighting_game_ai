package mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;
import aiinterface.CommandCenter;
import enumerate.Action;
import simulator.Simulator;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;

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
  private List<Action> myActions;

  private List<Action> oppActions;

  private Simulator simulator;

  private List<Action> selectedMyActions;

  private int myOriginalHp;

  private int oppOriginalHp;
  private int games;
  
  private Random rnd;
  private int depth;
  private FrameData frameData;
  private boolean playerNumber;
  private CommandCenter commandCenter;
  private GameData gameData;
  public static final int SIMULATION_TIME = 60;


  private boolean isCreateNode;

  Deque<Action> mAction;
  Deque<Action> oppAction;

  
  public Node(FrameData frameData, Node parent, List<Action> myActions,
      List<Action> oppActions, GameData gameData, boolean playerNumber,
      CommandCenter commandCenter, List<Action> selectedMyActions) {
    this(frameData, parent, myActions, oppActions, gameData, playerNumber, commandCenter);

    this.selectedMyActions = selectedMyActions;
  }
  
  public Node(FrameData frameData, Node parent, List<Action> myActions,
      List<Action> oppActions, GameData gameData, boolean playerNumber,
      CommandCenter commandCenter) {
    this.frameData = frameData;
    this.parent = parent;
    this.myActions = myActions;
    this.oppActions = oppActions;
    this.gameData = gameData;
    this.simulator = new Simulator(gameData);
    this.playerNumber = playerNumber;
    this.commandCenter = commandCenter;

    this.selectedMyActions = new ArrayList<Action>();

    this.rnd = new Random();
    this.mAction = new LinkedList<Action>();
    this.oppAction = new LinkedList<Action>();

    CharacterData myCharacter = frameData.getCharacter(playerNumber);
    CharacterData oppCharacter = frameData.getCharacter(!playerNumber);
    myOriginalHp = myCharacter.getHp();
    oppOriginalHp = oppCharacter.getHp();

    if (this.parent != null) {
      this.depth = this.parent.depth + 1;
    } else {
      this.depth = 0;
    }
  }
  
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
       i++;
      }
    
    
    return children.get(nodePosition);
  }
  
  public double playout() {

    mAction.clear();
    oppAction.clear();

    for (int i = 0; i < selectedMyActions.size(); i++) {
      mAction.add(selectedMyActions.get(i));
    }

    for (int i = 0; i < 5 - selectedMyActions.size(); i++) {
      mAction.add(myActions.get(rnd.nextInt(myActions.size())));
    }

    for (int i = 0; i < 5; i++) {
      oppAction.add(oppActions.get(rnd.nextInt(oppActions.size())));
    }

    FrameData nFrameData =
        simulator.simulate(frameData, playerNumber, mAction, oppAction, SIMULATION_TIME); 

    return getScore(nFrameData);
  }
  
  public int getScore(FrameData fd) {
    return (fd.getCharacter(playerNumber).getHp() - myOriginalHp) - (fd.getCharacter(!playerNumber).getHp() - oppOriginalHp);
  }
  
  
  
}
