package mcts;

import enumerate.Action;
import struct.FrameData;
public class TrainingExample {
  FrameData state;
  Action action;
  int result;
  
  public TrainingExample(FrameData state, Action action) {
    this.state = state;
    this.action = action;
  }
  
  public TrainingExample(FrameData state) {
    this.state = state;
  }
  
  public void setAction(Action action) {
    this.action = action;
  }
  public void setResult(int result) {
    this.result = result;
  }
  
  public FrameData getState() {
    return state;
  }
  
  public Action getAction() {
    return action;
  }
  
  public int getResult() {
    return result;
  }
}
