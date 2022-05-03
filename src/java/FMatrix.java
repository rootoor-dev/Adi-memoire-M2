

/*## 
 ##  class FMatrix
 ##   
 ##  This class extends Matrix to represent a Matrix of single precision
 ##  floating point numbers.  It contains all the BLAS (basic linear 
 ##  algebra subroutines) and other low-level functions common to all
 ##  matrices.  
 ##  FMatrix's contain no factoring or solving capabilities.
 ##
 ##*/

package edu.rice.linpack.Matrix.FMatrix;
import edu.rice.linpack.Matrix.Matrix;
import edu.rice.linpack.util.*;
import edu.rice.linpack.Vector.*;


public abstract class FMatrix extends Matrix {

  protected float[][] Mat;

  void cons() {
    pivot = new int[cols];
  }
  void cons(int i, int j) {
    Mat = new float[i][j];
    rows = i;
    cols = j;
    pivot = new int[cols];
  }
  void cons(FMatrix M) {
    rows = M.rows;
    cols = M.cols;
    Mat = new float[rows][cols];
    for(int i=0;i<rows;i++)
      for(int j=0;j<cols;j++) 
	Mat[i][j] = M.Mat[i][j];
    pivot = new int[cols];
  }    
  void cons(float[][] F) {
    rows = F.length;
    cols = (F[0]).length;
    Mat = F;
    pivot = new int[cols];
  }
  
  /*##
   ##  These routines return a copy of the desired object beginning at
   ##   the element (i,q)  
   ##*/

  public float getElem(int i, int q) {
    return Mat[i][q];
  }
  public float[] getRow(int i, int q) {
    float[] F = new float[cols-q];
    for(int j=0;j<(cols-q);j++)
      F[j] = Mat[i][q+j];
    return F;
  }
  public float[] getColumn(int i, int q) {
    float[] F = new float[rows-q];
    for(int j=0; j<(rows-q);j++) {
      F[j] = Mat[j+q][i];
    }
    return F;
  }

  /*##
   ##  this.setColumn changes column c to F  
   ##*/

  public void setColumn(int c,float[] F) {
    for(int r=0;r<rows;r++) {
      this.Mat[r][c] = F[r];
    }
  }
  public void setElem(int r, int c, float value) {
    this.Mat[r][c] = value;
  }

  public void Print() {
    System.out.println();
    for(int i=0;i<rows;i++) {
      for(int j=0;j<cols;j++) {
	System.out.print(Mat[i][j] + "     ");
      }
      System.out.println();
    }
    System.out.println();
  }

  /*##
   ##  this.swapElem swaps two elements in this FMatrix  
   ##*/

  public void swapElem(int rx, int cx, int ry, int cy) {
    float T = this.Mat[rx][cx];
    this.Mat[rx][cx] = this.Mat[ry][cy];
    this.Mat[ry][cy] = T;
  }

  /*#######################*/
  /*##   BLAS Routines   ##*/

  /*##
   ##  For all BLAS: 
   ##
   ##  On Input:
   ##
   ##     n         int
   ##               The number of elements to be effected. 
   ##
   ##     incx      int
   ##               The amount to increase the column number for 
   ##               this.Mat on each iteration.
   ##
   ##     r,rx      int 
   ##               The initial row in this.Mat.
   ##
   ##     c,cx      int
   ##               The initial column in this.Mat.
   ## 
   ##     B         float[], FMatrix, float[][]
   ##               The other object used in some calculations.
   ##     
   ##     incy      int
   ##               The amount to increase the column number for B.
   ##
   ##     ry        int
   ##               The initial row in B.
   ## 
   ##     cy        int
   ##               The initial column in B.
   ##
   ##     Da        float
   ##               A float used as a scalar.
   ##
   ##     Tg        FTrig
   ##               Holds the sine and cosine values formed in a Givens
   ##               rotation and used in a rotation.
   ##*/

  /*##
   ##  asum - sum of absolute values in a vector
   ##  Returns the sum as a float  
   ##*/

  public float asum(int n, int incx, int r, int c) {
    float dasum = 0;
    int ix = r;
    for(int i=0;i<n;i++,ix+=incx) 
      dasum += Math.abs(this.Mat[ix][c]);
    return dasum;
  }

  /*##
   ##  axpy - a const times a vector plus a vector 
   ##  Dy is changed in the loop and holds the desired value on return 
   ##*/

