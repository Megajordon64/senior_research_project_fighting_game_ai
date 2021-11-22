package finalAI;
import java.util.ArrayList;
import java.util.LinkedList;

import aiinterface.AIInterface;
import aiinterface.CommandCenter;
import enumerate.Action;
import mcts.Node;
import mcts.MCTS;
import neural_network.NN;
import neural_network.Matrix;
import simulator.Simulator;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import struct.MotionData;
public class FinalTrainingAI implements AIInterface{

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

  public int[] neuralNetInput() {
    int[] neural_net_input = new int[100];
    neural_net_input[0] = frameData.getDistanceX();
    neural_net_input[1] = frameData.getDistanceY();
    neural_net_input[2] = frameData.getRemainingTimeMilliseconds();
    neural_net_input[3] = frameData.getCharacter(false).getCenterX();
    neural_net_input[4] = frameData.getCharacter(false).getCenterY();
    neural_net_input[5] = frameData.getCharacter(false).getAttack().getActive();
    neural_net_input[6] = frameData.getCharacter(false).getAttack().getAttackType();
    neural_net_input[7] = frameData.getCharacter(false).getAttack().getCurrentFrame();
    neural_net_input[8] = frameData.getCharacter(false).getAttack().getCurrentHitArea().getBottom();
    neural_net_input[9] = frameData.getCharacter(false).getAttack().getCurrentHitArea().getLeft();
    neural_net_input[10] = frameData.getCharacter(false).getAttack().getCurrentHitArea().getTop();
    neural_net_input[11] = frameData.getCharacter(false).getAttack().getCurrentHitArea().getRight();
    neural_net_input[12] = frameData.getCharacter(false).getAttack().getGiveEnergy();
    neural_net_input[13] = frameData.getCharacter(false).getAttack().getGiveGuardRecov();
    // resume from attackData whatever is after getGiveGuardRecov
    return neural_net_input;
  }
  @Override
  public void close() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getInformation(FrameData arg0, boolean arg1) {
    this.frameData = frameData;
    this.commandCenter.setFrameData(this.frameData, playerNumber);

    myCharacter = frameData.getCharacter(playerNumber);
    oppCharacter = frameData.getCharacter(!playerNumber);
    
  }

  @Override
  public int initialize(GameData arg0, boolean arg1) {
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void processing() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void roundEnd(int arg0, int arg1, int arg2) {
    // TODO Auto-generated method stub
    
  }

}
