import java.util.Random;

/******************************************************************************
 *  Compilation:  javac KMP.java
 *  Execution:    java KMP pattern text
 *  Dependencies: StdOut.java
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  KMP algorithm.
 *
 *  % java KMP abracadabra abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:               abracadabra          
 *
 *  % java KMP rab abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:         rab
 *
 *  % java KMP bcara abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:                                   bcara
 *
 *  % java KMP rabrabracad abacadabrabracabracadabrabrabracad 
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  % java KMP abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ******************************************************************************/



/**
 *  The {@code KMP} class finds the first occurrence of a pattern string
 *  in a text string.
 *  <p>
 *  This implementation uses a version of the Knuth-Morris-Pratt substring search
 *  algorithm. The version takes time proportional to <em>n</em> + <em>m R</em>
 *  in the worst case, where <em>n</em> is the length of the text string,
 *  <em>m</em> is the length of the pattern, and <em>R</em> is the alphabet size.
 *  It uses extra space proportional to <em>m R</em>.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/53substring">Section 5.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class KMP {
	private final int R;       // the radix
	private int[][] dfa;       // the KMP automoton

	private char[] pattern;    // either the character array for the pattern
	private String pat;        // or the pattern string

	/**
	 * Preprocesses the pattern string.
	 *
	 * @param pat the pattern string
	 */
	public KMP(String pat) {
		this.R = 256;
		this.pat = pat;

		// build DFA from pattern
		int m = pat.length();
		dfa = new int[R][m]; 
		dfa[pat.charAt(0)][0] = 1; 
		for (int x = 0, j = 1; j < m; j++) {
			for (int c = 0; c < R; c++) 
				dfa[c][j] = dfa[c][x];     // Copy mismatch cases. 
			dfa[pat.charAt(j)][j] = j+1;   // Set match case. 
			x = dfa[pat.charAt(j)][x];     // Update restart state. 
		} 
	} 

	/**
	 * Preprocesses the pattern string.
	 *
	 * @param pattern the pattern string
	 * @param R the alphabet size
	 */
	public KMP(char[] pattern, int R) {
		this.R = R;
		this.pattern = new char[pattern.length];
		for (int j = 0; j < pattern.length; j++)
			this.pattern[j] = pattern[j];

		// build DFA from pattern
		int m = pattern.length;
		dfa = new int[R][m]; 
		dfa[pattern[0]][0] = 1; 
		for (int x = 0, j = 1; j < m; j++) {
			for (int c = 0; c < R; c++) 
				dfa[c][j] = dfa[c][x];     // Copy mismatch cases. 
			dfa[pattern[j]][j] = j+1;      // Set match case. 
			x = dfa[pattern[j]][x];        // Update restart state. 
		} 
	} 

	/**
	 * Returns the index of the first occurrence of the pattern string
	 * in the text string.
	 *
	 * @param  txt the text string
	 * @return the index of the first occurrence of the pattern string
	 *         in the text string; N if no such match
	 */
	public int search(String txt) {

		// simulate operation of DFA on text
		int m = pat.length();
		int n = txt.length();
		int i, j;
		for (i = 0, j = 0; i < n && j < m; i++) {
			j = dfa[txt.charAt(i)][j];
		}
		if (j == m) return i - m;    // found
		return n;                    // not found
	}

	/**
	 * Returns the index of the first occurrence of the pattern string
	 * in the text string.
	 *
	 * @param  text the text string
	 * @return the index of the first occurrence of the pattern string
	 *         in the text string; N if no such match
	 */
	public int search(char[] text) {

		// simulate operation of DFA on text
		int m = pattern.length;
		int n = text.length;
		int i, j;
		for (i = 0, j = 0; i < n && j < m; i++) {
			j = dfa[text[i]][j];
		}
		if (j == m) return i - m;    // found
		return n;                    // not found
	}

	/**
	 * Creates a pattern and a text of the given lengths using the provided alphabet. 
	 * Alphabet cannot contain semicolons ";"
	 * @return A string of the pattern and text separated by a semicolon
	 */
	private static String makeText() {

		// --- experimental variables ---
		final int patLen =2;
		final int txtLen = 100;
		final String[] alph = new String[]{"a","b","c","d","e","f","g","h","i","j",
				"k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		// {"0,1"}
		// {"a","c","t","g"}

		StringBuilder pat = new StringBuilder(patLen);
		StringBuilder txt = new StringBuilder(txtLen);
		StringBuilder total = new StringBuilder(patLen + txtLen);
		Random r = new Random();


		// build pat
		for(int p = 0; p < patLen; p++) { pat.append(alph[r.nextInt(alph.length - 1)]);	}

		// build txt
		for(int p = 0; p < txtLen; p++) { txt.append(alph[r.nextInt(alph.length - 1)]);	}

		// combine and return total
		total.append(pat);
		total.append(";");
		total.append(txt);

		//System.out.printf("Made: %s\n", total.toString());
		
		return total.toString();
	}

	/** 
	 * Takes a pattern string and an input string as command-line arguments;
	 * searches for the pattern string in the text string; and prints
	 * the first occurrence of the pattern string in the text string.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {

		// check for args, then either use args or makeText
		String pat = "";
		String txt = "";
		
		if(args.length != 0) {

			if(args.length == 2) {
				pat = args[0];
				txt = args[1];
			}
			else System.out.printf("\nWrong number of arguments provided: %d\n", args.length);
		}
		else {
			String total = makeText();
			
			// build pat
			int index = 0;
			char c = total.charAt(index);
			while(c != ';') {
				pat = pat + c;
				index++;
				c = total.charAt(index);
			}
			
			// build text
			index++; // clear semicolon
			for(int i = index; i < total.length(); i++) {
				c = total.charAt(i);
				txt = txt + c;
			}
		}

		char[] pattern = pat.toCharArray();
		char[] text    = txt.toCharArray();
		
		//System.out.printf("Pat: %s, Txt: %s\n", pat, txt);
		
		KMP kmp1 = new KMP(pat);
		int offset1 = kmp1.search(txt);

		KMP kmp2 = new KMP(pattern, 256);
		int offset2 = kmp2.search(text);

		// print results
		System.out.println("text:    " + txt);

		System.out.print("pattern: ");
		for (int i = 0; i < offset1; i++)
			System.out.print(" ");
		System.out.println(pat);

		System.out.print("pattern: ");
		for (int i = 0; i < offset2; i++)
			System.out.print(" ");
		System.out.println(pat);
	}
}

/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
