# Idées de développement de l'outil en JAVA

Dans ce mémoire, les collections ou arrays seront beaucoup manipulés.
A défaut d'utiliser des librairies externes, il est conseillé de développer ses propres classes ou librairies.


```Java
/******************************************************************************

Welcome to GDB Online.
GDB online is an online compiler and debugger tool for C, C++, Python, Java, PHP, Ruby, Perl,
C#, VB, Swift, Pascal, Fortran, Haskell, Objective-C, Assembly, HTML, CSS, JS, SQLite, Prolog.
Code, Compile, Run and Debug online from anywhere in world.

*******************************************************************************/
public class Main
{
		// Driver program
	public static void main(String arg[])
	{
	// Declaring number of rows and columns
		int n = 3, m = 3;
		int array[]=new int[100];
	
		// Initialising a 2-d array
		int grid[][] = {{1, 2, 3},
						{4, 5, 6},
						{7, 8, 9}};
	
		// storing elements in 1-d array
		int i, j, k = 0;
		for (i = 0; i < n; i++)
		{
			for (j = 0; j < m; j++)
			{
				k = i * m + j;
				array[k] = grid[i][j];
				k++;
			}
		}
	
		// displaying elements in 1-d array
		for (i = 0; i < n; i++)
		{
			for (j = 0; j < m; j++)
				System.out.print((array[i * m + j])+" ");
			System.out.print("\n");
		}
}    
```


# Extraire chaque ligne d'un tableau 2D ou plus puis la traiter

Le traitement consiste en la multiplication (formulation algébrique) de VectorA*transpose(VectorB) pour trouver les différentes valeurs recherchées telles que:
- n11
- n10
- n01
- n00

```Java
int[][] mat = {
		{ 0, 1, 0, 0, 0, 1, 0, 0, 1 }, 
		{ 0, 0, 1, 0, 0, 0, 0, 0, 1 }, 
		{ 1, 1, 0, 0, 0, 1, 0, 0, 0 },
		{ 1, 1, 0, 1, 1, 1, 1, 1, 1 } 
		};

		int[][] C = { { 1, 1, 0, 1, 0, 1, 1, 0, 0 } }; // C has as many cols as A.
 
		 int n11; int n10; int n01; int n00;
		  
		 for (int i = 0; i < mat.length; i++) {
		  
		 n11 = 0; n10 = 0; n01 = 0; n00 = 0;
		 
		 for (int j = 0; j < mat[0].length; j++) {
		  
		 if(mat[i][j] == 1 && C[0][j]==1) { n11++; } if(mat[i][j] == 1 && C[0][j]==0)
		 { n10++; } if(mat[i][j] == 0 && C[0][j]==1) { n01++; } if(mat[i][j] == 0 &&
		 C[0][j]==0) { n00++; } }
		  
		 // fin de parcours d'une ligne System.out.println("Ligne "+(i+1));
		  System.out.println("n11 = "+n11+" n10= "+n10+" n01 ="+n01+" n00 ="+n00);
		  System.out.println();
		  
		 }
		 
```
