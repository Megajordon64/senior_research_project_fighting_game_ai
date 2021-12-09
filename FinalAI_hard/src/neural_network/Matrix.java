package neural_network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Matrix {
  double[][] data;
  int rows;
  int cols;
  
  public Matrix(int rows, int cols){
    this.rows = rows;
    this.cols = cols;
    
    data = new double[rows][cols];
    
    for(int i = 0; i < rows; i++) {
      for(int j = 0; j < cols; j++) {
        data[i][j] = Math.random()*2 - 1;
      }
    }
  }
  
  public void add(double singleUnit) {
    
    for(int i = 0; i < rows; i++) {
      for(int j = 0; j < cols; j++) {
        data[i][j] = data[i][j] + singleUnit;
      }
    }
  }
  
  public void add(Matrix mat) {
    if(rows != mat.rows || cols != mat.cols) {
      System.out.println("matrix sizes are not the same and cannot be added");
      return;
    }
    for(int i = 0; i < rows; i++) {
      for(int j = 0; j < cols; j++) {
        data[i][j] = data[i][j] + mat.data[i][j];
      }
    }
  }
  
  public static Matrix subtract(Matrix matA, Matrix matB) {
    if(matA.rows != matB.rows || matA.cols != matB.cols) {
      System.out.println("matrix sizes are not the same and cannot be subtracted");
    }

    Matrix newMatrix = new Matrix(matA.rows, matA.cols);
    
    for(int i = 0; i < matA.rows; i++) {
      for(int j = 0; j < matA.cols; j++) {
        newMatrix.data[i][j] = matA.data[i][j] + matB.data[i][j];
      }
    }
    
    return newMatrix;
  }
  
  public static Matrix transpose(Matrix matrix) {
    Matrix newMatrix = new Matrix(matrix.cols, matrix.rows);
    for(int i = 0; i < matrix.rows; i++)
    {
        for(int j = 0; j < matrix.cols; j++)
        {
            newMatrix.data[j][i] = matrix.data[i][j];
        }
    }
    return newMatrix;
  }
  
  public static Matrix multiply(Matrix matrix1, Matrix matrix2) {
    Matrix newMatrix = new Matrix(matrix1.rows, matrix2.cols);
    for(int i = 0; i < newMatrix.rows; i++)
    {
        for(int j = 0; j < newMatrix.cols; j++)
        {
            double sum=0;
            for(int k = 0; k < matrix1.cols; k++)
            {
                sum = sum + (matrix1.data[i][k] * matrix2.data[k][j]);
            }
            newMatrix.data[i][j] = sum;
        }
    }
    return newMatrix;
  }
  
  public void multiply(Matrix matrix) {
    for(int i = 0; i < matrix.rows; i++)
    {
        for(int j = 0; j < matrix.cols;j++)
        {
            data[i][j] = data[i][j] * matrix.data[i][j];
        }
    }
    
  }
  
  public void multiply(double scalar) {
    for(int i = 0; i < rows; i++)
    {
        for(int j = 0; j < cols; j++)
        {
            data[i][j] = data[i][j] * scalar;
        }
    }
    
  }
  
  public void sigmoid() {
    for(int i = 0; i < rows; i++)
    {
        for(int j = 0; j < cols; j++)
            data[i][j] = 1/(1+Math.exp(-data[i][j])); 
    }
    
  }
  
  public Matrix dsigmoid() {
    Matrix newMatrix = new Matrix(rows,cols);
    for(int i=0; i<rows; i++)
    {
        for(int j = 0; j < cols; j++)
            newMatrix.data[i][j] = data[i][j] * (1-data[i][j]);
    }
    return newMatrix;
    
  }
  
  public static Matrix fromArray(double[] ds)
  {
      Matrix newMatrix = new Matrix(ds.length,1);
      for(int i = 0; i < ds.length; i++)
          newMatrix.data[i][0] = ds[i];
      return newMatrix;
      
  }
  
  public List<Double> toArray() {
    List<Double> newArray = new ArrayList<Double>();
    
    for(int i = 0;i < rows; i++)
    {
        for(int j = 0; j < cols;j ++)
        {
            newArray.add(data[i][j]);
        }
    }
    return newArray;
}
}
