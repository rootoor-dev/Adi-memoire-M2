package la4j.matrix;

import java.util.Arrays;

import la4j.err.MatrixException;
import la4j.vector.Vector;
import la4j.vector.VectorFactory;

/**
 * Matrix class.
 * 
 * 
 * @author Vladimir Kostukov <vladimir.kostukov@gmail.com>
 * 
 *
 */
public class Matrix implements Cloneable {
	private double matrix[][];

	private int rows; // rows of matrix
	private int columns; // columns of matrix

	/* default constructor */
	public Matrix() {
		matrix = new double[0][0];
		rows = 0;
		columns = 0;
	}

	/* constructor from double array */
	public Matrix(double array[][]) {
		matrix = array;
		rows = array.length;
		columns = array[0].length;
	}

	/* constructor with specify dimmention */
	public Matrix(int row, int col) {
		matrix = new double[row][col];
		rows = row;
		columns = col;
	}

	public double get(int i, int j) {
		return matrix[i][j];
	}

	public void set(int i, int j, double value) {
		matrix[i][j] = value;
	}

	public void setRows(int rows) {
		this.rows = rows;

		double self[][] = new double[rows][columns];

		for (int i = 0; i < rows; i++) {
			self[i] = matrix[i];
		}

		matrix = self;
	}

	public void setColumns(int columns) {
		this.columns = columns;

		double self[][] = new double[rows][columns];

		for (int i = 0; i < rows; i++) {
			self[i] = Arrays.copyOf(matrix[i], columns);
		}

		matrix = self;

	}

	public double[][] toArray() {
		return matrix;
	}

	public double[][] toArrayCopy() {
		double arrcopy[][] = new double[rows][columns];

		for (int i = 0; i < rows; i++) {
			System.arraycopy(matrix[i], 0, arrcopy[i], 0, columns);
		}

		return arrcopy;
	}

	public void swapRows(int i, int j) {
		double dd[] = matrix[i];
		matrix[i] = matrix[j];
		matrix[j] = dd;
	}

	public void swapColumns(int i, int j) {
		for (int _i = 0; _i < rows; _i++) {
			double d = matrix[_i][i];
			matrix[_i][i] = matrix[_i][j];
			matrix[_i][j] = d;
		}

	}

	public int rows() {
		return rows;
	}

	public int columns() {
		return columns;
	}

	public Matrix transpose() {
		double self[][] = new double[columns][rows];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				self[j][i] = matrix[i][j];
			}
		}
		return new Matrix(self);

	}

	public Matrix multiply(double d) {
		double self[][] = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				self[i][j] = matrix[i][j] * d;
			}
		}
		return new Matrix(self);
	}

	public Matrix multiply(Matrix m) throws MatrixException {
		if (columns == m.rows) {
			double self[][] = new double[rows][m.columns];

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < m.columns; j++) {
					for (int k = 0; k < columns; k++) {
						self[i][j] += matrix[i][k] * m.matrix[k][j];
					}
				}
			}
			return new Matrix(self);
		} else {
			throw new MatrixException(
					"can not multiply this matrix: wrong dimmentions");
		}
	}

	public Vector multiply(Vector v) throws MatrixException {
		if (columns == v.length()) {
			double self[] = new double[v.length()];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					self[i] += matrix[i][j] * v.get(j);
				}
			}
			return new Vector(self);
		} else {
			throw new MatrixException(
					"can not multiply this matrix on vector: wrong dimmentions");
		}
	}

	public Matrix substract(Matrix m) throws MatrixException {
		if (rows == m.rows && columns == m.columns) {
			double self[][] = new double[rows][columns];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < 0; j++) {
					self[i][j] = matrix[i][j] - m.matrix[i][j];
				}
			}

			return new Matrix(self);

		} else {
			throw new MatrixException(
					"can not substract this matrix: wrong dimmentions");
		}

	}

	public Matrix add(Matrix m) throws MatrixException {
		if (rows == m.rows && columns == m.columns) {
			double self[][] = new double[rows][columns];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < 0; j++) {
					self[i][j] = matrix[i][j] + m.matrix[i][j];
				}
			}

			return new Matrix(self);
		} else {
			throw new MatrixException(
					"can not sum this matrix: wrong dimmentions");
		}
	}

	public double trace() {
		double tr = 1;
		for (int i = 0; i < rows; i++) {
			tr *= matrix[i][i];
		}

		return tr;
	}

	public Vector getRow(int i) {
		Vector v = VectorFactory.createVector(columns);
		for (int j = 0; j < columns; j++) {
			v.set(j, matrix[i][j]);
		}

		return v;
	}

	public Vector getColumn(int i) {
		Vector v = VectorFactory.createVector(rows);
		for (int j = 0; j < rows; j++) {
			v.set(j, matrix[j][i]);
		}

		return v;
	}

	public void setColumn(int i, Vector column) {
		for (int j = 0; j < column.length(); j++) {
			matrix[j][i] = column.get(j);
		}
	}

	public void setRow(int i, Vector row) {
		for (int j = 0; j < row.length(); j++) {
			matrix[i][j] = row.get(j);
		}
	}
	
	public boolean isSymmetric() {
		boolean ret = true;
		for (int i = 0; i < rows; i++) {
			for (int j = i + 1; j < columns; j++) {
				ret = ret && matrix[i][j] == matrix[j][i];
			}
		}
		return ret && rows > 0 && columns > 0;
	}
	
	public boolean isTreangle() {
		boolean ret = true;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < i+1; j++) {
				ret = ret && matrix[i][j] == 0.0;
			}
		}
		return ret && rows > 0 && columns > 0;
	}

	@Override
	public Matrix clone() {
		Matrix clon = new Matrix(rows, columns);

		for (int i = 0; i < rows; i++) {
			System.arraycopy(matrix[i], 0, clon.matrix[i], 0, columns);
		}

		return clon;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				sb.append(String.format("%8.3f", matrix[i][j]));
			}
			sb.append("\n");
		}

		return sb.toString();
	}

}
