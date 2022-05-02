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
