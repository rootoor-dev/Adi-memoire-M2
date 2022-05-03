package org.adi.similarity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Knn {
	
/**

By ADI Junior 

Computing binary data (fingerprints data) by couting the scores of some needed values called coefficients which are:

if we want to compare (calculate the similarity, proximity, dissemblance, distance... between) two objects X and Y
decribed binarly (holding only binary states), we have to know these values (coefficient)

n11 ====> X=1 & Y=1
n10 ====> X=1 & Y=0
n01 ====> X=0 & Y=1
n00 ====> X=0 & Y=0

*/

	public static void main(String[] args) {
		
//		# Define some binary vectors into a marix
			
		int[][] mat = {
				
				{0,1,0,0,0,1,0,0,1},
				{0,0,1,0,0,0,0,0,1},
				{1,1,0,0,0,1,0,0,0},
				{1,1,0,1,1,1,1,1,1}
		};
		
		int[][] newQuery = {{1,1,0,1,0,1,1,0,0}};  // C has as many cols as A.
		
		int n11;
		int n10;
		int n01;
		int n00;
		
		for (int i = 0; i < mat.length; i++) {
			
			n11 = 0;
			n10 = 0;
			n01 = 0;
			n00 = 0;
			
			for (int j = 0; j < mat[0].length; j++) {
        
        // simple couting of the coefficients
			    
				if(mat[i][j] == 1 && C[0][j]==1) {
					n11++;
				}
				if(mat[i][j] == 1 && C[0][j]==0) {
					n10++;
				}
				if(mat[i][j] == 0 && C[0][j]==1) {
					n01++;
				}
				if(mat[i][j] == 0 && C[0][j]==0) {
					n00++;
				}
			}
			
			// fin de parcours d'une ligne
			System.out.println("Ligne "+(i+1));
			System.out.println("n11 = "+n11+" n10= "+n10+" n01 ="+n01+" n00 ="+n00);
			System.out.println();
		    
		}

	}//main

}
