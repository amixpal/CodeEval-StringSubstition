package com.interviews.challenges.lookout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

public class Main {

	/**
	 * 
	 * @author Mehmet Emre Aydinli
	 * 
	 *         Given a string S, and a list of strings of positive length,
	 *         F1,R1,F2,R2,...,FN,RN, proceed to find in order the occurrences
	 *         (left-to-right) of Fi in S and replace them with Ri. All strings
	 *         are over alphabet { 0, 1 }. Searching should consider only
	 *         contiguous pieces of S that have not been subject to replacements
	 *         on prior iterations. An iteration of the algorithm should not
	 *         write over any previous replacement by the algorithm.
	 * 
	 */

	private static FileInputStream fins = null;
	private static InputStreamReader ins = null;
	private static BufferedReader bfReader = null;
	private static LinkedList<String> originalLines = new LinkedList<String>();

	/**
	 * @param args
	 *            Input file path
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) {
		// Check if there is file path input
		if (args.length == 0) {
			System.out.println("No file path input. Exiting...");
			System.exit(1);
		}

		// We have some input, let's try to open it and start reading
		try {
			fins = new FileInputStream(args[0]);
			ins = new InputStreamReader(fins);
			bfReader = new BufferedReader(ins);
			String oneLine = null;

			// Add all lines in the file to the LinkedList<String> originalLines
			// so that we can close and release the IO objects
			while ((oneLine = bfReader.readLine()) != null) {
				originalLines.add(oneLine);
			}

			oneLine = null;
		} catch (FileNotFoundException e) {
			// Either it wasn't a file path or a bad path so exit out
			System.out.println("File not found.");
			System.exit(1);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure all resources are released after they have been used.
			if (fins != null) {
				try {
					fins.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// Release the reader last after all reading has been done.
			if (bfReader != null) {
				try {
					bfReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// Suggest a garbage collection since this objects are now released.
			System.gc();
		}

		// Start iterating on the list of lines in the file
		// Run the findAndReplace() to find new string
		// Print out Original String and New String
		ListIterator<String> it = originalLines.listIterator();
		int i = 0;
		while (it.hasNext()) {
			String tempString = it.next();
			String[] temp_whole = splitLine(tempString);
			String[] temp_f = extract_F(temp_whole[1]);
			String[] temp_r = extract_R(temp_whole[1]);
			System.out.println(findAndReplace(temp_whole[0], temp_f, temp_r));
			i++;
		}

		// Challenge condition: All programs should return a zero exit code on
		// success.
		System.exit(0);
	}

	/**
	 * Splits the original line between the original string and the F,R patterns
	 * 
	 * @param line
	 *            The original line
	 * @return String[] that contains two parts: original string to be
	 *         manipulated, and a string of {Fn, Rn}
	 */
	private static String[] splitLine(String line) {
		String[] temp = line.split(";");
		return temp;
	}

	/**
	 * Extracts F patterns
	 * 
	 * @param s
	 *            String of Fn, Rn
	 * @return String[] of Fn
	 */
	private static String[] extract_F(String s) {
		String[] temp = s.split(",");
		String[] fArray = new String[temp.length / 2];
		int j = 0;
		for (int i = 0; i < temp.length; i += 2) {
			fArray[j] = temp[i];
			j++;
		}
		return fArray;
	}

	/**
	 * Extracts R patterns
	 * 
	 * @param s
	 *            String of Fn, Rn
	 * @return String[] of Rn
	 */
	private static String[] extract_R(String s) {
		String[] temp = s.split(",");
		String[] rArray = new String[temp.length / 2];
		int j = 0;
		for (int i = 1; i < temp.length; i += 2) {
			rArray[j] = temp[i];
			j++;
		}
		return rArray;
	}

	/**
	 * Work on the original string with F and R patterns and produce new string
	 * 
	 * @param originalLine
	 *            String that we are going to work on
	 * @param fArray
	 *            String[] of Fn
	 * @param rArray
	 *            String[] of Rn
	 * @return New string after find and replace operations
	 */
	private static String findAndReplace(String originalLine, String[] fArray,
			String[] rArray) {

		String[] newline = originalLine.split("(?!^)");

		// i controls iteration through the whole strings in f and r
		for (int i = 0; i < fArray.length; i++) {
			String[] fn = fArray[i].split("(?!^)");
			String[] rn = rArray[i].split("(?!^)");
			// j for iteration through newline
			int j = 0;
			// k for iteration through fn/rn
			int k = 0;
			// marks the last known match index
			int marker = 0;
			// match counter
			int matchCounter = 0;
			int temp = 0;

			while (j < newline.length) {
				if (newline[j].equals(fn[k])) {
					// found a match
					// k+1 to check the next fn[]
					k++;
					// mark this position
					marker = j;
					// move to next newline[]
					j++;
					matchCounter++;
					if (k == fn.length) {
						temp++;						
						newline[j-k] = rArray[i];
						// Replace the string
						for(int m = 1 ; m < fn.length; m++) {
							newline[(j-k+m)] = "";
						}
						k = 0;
					}
				} else {
					// reset k, j and match counter
					matchCounter = 0;
					j = j - k + 1;
					k = 0;
				}
			}
		}
		
		String newString = "";
		for(String t : newline) {
			newString += t;
		}
		
		return newString;
	}
}