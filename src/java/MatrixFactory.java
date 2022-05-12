package la4j.matrix;

import java.util.Random;

public abstract class MatrixFactory {

	private MatrixFactory() {
	}

	public static Matrix createMatrix() {
		return new Matrix();
	}

	public static Matrix createMatrix(int row, int col) {
		return new Matrix(row, col);
	}

	public static Matrix createMatrix(double array[][]) {
		return new Matrix(array);
	}

	public static Matrix createMatrixWithCopy(double array[][]) {
		int row = array.length;
		int col = array[0].length;

		double arraycopy[][] = new double[col][row];

		for (int i = 0; i < row; i++) {
			System.arraycopy(array[i], 0, arraycopy[i], 0, col);
		}

		return new Matrix(arraycopy);
	}

	public static Matrix createRandomMatrix(int row, int col) {
		Matrix m = new Matrix(row, col);

		Random gen = new Random();

		for (int i = 0; i < m.rows(); i++) {
			for (int j = 0; j < m.columns(); j++) {
				m.set(i, j, gen.nextDouble());
			}
		}

		return m;
	}

	public static Matrix createIdentityMatrix(int row, int col) {
		Matrix m = new Matrix(row, col);

		for (int i = 0; i < m.rows(); i++) {
			m.set(i, i, (double) 1.0);
		}

		return m;
	}

	public static Matrix createSquareMatrix(int dim) {
		return new Matrix(dim, dim);
	}

	public static Matrix createSquareIdentityMatrix(int dim) {
		return createIdentityMatrix(dim, dim);
	}

}
