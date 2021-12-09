
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import aiinterface.AIInterface;
import aiinterface.CommandCenter;
import enumerate.Action;
import enumerate.State;
import neural_network.NN;
import neural_network.Matrix;
import simulator.Simulator;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import struct.MotionData;
public class FinalAI_med implements AIInterface{


  private Simulator simulator;
  private Key key;
  private CommandCenter commandCenter;
  public static boolean playerNumber;
  public static GameData gameData;
  public static NN neural_net;
  
  /** Main FrameData */
  private FrameData frameData;
  
  private FrameData simulatorAheadFrameData;
  
  private LinkedList<Action> myActions;
  
  private LinkedList<Action> oppActions;
  
  private CharacterData myCharacter;
  
  private CharacterData oppCharacter;
  
  private static final int FRAME_AHEAD = 14;
  private static final State STAND = null;
  private static final State CROUCH = null;
  private static final State DOWN = null;
  private static final State AIR = null;

  private ArrayList<MotionData> myMotion;

  private ArrayList<MotionData> oppMotion;

  private Action[] actionAir;

  private Action[] actionGround;

  private Action spSkill;
  
  private Action[] AllActions;
  
  private int drawNegate = 0;

  private int winCount = 0;
  private int drawCount = 0;
  private int lossCount = 0;
  public LinkedList<Double> neuralNetInput() {
    double[] neural_net_input = new double[44];
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
    neural_net_input[14] = frameData.getCharacter(false).getAttack().getGuardAddEnergy();
    neural_net_input[15] = frameData.getCharacter(false).getAttack().getGuardDamage();
    neural_net_input[16] = frameData.getCharacter(false).getAttack().getHitAddEnergy();
    neural_net_input[17] = frameData.getCharacter(false).getAttack().getHitDamage();
    neural_net_input[18] = frameData.getCharacter(false).getAttack().getImpactX();
    neural_net_input[19] = frameData.getCharacter(false).getAttack().getImpactY();
    neural_net_input[20] = frameData.getCharacter(false).getAttack().getPlayerNumber();
    neural_net_input[21] = frameData.getCharacter(false).getAttack().getSettingSpeedX();
    neural_net_input[22] = frameData.getCharacter(false).getAttack().getSettingSpeedY();
    neural_net_input[23] = frameData.getCharacter(false).getAttack().getSpeedX();
    neural_net_input[24] = frameData.getCharacter(false).getAttack().getSpeedY();
    neural_net_input[25] = frameData.getCharacter(false).getAttack().getStartAddEnergy();
    neural_net_input[26] = frameData.getCharacter(false).getAttack().getStartUp();
    if(frameData.getCharacter(false).getAttack().isDownProp()) {
      neural_net_input[27] = 1;
    } else {
      neural_net_input[27] = 0;
    }
    if(frameData.getCharacter(false).getAttack().isProjectile()) {
      neural_net_input[28] = 1;
    } else {
      neural_net_input[28] = 0;
    }
    neural_net_input[29] = frameData.getCharacter(false).getAttack().getSettingHitArea().getBottom();
    neural_net_input[30] = frameData.getCharacter(false).getAttack().getSettingHitArea().getTop();
    neural_net_input[31] = frameData.getCharacter(false).getAttack().getSettingHitArea().getRight();
    neural_net_input[32] = frameData.getCharacter(false).getAttack().getSettingHitArea().getLeft();
    neural_net_input[33] = frameData.getCharacter(false).getBottom();
    neural_net_input[34] = frameData.getCharacter(false).getTop();
    neural_net_input[35] = frameData.getCharacter(false).getRight();
    neural_net_input[36] = frameData.getCharacter(false).getLeft();
    neural_net_input[37] = frameData.getCharacter(false).getEnergy();
    neural_net_input[38] = frameData.getCharacter(false).getHp();
    neural_net_input[39] = frameData.getCharacter(false).getLastHitFrame();
    neural_net_input[40] = frameData.getCharacter(false).getRemainingFrame();
    neural_net_input[41] = frameData.getCharacter(false).getSpeedX();
    neural_net_input[42] = frameData.getCharacter(false).getSpeedY();
    if(frameData.getCharacter(false).getState() == STAND) {
      neural_net_input[43] = 1;
    }
    if(frameData.getCharacter(false).getState() == CROUCH) {
      neural_net_input[43] = 2;
    }
    if(frameData.getCharacter(false).getState() == AIR) {
      neural_net_input[43] = 3;
    }
    if(frameData.getCharacter(false).getState() == DOWN) {
      neural_net_input[43] = 4;
    }
    LinkedList<Double> finalInput = null;
    for(int i = 0; i < neural_net_input.length; i++) {
      finalInput.add(neural_net_input[i]);
    }
    return finalInput;
  }
  @Override
  public void close() {
    System.out.println("wins for med AI"+winCount);
    System.out.println("draws for med AI"+drawCount);
    System.out.println("losses for med AI"+lossCount);
  }

