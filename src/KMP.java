import java.util.Random;
import java.lang.Math;

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
	private static int counter;

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
			counter++;
		}
		if (j == m) return i - m;    // found
		return -1;                    // not found
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
			counter++;
		}
		if (j == m) return i - m;    // found
		return -1;                    // not found
	}

	private static int bruteForceSearch(String pat, String text) {

		int index = -1;
		boolean done = false;
		while(!done) { //runs until done
			index ++; //increment index
			if(index >= text.length() - pat.length()) break;
			int matches = 0;
			char t = text.charAt(index);
			counter++;
			char p = pat.charAt(0);
			counter++;

			while(t == p) {
				matches++;
				if(matches == pat.length()) {
					done = true;
					break;
				}
				t = text.charAt(index + matches);
				counter++;
				p = pat.charAt(matches);
				counter++;

			}
		}
		if(!done) {
			index = -1;
		}

		return index;
	}

	public static int returnCount() {
		int tempCount = counter;
		counter = 0;
		return tempCount;
	}

	/**
	 * Creates a random text String of the given lengths using the provided alphabet.
	 * @return A string text
	 */
	private static String makeText(int txtL, String[] alphabet) {

		final int txtLen = txtL;
		String[] alph = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","x","t","u",
				"v","w","x","y","z"};
		if (alphabet != null)
		{
			alph = alphabet;
		}

		StringBuilder txt = new StringBuilder(txtLen);
		Random r = new Random();

		for(int p = 0; p < txtLen; p++) { txt.append(alph[r.nextInt(alph.length - 1)]);	}

		//System.out.printf("\nMade text: %s\n", txt);

		return txt.toString();
	}

	/**
	 * Runs a certain number of trials with text and patterns generated of specific lengths.
	 * Doesn't return any value but prints the highest number of array inspections for any
	 * of the trials and the average number of array inspections for the trials
	 * 
	 * Each trial runs the KMP and brute force algorithms on the generated text and pattern. 
	 * 
	 * @param txtSize size of text to be generated and searched
	 * @param patSize size of pattern to be generated and searched for in text
	 * @param numOfTrials number of trials of KMP to run
	 * @param alph a char array of the alphabet to be used to generate txt and pat
	 */
	public static void runTrials(int patSize, int txtSize, int numOfTrials, String[] alph) {
		// int arrays to store the number of array inspections for each alg for each trial
		int[] KMPresults = new int[numOfTrials];
		int[] BFresults = new int[numOfTrials];
		String txt = makeText(txtSize, alph);
		boolean failed = true;

		System.out.printf("Running %d trials for text size %d\n", numOfTrials, txtSize);

		for (int trial = 0; trial < numOfTrials; trial++) {
			failed = true;
			while(failed == true) {
				// create pat for trial and reset counter
				String pat = makeText(patSize, alph);
				counter = 0;

				// run brute force
				// if pat found, add array inspections to the array
				int BFindex = bruteForceSearch(pat, txt);
				int bfTrials = returnCount();
				if(BFindex != -1) {
					BFresults[trial] = bfTrials;
				}

				// run KMP
				// if pat found, add array inspections to the array
				KMP kmp1 = new KMP(pat);
				int offset1 = kmp1.search(txt);
				int trialsKMP = returnCount();

				if(offset1 != -1) {
					KMPresults[trial] = trialsKMP;
				}


				// check to make sure array has valid data in it (not all trials failed to find pat)
				for(int a = 0; a < KMPresults.length; a++) {
					if((KMPresults[a] != -1) || (patSize >= txtSize)) {
						failed = false;
						break;
					}
				}
			}
		}



		if (!failed) {
			// find max array inspections for KMP and sum to calculate the average
			int maxInspections = -1;
			int maxInspectionsBF = -1;
			int KMPavg = 0;
			int BFavg = 0;
			for (int i = 0; i < numOfTrials; i++) {
				if (KMPresults[i] > maxInspections) {
					maxInspections = KMPresults[i];
				}
				if (BFresults[i] > maxInspectionsBF) {
					maxInspectionsBF = i;
				}
				KMPavg += KMPresults[i];
				BFavg += BFresults[i];
			}
			if (alph.length == 2) {
                System.out.println("Max array inspections for pattern of length " + patSize + " on text of length "
                        + txtSize + " was " + maxInspections +
                        " \ncompared to brute force for the same pattern with " + maxInspectionsBF +
                        " for binary alphabet.");
            }
			else if (alph.length == 4) {
                System.out.println("Max array inspections for pattern of length " + patSize + " on text of length "
                        + txtSize + " was " + maxInspections +
                        " \ncompared to brute force for the same pattern with " + maxInspectionsBF +
                        " for DNA sequencing alphabet.");
            }
			else {
                System.out.println("Max array inspections for pattern of length " + patSize + " on text of length "
                        + txtSize + " was " + maxInspections +
                        " \ncompared to brute force for the same pattern with " + maxInspectionsBF +
                        " for regular alphabet.");
            }
			
			KMPavg = KMPavg / numOfTrials;
			BFavg = BFavg / numOfTrials;

			System.out.printf("Avg inspections for pat size %d: \n", patSize);
			System.out.println("Avg KMP: " + KMPavg);
			System.out.println("Avg Brute Force: " + BFavg);
			System.out.println("");
		}

	}

	/** 
	 * Takes a pattern string and an input string as command-line arguments;
	 * searches for the pattern string in the text string; and prints
	 * the first occurrence of the pattern string in the text string.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {

		String[] abc = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","x","t","u",
				"v","w","x","y","z"};
		String[] acgt = {"a", "c", "g", "t"};
		String[] binary = {"0", "1"};

		int txtSize = 8192;
		for (int i = 0; i < 9; i++) {
		    txtSize = txtSize * 2;
		    int patSize = 1;
		    for (int j = 0; j < 5; j++) {
		        patSize = patSize * 2;
                runTrials(patSize, txtSize, 10, abc);
                runTrials(patSize, txtSize, 10, acgt);
                runTrials(patSize, txtSize, 10, binary);
            }
		    patSize = 8192;
		    for (int k = 0; k < 9; k++) {
                patSize = patSize * 2;
                runTrials(patSize, txtSize, 10, abc);
                runTrials(patSize, txtSize, 10, acgt);
                runTrials(patSize, txtSize, 10, binary);
            }
        }
		
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
