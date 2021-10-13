package neural_network;

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
}