  @Override
  public void getInformation(FrameData frameData, boolean playerNumber) {
    this.frameData = frameData;
    this.commandCenter.setFrameData(this.frameData, playerNumber);
    System.out.println("framedata updated");

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
    File file = new File("NN_setup2.txt");
   Scanner sc;
  try {
    sc = new Scanner(file);
  
   LinkedList<Double> src1 = new LinkedList<Double>();
   String check = sc.nextLine();
   
   while(check != "\n" && !check.isEmpty()) {
     double j = Double.parseDouble(check);
     if(j >= 1 || j <= -1 ) {
       j *= .01;
     }
     src1.add(j);
     check = sc.nextLine();
   }
 
   LinkedList<Double> src2 = new LinkedList<Double>();
   String check2 = sc.nextLine();
   
   while(check2 != "\n" && !check2.isEmpty()) {
     double j = Double.parseDouble(check2);
     if(j >= 1 || j <= -1 ) {
       j *= .01;
     }
     src2.add(j);
     check2 = sc.nextLine();
   }
   
   LinkedList<Double> src3 = new LinkedList<Double>();
   String check3 = sc.nextLine();
   
   while(check3 != "\n" && !check3.isEmpty()) {
     double j = Double.parseDouble(check3);
     if(j >= 1 || j <= -1 ) {
       j *= .01;
     }
     src3.add(j);
     check3 = sc.nextLine();
   }
   
   LinkedList<Double> src4 = new LinkedList<Double>();
   String check4 = sc.nextLine();
   
   while(check4 != "\n" && !check4.isEmpty()) {
     double j = Double.parseDouble(check4);
     if(j >= 1 || j <= -1 ) {
       j *= .01;
     }
     src4.add(j);
     if(sc.hasNextLine()) {
       check4 = sc.nextLine();
     }
     else {
       check4 = "\n";
     }
   }
   double[] src1Final = new double[src1.size()];
   for(int i = 0; i < src1.size(); i++) {
       src1Final[i] = src1.get(i);
   }
   
   double[] src2Final = new double[src2.size()];
   for(int i = 0; i < src2.size(); i++) {
       src2Final[i] = src2.get(i);
   }
   
   double[] src3Final = new double[src3.size()];
   for(int i = 0; i < src3.size(); i++) {
       src3Final[i] = src3.get(i);
   }
   
   double[] src4Final = new double[src4.size()];
   for(int i = 0; i < src4.size(); i++) {
       src4Final[i] = src4.get(i);
   }
   sc.close();
   neural_net = new NN(44,68,40,Matrix.fromArray(src1Final), Matrix.fromArray(src2Final), Matrix.fromArray(src3Final), Matrix.fromArray(src4Final));
   System.out.println(src1Final.length);
   System.out.println(src2Final.length);
   System.out.println(src3Final.length);
   System.out.println(src4Final.length);
  } catch (FileNotFoundException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }

    return 0;
  }

  @Override
  public Key input() {
    return key;
  }


  public double[] neuralNetInput(FrameData fd) {
    double[] neural_net_input = new double[44];
    if(!fd.getEmptyFlag()) {
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
    }
    else {
      for(int i = 0; i < neural_net_input.length; i++) {
        neural_net_input[i] = 1;
      }
    }
    return neural_net_input;
  }
  
  @Override
  public void processing() {
    if(canProcessing()) {
    if (commandCenter.getSkillFlag()) {
      key = commandCenter.getSkillKey();
    } else {
      key.empty();
      commandCenter.skillCancel();
    if(frameData != null) {
      double[] input = neuralNetInput(frameData); 
      if(neural_net != null) {
      List<Double> NN_output = neural_net.predict(input);
      int count = 0;
      double track = -9999;
      for(int i = 0; i < NN_output.size(); i++) {
        if(track < NN_output.get(i)) {
          track = NN_output.get(i);
          count = i;
        }
      }
    
    if(count > 39) {
      System.out.println(count);
      System.out.println("counter went over 40");
      commandCenter.commandCall(AllActions[0].name());
      
    }
    if(drawNegate == 3) {
      commandCenter.commandCall("DASH");
      drawNegate = 0;
    }
    
      else {
      commandCenter.commandCall(AllActions[count].name());
      if(myCharacter.getHp() == oppCharacter.getHp())
      {
        commandCenter.commandCall("DASH");
        drawNegate++;
      }
    }
    }
      else {
        System.out.println("nn failed");
      }
    }
   }
   }
  }

  @Override
  public void roundEnd(int arg0, int arg1, int arg2) {
    if(arg0 > arg1) {
      winCount++;
    }
    if(arg0 < arg1) {
      lossCount++;
    }
    
   if(arg0 == arg1) {
     drawCount++;
   }
  }
  
  private boolean canProcessing() {
    return !frameData.getEmptyFlag() && frameData.getRemainingFramesNumber() > 0;
  }

}