  public void axpy(int n, float Da, int incx, int rx, int cx, 
		   Matrix B, int incy, int ry, int cy) {
    
    float[][] Mat2 = ((FMatrix) B).Mat;

    if(n > 0 && Da != 0) {
      int ix = 0;
      int iy = 0;
      if(incx < 0) ix = (-n+1)*incx;
      if(incy < 0) iy = (-n+1)*incy;
      for(int i=0;i<n;i++,ix+=incx,iy+=incy) 
	Mat2[iy+ry][cy] += Da * Mat[ix+rx][cx];
    }
  }
  public void axpy(int n, float Da, int incx, int rx, int cx,
		   Vector Be, int incy, int ry) 
       throws WrongDataTypeException 
  {
    float[] B = Be.getFloatArray();
    this.axpy(n,Da,incx,rx,cx,B,incy,ry);
  }
  public void axpy(int n, float Da, int incx, int rx, int cx,
		   float[] B, int incy, int ry) {
    
    if(n > 0 && Da != 0) {
      int ix = 0;
      int iy = 0;
      if(incx < 0) ix = (-n+1)*incx;
      if(incy < 0) iy = (-n+1)*incy;
      for(int i=0;i<n;i++,ix+=incx,iy+=incy) 
	B[ry+iy] += Da*Mat[ix+rx][cx];
    }
  }

  /*##
   ##  This version takes an array of floats and modifies the calling object.
   ##  this.Mat is changed in the loop and holds the desired value on return.
   ##*/

  public void axpy(int n, float Da, Vector Be, int incy, int ry,
		   int incx, int rx, int cx) 
       throws WrongDataTypeException
  {
    float[] B = Be.getFloatArray();
    this.axpy(n,Da,B,incy,ry,incx,rx,cx);
  }
  public void axpy(int n, float Da, float[] B, int incy, int ry,
		   int incx, int rx, int cx) {
    if(n > 0 && Da != 0) {
      int ix = 0;
      int iy = 0;
      if(incx < 0) ix = (-n+1)*incx;
      if(incy < 0) iy = (-n+1)*incy;
      for(int i=0;i<n;i++,ix+=incx,iy+=incy) 
	this.Mat[rx+ix][cx] += Da*B[ry+iy];
    }
  }


  /*##
   ##  copy - copies one vector to another 
   ##  B is changed and holds the desired array 
   ##*/

  public void copy(int n, int incx, int rx, int cx, 
		   Vector Be, int incy) 
       throws WrongDataTypeException
  {
    float[] B = Be.getFloatArray();
    this.copy(n,incx,rx,cx,B,incy);
  }
  public void copy(int n, int incx, int rx, int cx, 
		   float[] B, int incy) {
    if(n > 0) {
      int ix = 0;
      int iy = 0;
      if(incx < 0) ix = (-n+1)*incx;
      if(incy < 0) iy = (-n+1)*incy;
      
      for(int i=0;i<n;i++,ix+=incx, iy+= incy) 
	B[iy] = Mat[ix+rx][cx];
    }
  }
  public void copy(int n, int incx, int rx, int cx, 
		   Matrix B, int incy, int ry, int cy) {
    
    float[][] Mat2 = ((FMatrix)B).Mat;
    
    if(n > 0) {
      int ix = 0;
      int iy = 0;
      if(incx < 0) ix = (-n+1)*incx;
      if(incy < 0) iy = (-n+1)*incy;
      
      for(int i=0;i<n;i++,ix+=incx, iy+= incy) 
	Mat2[iy+ry][cy] = Mat[ix+rx][cx];
    }
  }
  

  /*##
   ##  dot - Dot product between two vectors 
   ##  A float is returned  
   ##*/

  public float dot(int n, int incx, int rx, int cx,
		   FMatrix B, int incy, int ry, int cy) {
    
    float N = 0;
    
    if(n > 0){
      int ix = 0;
      int iy = 0;
      if(incx < 0) ix = (-n+1)*incx;
      if(incy < 0) iy = (-n+1)*incy;
      
      for(int i=0; i<n;i++,ix+= incx,iy += incy) 
	N += Mat[rx+ix][cx] * B.Mat[ry+iy][cy];
    }
    return N;
  }
  public float dot(int n, int incx, int rx, int cx,
		   Vector Be, int incy, int ry) 
       throws WrongDataTypeException
  {
    float[] B = Be.getFloatArray();
    return this.dot(n,incx,rx,cx,B,incy,ry);
  }
  public float dot(int n, int incx, int rx, int cx,
		   float[] B, int incy, int ry) {

    float N = 0;
    
    if(n > 0){
      int ix = 0;
      int iy = 0;
      if(incx < 0) ix = (-n+1)*incx;
      if(incy < 0) iy = (-n+1)*incy;
      
      for(int i=0; i<n;i++,ix+= incx,iy += incy) 
	N += Mat[rx+ix][cx] * B[ry+iy];
    }
    return N;
  }


  /*##
   ##  scal - multiply a vector by a scalar 
   ##  The calling object is changed  
   ##*/

