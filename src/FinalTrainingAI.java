
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import aiinterface.AIInterface;
import aiinterface.CommandCenter;
import enumerate.Action;
import enumerate.State;
import mcts.Node;
import mcts.TrainingExample;
import mcts.MCTS;
import neural_network.NN;
import neural_network.Matrix;
import simulator.Simulator;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import struct.MotionData;
@SuppressWarnings("unused")
public class FinalTrainingAI implements AIInterface{

  private Simulator simulator;
  private Key inputKey;
  private CommandCenter commandCenter;
  public static boolean playerNumber;
  public static GameData gameData;
  public static MCTS mcts;
  
  /** Main FrameData */
  public LinkedList<TrainingExample> trainingExample;
  public LinkedList<TrainingExample> oppTrainingExample;
  private FrameData frameData;
  
  private LinkedList<Action> myActions;
  
  private LinkedList<Action> oppActions;
  
  private CharacterData myCharacter;
  
  private CharacterData oppCharacter;
  
  private static final State STAND = null;
  private static final State CROUCH = null;
  private static final State DOWN = null;
  private static final State AIR = null;

  private ArrayList<MotionData> myMotion;

  private ArrayList<MotionData> oppMotion;

  private Action[] AllActions;

  public double[] neuralNetInput(FrameData fd) {
    double[] neural_net_input = new double[44];
    neural_net_input[0] = fd.getDistanceX();
    neural_net_input[1] = fd.getDistanceY();
    neural_net_input[2] = fd.getRemainingTimeMilliseconds();
    neural_net_input[3] = fd.getCharacter(false).getCenterX();
    neural_net_input[4] = fd.getCharacter(false).getCenterY();
    neural_net_input[5] = fd.getCharacter(false).getAttack().getActive();
    neural_net_input[6] = fd.getCharacter(false).getAttack().getAttackType();
    neural_net_input[7] = fd.getCharacter(false).getAttack().getCurrentFrame();
    neural_net_input[8] = fd.getCharacter(false).getAttack().getCurrentHitArea().getBottom();
    neural_net_input[9] = fd.getCharacter(false).getAttack().getCurrentHitArea().getLeft();
    neural_net_input[10] = fd.getCharacter(false).getAttack().getCurrentHitArea().getTop();
    neural_net_input[11] = fd.getCharacter(false).getAttack().getCurrentHitArea().getRight();
    neural_net_input[12] = fd.getCharacter(false).getAttack().getGiveEnergy();
    neural_net_input[13] = fd.getCharacter(false).getAttack().getGiveGuardRecov();
    neural_net_input[14] = fd.getCharacter(false).getAttack().getGuardAddEnergy();
    neural_net_input[15] = fd.getCharacter(false).getAttack().getGuardDamage();
    neural_net_input[16] = fd.getCharacter(false).getAttack().getHitAddEnergy();
    neural_net_input[17] = fd.getCharacter(false).getAttack().getHitDamage();
    neural_net_input[18] = fd.getCharacter(false).getAttack().getImpactX();
    neural_net_input[19] = fd.getCharacter(false).getAttack().getImpactY();
    neural_net_input[20] = fd.getCharacter(false).getAttack().getPlayerNumber();
    neural_net_input[21] = fd.getCharacter(false).getAttack().getSettingSpeedX();
    neural_net_input[22] = fd.getCharacter(false).getAttack().getSettingSpeedY();
    neural_net_input[23] = fd.getCharacter(false).getAttack().getSpeedX();
    neural_net_input[24] = fd.getCharacter(false).getAttack().getSpeedY();
    neural_net_input[25] = fd.getCharacter(false).getAttack().getStartAddEnergy();
    neural_net_input[26] = fd.getCharacter(false).getAttack().getStartUp();
    if(fd.getCharacter(false).getAttack().isDownProp()) {
      neural_net_input[27] = 1;
    } else {
      neural_net_input[27] = 0;
    }
    if(fd.getCharacter(false).getAttack().isProjectile()) {
      neural_net_input[28] = 1;
    } else {
      neural_net_input[28] = 0;
    }
    neural_net_input[29] = fd.getCharacter(false).getAttack().getSettingHitArea().getBottom();
    neural_net_input[30] = fd.getCharacter(false).getAttack().getSettingHitArea().getTop();
    neural_net_input[31] = fd.getCharacter(false).getAttack().getSettingHitArea().getRight();
    neural_net_input[32] = fd.getCharacter(false).getAttack().getSettingHitArea().getLeft();
    neural_net_input[33] = fd.getCharacter(false).getBottom();
    neural_net_input[34] = fd.getCharacter(false).getTop();
    neural_net_input[35] = fd.getCharacter(false).getRight();
    neural_net_input[36] = fd.getCharacter(false).getLeft();
    neural_net_input[37] = fd.getCharacter(false).getEnergy();
    neural_net_input[38] = fd.getCharacter(false).getHp();
    neural_net_input[39] = fd.getCharacter(false).getLastHitFrame();
    neural_net_input[40] = fd.getCharacter(false).getRemainingFrame();
    neural_net_input[41] = fd.getCharacter(false).getSpeedX();
    neural_net_input[42] = fd.getCharacter(false).getSpeedY();
    if(fd.getCharacter(false).getState() == STAND) {
      neural_net_input[43] = 1;
    }
    if(fd.getCharacter(false).getState() == CROUCH) {
      neural_net_input[43] = 2;
    }
    if(fd.getCharacter(false).getState() == AIR) {
      neural_net_input[43] = 3;
    }
    if(fd.getCharacter(false).getState() == DOWN) {
      neural_net_input[43] = 4;
    }
    
    return neural_net_input;
  }
  @Override
  public void close() {
   NN neural_net = new NN(44, 68, 40);
   for(int i = 0; i < trainingExample.size(); i++) {
     double[] output = new double[40];
     for(int j = 0; j < 40; j++) {
       if(trainingExample.get(i).getAction() == AllActions[j]) {
         output[j] = 1.0;
       }
       else {
         output[j] = 0.0;
       }
     }
     neural_net.train(neuralNetInput(trainingExample.get(i).getState()), output);
   }
   
   for(int i = 0; i < oppTrainingExample.size(); i++) {
     double[] output = new double[40];
     for(int j = 0; j < 40; j++) {
       if(oppTrainingExample.get(i).getAction() == AllActions[j]) {
         output[j] = 1.0;
       }
       else {
         output[j] = 0.0;
       }
     }
     neural_net.train(neuralNetInput(oppTrainingExample.get(i).getState()), output); 
   }
   
   File neural_net_setup = new File("NN_setup");
   
   try {
    neural_net_setup.createNewFile();
    
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
   
   try {
    FileWriter NN_writer = new FileWriter("NN_setup");
    double[] weights1 = neural_net.getWIH().toArray();
    for(int i = 0; i < weights1.length; i++) {
      NN_writer.write((int)weights1[i]);
      NN_writer.write(System.getProperty( "line.separator" ));
    }
    
    NN_writer.write(System.getProperty( "line.separator" ));
    
    double[] weights2 = neural_net.getWHO().toArray();
    for(int i = 0; i < weights2.length; i++) {
      NN_writer.write((int)weights2[i]);
      NN_writer.write(System.getProperty( "line.separator" ));
    }
    
    NN_writer.write(System.getProperty( "line.separator" ));
    
    double[] weights3 = neural_net.getBH().toArray();
    for(int i = 0; i < weights3.length; i++) {
      NN_writer.write((int)weights3[i]);
      NN_writer.write(System.getProperty( "line.separator" ));
    }
    
    NN_writer.write(System.getProperty( "line.separator" ));
    
    double[] weights4 = neural_net.getBO().toArray();
    for(int i = 0; i < weights4.length; i++) {
      NN_writer.write((int)weights4[i]);
      NN_writer.write(System.getProperty( "line.separator" ));
    }
    
    NN_writer.close();
    
    
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
   
    
  }

  @Override
  public void getInformation(FrameData frameData, boolean playerNumber) {
    this.frameData = frameData;
    this.commandCenter.setFrameData(this.frameData, playerNumber);
    mcts.retrieveInformation(frameData);

    myCharacter = frameData.getCharacter(playerNumber);
    oppCharacter = frameData.getCharacter(!playerNumber);
    
  }

  @Override
  public int initialize(GameData gameData, boolean playerNumber) {
    FinalTrainingAI.playerNumber = playerNumber;
    FinalTrainingAI.gameData = gameData;

    this.inputKey = new Key();
    this.frameData = new FrameData();
    this.commandCenter = new CommandCenter();

    this.myActions = new LinkedList<Action>();
    this.oppActions = new LinkedList<Action>();

    simulator = gameData.getSimulator();

    //40
    AllActions = new Action[] { Action.AIR_GUARD, Action.AIR_A, Action.AIR_B, Action.AIR_DA, Action.AIR_DB,
        Action.AIR_FA, Action.AIR_FB, Action.AIR_UA, Action.AIR_UB, Action.AIR_D_DF_FA, Action.AIR_D_DF_FB,
        Action.AIR_F_D_DFA, Action.AIR_F_D_DFB, Action.AIR_D_DB_BA, Action.AIR_D_DB_BB, Action.STAND_D_DB_BA, 
        Action.BACK_STEP, Action.FORWARD_WALK, Action.DASH, Action.JUMP, Action.FOR_JUMP, Action.BACK_JUMP, 
        Action.STAND_GUARD, Action.CROUCH_GUARD, Action.THROW_A, Action.THROW_B, Action.STAND_A, Action.STAND_B, 
        Action.CROUCH_A, Action.CROUCH_B, Action.STAND_FA, Action.STAND_FB, Action.CROUCH_FA, Action.CROUCH_FB, 
        Action.STAND_D_DF_FA, Action.STAND_D_DF_FB, Action.STAND_F_D_DFA, Action.STAND_F_D_DFB, Action.STAND_D_DB_BB, 
        Action.STAND_D_DF_FC};

    myMotion = gameData.getMotionData(playerNumber);
    oppMotion = gameData.getMotionData(!playerNumber);
    
    mcts = new MCTS(gameData, playerNumber);
    

    return 0;
  }

  @Override
  public Key input() {
    return inputKey;
  }

  @Override
  public void processing() {
    if(canProcessing()) {
    if(commandCenter.getSkillFlag()) {
     inputKey = commandCenter.getSkillKey();
     System.out.println("processing");
       } else {
       inputKey.empty();
       commandCenter.skillCancel();
    oppTrainingExample.add(new TrainingExample(frameData));
    Action currentAction = mcts.selectBestMove(frameData);
    System.out.println(currentAction.name());
    commandCenter.commandCall(currentAction.name());
    oppTrainingExample.getLast().setAction(oppCharacter.getAction());
    System.out.println("processing");
       }
    
  }
  }

  @Override
  public void roundEnd(int arg0, int arg1, int arg2) {
    LinkedList<TrainingExample> te = mcts.getTrainingExamples();
    if(arg0 > arg1) {
      for(int i = 0; i < te.size(); i++) {
        te.get(i).setResult(1);
        oppTrainingExample.get(i).setResult(-1);
      }
    }
    else {
      for(int i = 0; i < te.size(); i++) {
        te.get(i).setResult(-1);
        oppTrainingExample.get(i).setResult(1);
    }
    }
    for(int i = 0; i < te.size(); i++) {
      trainingExample.add(te.get(i));
    }
  }
  
  private boolean canProcessing() {
    return !frameData.getEmptyFlag() && frameData.getRemainingFramesNumber() > 0;
  }

}
