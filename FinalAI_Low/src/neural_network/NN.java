package neural_network;

import java.util.LinkedList;
import java.util.List;

import enumerate.Action;
public class NN {
  
  Matrix weights_input_hidden;
  Matrix weights_hidden_output; 
  Matrix bias_hidden; 
  Matrix bias_output;    
  double learning_rate=0.1;
  Action bestAction;
  
  public NN(int input, int hidden, int output, Matrix wih, Matrix who, Matrix bh, Matrix bo) {
    weights_input_hidden = new Matrix(hidden, input);
    weights_hidden_output = new Matrix(output, hidden);
    
    bias_hidden = new Matrix(hidden, 1);
    bias_output = new Matrix(output, 1);
    
    int k = 0;
    for(int i = 0; i < weights_input_hidden.rows; i++) {
      for(int j = 0; j < weights_input_hidden.cols; j++) {
        weights_input_hidden.data[i][j] = wih.data[k][0];
        k++;
      }
    }
    
    k = 0;
    for(int i = 0; i < weights_hidden_output.rows; i++) {
      for(int j = 0; j < weights_hidden_output.cols; j++) {
        weights_hidden_output.data[i][j] = who.data[k][0];
        k++;
      }
    }
    
    k = 0;
    for(int i = 0; i < bias_hidden.rows; i++) {
      for(int j = 0; j < bias_hidden.cols; j++) {
        bias_hidden.data[i][j] = bh.data[k][0];
        k++;
      }
    }
    
    k = 0;
    for(int i = 0; i < bias_output.rows; i++) {
      for(int j = 0; j < bias_output.cols; j++) {
        bias_output.data[i][j] = bo.data[k][0];
        k++;
      }
    }
  }
  
  public NN(int input, int hidden, int output) {
    weights_input_hidden = new Matrix(hidden, input);
    weights_hidden_output = new Matrix(output, hidden);
    
    bias_hidden = new Matrix(hidden, 1);
    bias_output = new Matrix(output, 1);
  }
  public NN(Matrix wih, Matrix wio, Matrix bh, Matrix bo) {
    weights_input_hidden = wih;
    weights_hidden_output = wio;
    bias_hidden = bh;
    bias_output = bo;
  }
  
  public Matrix getWIH() {
    return weights_input_hidden;
  }
  
  public Matrix getWHO() {
    return weights_hidden_output;
  }
  
  public Matrix getBH() {
    return bias_hidden;
  }
  
  public Matrix getBO() {
    return bias_output;
  }
  
  public List<Double> predict(double[] ds)
  {
      Matrix input = Matrix.fromArray(ds);
      Matrix hidden = Matrix.multiply(weights_input_hidden, input);
      hidden.add(bias_hidden);
      hidden.sigmoid();
      
      Matrix output = Matrix.multiply(weights_hidden_output, hidden);
      output.add(bias_output);
      output.sigmoid();
      
      return output.toArray();
  }
  
  public void train(double[] x, double[] y)
  {
      Matrix input = Matrix.fromArray(x);
      Matrix hidden = Matrix.multiply(weights_input_hidden, input);
      hidden.add(bias_hidden);
      hidden.sigmoid();
      
      Matrix output = Matrix.multiply(weights_hidden_output, hidden);
      output.add(bias_output);
      output.sigmoid();
      
      Matrix target = Matrix.fromArray(y);
      
      Matrix error = Matrix.subtract(target, output);
      Matrix gradient = output.dsigmoid();
      gradient.multiply(error);
      gradient.multiply(learning_rate);
      
      Matrix hidden_T = Matrix.transpose(hidden);
      Matrix who_delta =  Matrix.multiply(gradient, hidden_T);
      
      weights_hidden_output.add(who_delta);
      bias_output.add(gradient);
      
      Matrix who_T = Matrix.transpose(weights_hidden_output);
      Matrix hidden_errors = Matrix.multiply(who_T, error);
      
      Matrix h_gradient = hidden.dsigmoid();
      h_gradient.multiply(hidden_errors);
      h_gradient.multiply(learning_rate);
      
      Matrix i_T = Matrix.transpose(input);
      Matrix wih_delta = Matrix.multiply(h_gradient, i_T);
      
      weights_input_hidden.add(wih_delta);
      bias_hidden.add(h_gradient);
      
  }
  
  //public void fit(double[][] x, double[][] y, int epochs)
  //{
  //    for(int i = 0; i < epochs; i++)
  //    {    
  //        int sampleN =  (int)(Math.random() * x.length );
  //        this.train(x[sampleN].Arrays.asList(), y[sampleN]).asList();
  //    }
  //}
  
  public Action returnBestAction() {
    return bestAction; 
  }
  
}
