package la4j.matrix;

import la4j.err.MatrixException;
import la4j.vector.Vector;

public abstract class MatrixUtils {

	private MatrixUtils() {
	};

	public static Matrix multyply(Matrix a, double d) {
		return a.multiply(d);
	}

	public static Matrix multiply(Matrix a, Matrix b) throws MatrixException {
		return a.multiply(b);
	}

	public static Vector multiply(Matrix a, Vector b) throws MatrixException {
		return a.multiply(b);
	}

	public static Matrix add(Matrix a, Matrix b) throws MatrixException {
		return a.add(b);
	}

	public static Matrix substract(Matrix a, Matrix b) throws MatrixException {
		return a.substract(b);
	}

	public static Matrix transpose(Matrix a) {
		return a.transpose();
	}

	public static Matrix expandMatrixByRow(Matrix a, Vector row) {
		Matrix b = a.clone();
		b.setRows(b.rows() + 1);

		try {
			for (int i = 0; i < b.columns(); i++) {
				b.set(b.rows() - 1, i, row.get(i));
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
		}

		return b;
	}

	public static Matrix expandMatrixByColumn(Matrix a, Vector column) {
		Matrix b = a.clone();
		b.setColumns(b.columns() + 1);

		try {
			for (int i = 0; i < b.rows(); i++) {
				b.set(i, b.columns() - 1, column.get(i));
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
		}

		return b;
	}

	public static Matrix expandMatrix(Matrix a, Vector row, Vector column) {
		Matrix b = a.clone();
		b.setRows(b.rows() + 1);
		b.setColumns(b.columns() + 1);

		try {
			for (int i = 0; i < b.columns(); i++) {
				b.set(b.rows() - 1, i, row.get(i));
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
		}

		try {
			for (int i = 0; i < b.rows(); i++) {
				b.set(i, b.columns() - 1, column.get(i));
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
		}

		return b;
	}

	public static Matrix createTreangleMatrixKeepCoeficients(Matrix a) {
		Matrix treangle = a.clone();

		for (int i = 0; i < treangle.rows(); i++) {

			for (int j = i + 1; j < treangle.rows(); j++) {
				double C = treangle.get(j, i) / treangle.get(i, i);

				for (int k = i; k < treangle.columns(); k++) {
					if (k == i) {
						treangle.set(j, k, C);
					} else {
						treangle.set(j, k, treangle.get(j, k)
								- treangle.get(i, k) * C);
					}
				}
			}
		}
		return treangle;
	}

	public static Matrix createTreangleMatrix(Matrix a) {
		Matrix treangle = a.clone();

		for (int i = 0; i < treangle.rows(); i++) {

			for (int j = i + 1; j < treangle.rows(); j++) {
				double C = treangle.get(j, i) / treangle.get(i, i);

				for (int k = i; k < treangle.columns(); k++) {
					if (k == i) {
						treangle.set(j, k, 0.0);
					} else {
						treangle.set(j, k, treangle.get(j, k)
								- treangle.get(i, k) * C);
					}
				}
			}
		}
		return treangle;
	}

}
