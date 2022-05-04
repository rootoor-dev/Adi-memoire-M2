/******************************************************************************

Welcome to GDB Online.
GDB online is an online compiler and debugger tool for C, C++, Python, Java, PHP, Ruby, Perl,
C#, VB, Swift, Pascal, Fortran, Haskell, Objective-C, Assembly, HTML, CSS, JS, SQLite, Prolog.
Code, Compile, Run and Debug online from anywhere in world.

*******************************************************************************/
public class Main
{
	public static void main(String[] args) {
		System.out.println("Hello World");
		int[][] contents = { { 88, 66, 79 }, { 56, 25, 39 }, { 58, 47, 69 } };
 
                System.out.println("Loop Using Enhanced for loop:");
                for (int[] eachRow : contents) {
                        for (int j : eachRow) {
                                System.out.print(j + "\t");
                        }
                        System.out.println("");
                }
	}
}
