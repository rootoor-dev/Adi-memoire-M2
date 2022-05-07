package org.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import Jama.Matrix;



public class TestJama {

	public static void main(String[] args) {

		// Define some binary vectors

		double[][] mat = {
				{ 0., 1., 0., 0., 0., 1., 0., 0., 1. }, 
				{ 0., 0., 1., 0., 0., 0., 0., 0., 1. }, 
				{ 1., 1., 0., 0., 0., 1., 0., 0., 0. },
				{ 1., 1., 0., 1., 1., 1., 1., 1., 1. } 
		};

		double[][] C = { { 1, 1, 0, 1, 0, 1, 1, 0, 0 } }; // C has as many cols as A.
		
		// Usage de JAMA - Java Matrix class
		
		Matrix matrix = new Matrix(mat);
		
		Matrix mm = new Matrix(C);
		
		//Extraction d'une ligne de la matrice en matrice-ligne | matrice-colonne 2D via JAMA
		//matrix.getMatrix(0, 0, 0, 8).print(9, 0);
		// matrix.getMatrix(0, 0, 0,matrix.getColumnDimension()-1).print(matrix.getColumnDimension(), 0)
		
		// double[][] array2D = matrix.getMatrix(0, 0, 0,matrix.getColumnDimension()-1).getArray();
		
		//Extraction d'une ligne de la matrice en tableau 2D via JAMA
		
//		for(int i=0; i<array2D.length;i++) {
//			for(int j=0; j<array2D[0].length; j++) {
//				System.out.print(array2D[i][j]+" ");
//			}
//			System.out.println();
//		}
		
		// produit matriciel "Matrice x Vector"
		Matrix array2D;
		array2D = matrix.times(mm.transpose());
		
		// afficher un element du resultat du produit
		//System.out.println(array2D.get(0, 0));
		
		//transformer le produit en tableau 2D
		double[][] array = array2D.getArray();
		
		for(int i=0; i<array.length;++i) {
			for(int j=0; j<array[0].length;++j) {
				System.out.print(array[i][j]+"\t");
			}
		}
		
		
		
		
		
		
		

		/*
		 * 
		 * 
		 * int n11; int n10; int n01; int n00;
		 * 
		 * for (int i = 0; i < mat.length; i++) {
		 * 
		 * n11 = 0; n10 = 0; n01 = 0; n00 = 0;
		 * 
		 * for (int j = 0; j < mat[0].length; j++) {
		 * 
		 * if(mat[i][j] == 1 && C[0][j]==1) { n11++; } if(mat[i][j] == 1 && C[0][j]==0)
		 * { n10++; } if(mat[i][j] == 0 && C[0][j]==1) { n01++; } if(mat[i][j] == 0 &&
		 * C[0][j]==0) { n00++; } }
		 * 
		 * // fin de parcours d'une ligne System.out.println("Ligne "+(i+1));
		 * System.out.println("n11 = "+n11+" n10= "+n10+" n01 ="+n01+" n00 ="+n00);
		 * System.out.println();
		 * 
		 * }
		 */
		
	}// main

} // class