  public void scal(int n, float Da, int incx, int r, int c) {

    if(n > 0 && Da != 1) {
      int ix = 0;
      for (int i=0;i<n;i++,ix += incx)
	Mat[ix+r][c] *= Da;
    }
  }      

  /*##
   ##  swap - interchanges two vectors 
   ##*/

  public void swap(int n, int incx, int rx, int cx, 
		   Matrix B, int incy, int ry, int cy) {
    
    float[][] Mat2 = ((FMatrix)B).Mat;

    if(n > 0) {
      int ix = 0;
      int iy = 0;
      if(incx < 0) ix = (-n+1)*incx;
      if(incy < 0) iy = (-n+1)*incy;
      float N;
      for(int i=0;i<n;i++) {
	N = Mat[ix+rx][cx];
	Mat[ix+rx][cx] = Mat2[iy+ry][cy];
	Mat2[iy+ry][cy] = N;
	ix += incx;
	iy += incy;
      }
    }
  }

  /*##
   ##  i_amax - finds the index of element having the max absolute value 
   ##  Returns the index with zero as first element 
   ##*/

  public int i_amax(int n, int incx, int r, int c) {
  
    int max = 0;
    float Dmax;
    
    if(n < 0) 
      return 0;  
    
    if(n > 0) {
      int ix = incx;
      Dmax = Math.abs(Mat[r][c]);
      for(int i=1;i<n;i++) {
	if(Math.abs(Mat[ix+r][c]) > Dmax) {
	  max = ix;
	  Dmax = Math.abs(Mat[ix+r][c]);
	}
	ix += incx;
      }
    } 
    return max;
  }
  
  /*##
   ##  rotg - construct a Givens plane rotation 
   ##  Tg contains the computed sin and cos values 
   ##*/
  
  public void rotg(int r, int c, Trig Tger) {

    FTrig Tg = (FTrig) Tger;

    float Roe = Tg.getB();
    float Da = this.Mat[r][c];
    float R;

    if(Math.abs(Da) > Math.abs(Tg.getB())) 
      Roe = Da;
    float Scale = Math.abs(Da) + Math.abs(Tg.getB());
    if(Scale == 0) {
      Tg.setCos(1);
      Tg.setSin(0);
      R = 0;
    }
    else {
      R = Scale*(float)Math.sqrt(Math.pow(Da/Scale,2) + 
				 Math.pow(Tg.getB()/Scale,2));
      if(Roe < 0) 
	R = -R;
      Tg.setCos(Da/R);
      Tg.setSin(Tg.getB()/R);
    }
    float Z = 1;
    if(Math.abs(Da) > Math.abs(Tg.getB())) 
      Z = Tg.getSin();
    else if(Tg.getCos() != 0) 
      Z = 1/Tg.getCos();

    this.Mat[r][c] = R;
    Tg.setB(Z);
  }
  
  /*##
   ##  rot applies a plane rotation from the parameters figured from Givens 
   ##*/

  public void rot(int n, int incx, int rx, int cx, Matrix B, 
		  int incy, int ry, int cy, Trig Tger) {
    
    FTrig Tg = (FTrig) Tger;
    float[][] Mat2 = ((FMatrix) B).Mat;

    if(n <= 0) 
      return;
    else {
      int ix = 0;
      int iy = 0;
      if(incx < 0) ix = (-n+1)*incx;
      if(incy < 0) iy = (-n+1)*incy;

      float temp;      
      for(int i=0;i<n;i++) {
        temp = (this.Mat[ix+rx][cx] * Tg.getCos()) + 
	  (Mat2[iy+ry][cy] * Tg.getSin());
        Mat2[iy+ry][cy] = (Mat2[iy+ry][cy] * Tg.getCos()) - 
	  (this.Mat[ix+rx][cx] * Tg.getSin());
        this.Mat[ix+rx][cx] = temp;
        ix += incx;
        iy += incy;
      }
    }
  }

  public float nrm2(int n, int incx, int r, int c) {
    
    int ix;
    float absxi, scale, ssq;
    
    if(n < 1 || incx < 1)
      return 0;
    else if( n == 1) 
      return Math.abs(Mat[r][c]);

    scale = 0;
    ssq = 1; 
    
    for(ix = 0;ix < n*incx;ix+=incx) {
      if(Mat[r+ix][c] != 0) {
	absxi = Math.abs(Mat[r+ix][c]);
	if(scale < absxi) {
	  ssq = 1 + ssq*(float)Math.pow((scale/absxi),2);
	  scale = absxi;
	}
	else 
	  ssq += (float)Math.pow( absxi/scale,2);
      }
    }
    return (scale*(float)Math.sqrt(ssq));
  }
}











