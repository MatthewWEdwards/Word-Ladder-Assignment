/* WORD LADDER Main.java
 
* EE422C Project 3 submission by
 
* Matthew Edwards
* EID: mwe295
* Unique Number: 16445
 
* Johnny Rojas
* EID: jr52483
* Unique Number: 16445
* Slip days used: <0>
 
* Git URL: https://github.com/MatthewWEdwards/EE442C_Project3.git
* 
* Fall 2016
 
*/

package assignment3;

import java.util.*;
import java.io.*;

/**
 * The BFS and DFS word ladder class.
 * 
 * @author Matthew Edwards
 * @author JohnnyAngel Rojas
 */
public class Main {
	// This flag is true when the end of a DFS is found
	private static boolean flagDFS;
	// This flag is true between DFS calls, and is false during a DFS call. Its
	// handled within the DFS
	private static boolean firstCallDFS;
	// This string is used when returning from a successful DFS recursive call,
	// to test if the top call is reached
	private static String firstStringDFS;
	// dictDFS is used exclusively between recursive DFS calls
	private static Set<String> dictDFS;
	// This is a ladder which can be called between recursive DFS calls
	private static ArrayList<String> ladderDFS;
	// length of words the world ladder is constructing from
	public static int wordLength;
	// This string is used if printLadder is sent an empty array, it tells DFS
	// which ladder couldn't be found
	private static String parseStart;
	// This string is used if printLadder is sent an empty array, it tells DFS
	// which ladder couldn't be found
	private static String parseEnd;
	// This flag tells the DFS if the searching the reverse heuristic failed by
	// causing a stack overflow
	private static boolean twoStackOverflows;

	public static void main(String[] args) throws Exception {

		Scanner kb; // input Scanner for commands

		PrintStream ps; // output file
		// If arguments are specified, read/write from/to files instead of Std
		// IO.

		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps); // redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out; // default to Stdout
		}

		initialize();
		ArrayList<String> test = new ArrayList<String>();
		ArrayList<String> input = new ArrayList<String>();
		initialize();
		while (true) {
			input = parse(kb);
			test = getWordLadderDFS(input.get(0), input.get(1));
			printLadder(test);
			test = getWordLadderBFS(input.get(0), input.get(1));
			printLadder(test);
		}

		// TODO methods to read in words, output ladder
	}

	/**
	 * This method initializes static variables for the DFS and BFS methods
	 */
	public static void initialize() {

		dictDFS = makeDictionary();
		ladderDFS = new ArrayList<String>();
		flagDFS = false;
		firstCallDFS = true;
		firstStringDFS = null;
		wordLength = 5;
		parseStart = new String();
		parseEnd = new String();
	}

	/**
	 * This method takes the input words from the console, and puts them into an
	 * array list. if "/quit" appears anywhere in the input, main terminates.
	 * 
	 * @param keyboard:
	 *            The keyboard scanner.
	 * @return: An array list containing two strings, start at index 0, and end
	 *          at index 1.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		String hold;
		int length = 0;
		int index = 0;
		ArrayList<String> temp = new ArrayList<String>();
		String first = "";
		String last = "";
		hold = keyboard.nextLine();

		// Test for quit case
		if (hold.contains("/quit")) {
			temp.clear();
			System.exit(0);
			return temp;
		}
		// Read input 
		//(assumed to be two 5 letter words which exist in the dictionary)
		else {
			for (int k = 0; hold.charAt(k) != ' '; k++) {
				first = first + hold.charAt(k);
				index = k;
			}
			index = index + 1;
			length = index;
			while (true) {
				if ((hold.charAt(index) == ' ') || (hold.charAt(index) == '\t') || (hold.charAt(index) == '\n')) {
					index = index + 1;
				} else {
					break;
				}
			}
			for (int l = index; l < index + length; l++) {
				last = last + hold.charAt(l);
			}
			temp.add(first.toUpperCase());
			parseStart = first.toLowerCase();
			temp.add(last.toUpperCase());
			parseEnd = last.toLowerCase();
			return temp;
		}
	}

	/**
	 * This method generates a word ladder, if one exists, between start and
	 * end. This method uses a recursive DFS algorithm, with some heuristics to
	 * find the ladder faster. This method catches stack overflow exceptions and
	 * handles them. This method calls removeRedundancies() to reduce the size
	 * of the list it generates.
	 * 
	 * @param start:
	 *            start of the potential word ladder
	 * @param end:
	 *            end of the potential word ladder
	 * @return: an array list which contains the word ladder. Returns empty if
	 *          no word ladder exists.
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		// The returned array is currently in reverse order.
		// Also there are some trailing nulls strings for some reason

		char[] binString = new char[wordLength];
		binString = start.toCharArray();
		char replacedChar;
		Set<String> nodeMatch = new HashSet<String>();
		ArrayList<String> prioritySearch = new ArrayList<String>();

		try {
			// flagDFS represents if the end has been found
			if (flagDFS == true) {
				flagDFS = false;
			}

			if (firstCallDFS == true) {
				ladderDFS.clear();
				dictDFS = makeDictionary();
				firstStringDFS = start;
				firstCallDFS = false;
			}

			// Test for recursion end-case
			if (start.equals(end)) {
				flagDFS = true;
				return ladderDFS;
			}

			// Remove current string from dictionary
			dictDFS.remove(start);

			// Generate List
			while (true) {
				// Generate Nodes
				for (int i = 0; i < wordLength; i++) {
					for (char charChange = 'A'; charChange <= 'Z'; charChange++) {

						// test for the case when iterating when binString is
						// not changed
						if (binString[i] == charChange) {
							continue;
						}

						replacedChar = binString[i];
						binString[i] = charChange;
						String test = new String(binString);

						if (dictDFS.contains(test) == true) {
							nodeMatch.add(test);
						}
						binString[i] = replacedChar; // Prepare binString for
														// further modification
					}
				}

				// Order Nodes by priority
				String[] nodeMatchArray = new String[nodeMatch.size()];
				nodeMatchArray = nodeMatch.toArray(new String[wordLength]);
				for (int nodeMatchIndex = nodeMatch.size() - 1; nodeMatchIndex >= 0; nodeMatchIndex--) {
					int charMatches = 0;
					for (int k = 0; k < wordLength; k++) {
						if (nodeMatchArray[nodeMatchIndex].charAt(k) == end.charAt(k)) {
							charMatches++;
						}
					}
					nodeMatchArray[nodeMatchIndex] += charMatches;
				}

				// Generate priority List
				for (char matchingChars = (char) (wordLength + 48); matchingChars >= '0'; matchingChars--) {
					for (int nodeMatchIndex = nodeMatch.size() - 1; nodeMatchIndex >= 0; nodeMatchIndex--) {
						if (nodeMatchArray[nodeMatchIndex].charAt(wordLength) == matchingChars) {
							prioritySearch.add(nodeMatchArray[nodeMatchIndex]);
						}
					}
				}

				// Move on to next node, find end or call recursively
				for (int nodeMatchIndex = 0; nodeMatchIndex <= prioritySearch.size() - 1; nodeMatchIndex++) {
					// Call recursively
					ladderDFS = (getWordLadderDFS(prioritySearch.get(nodeMatchIndex).substring(0, 5), end));

					// Handle returning from recursive calls if the end has been
					// found
					if (flagDFS) {
						if (firstStringDFS.equals(start)) { // prioritySearch.get(nodeMatchIndex)
															// for end of list
							ladderDFS.add(prioritySearch.get(nodeMatchIndex).substring(0, 5));
							ladderDFS.add(start);
							break;
						}
						ladderDFS.add(prioritySearch.get(nodeMatchIndex).substring(0, 5));
						return (ladderDFS);
					}

					// Handle returning from recursive calls if the end hasn't
					// been found
					else {
						// remove dead node from dictionary
						dictDFS.remove(prioritySearch.get(nodeMatchIndex));
					}
				}

				// Manage end of DFS
				if (flagDFS) {
					// Put list in proper order
					ArrayList<String> reverseDFS = new ArrayList<String>();
					for (int j = ladderDFS.size() - 1; j >= 0; j--) {
						reverseDFS.add(ladderDFS.get(j));
					}

					// Remove redundant stuff from node list
					removeRedundancies(reverseDFS);
					reverseDFS.trimToSize();
					firstCallDFS = true; // Prepare for next DFS call
					return reverseDFS;

				}
				// If this if statement is true, we have run out of places to
				// search
				if (start.equals(firstStringDFS)) {
					firstCallDFS = true; // Prepare for next DFS call
					ladderDFS.clear();
					return ladderDFS;
				}
				// Return from a dead end
				return ladderDFS;
			}
		} catch (final Exception stackOverflowError) {
			if (twoStackOverflows) {
				ladderDFS.clear();
				twoStackOverflows = false;
				return ladderDFS;
			} else {
				twoStackOverflows = true;
			}
			return getWordLadderDFS(end, start);

		}
	}

	/**
	 * This method generates a word ladder, if one exists, between start and
	 * end. This method uses an iterative BFS algorithm.
	 * 
	 * @param start:
	 *            start of the potential word ladder
	 * @param end:
	 *            end of the potential word ladder
	 * @return: an array list which contains the word ladder. Returns empty if
	 *          no word ladder exists.
	 */
	public static ArrayList<String> getWordLadderBFS(String start, String end) {

		ArrayList<String> currentChain = new ArrayList<String>();
		ArrayList<String> addChain = new ArrayList<String>();
		ArrayList<ArrayList<String>> nodeLists = new ArrayList<ArrayList<String>>();

		char[] binString = new char[wordLength];
		char replacedChar;

		// Generate dictionary
		Set<String> dict = makeDictionary();

		// test for trivial case
		if (start == end) {
			return currentChain;
		}

		// initialize nodeLists, handle the start string.
		ArrayList<String> first = new ArrayList<String>();
		first.add(start.toUpperCase());
		nodeLists.add(first);
		dict.remove(start);

		// nodeLists represents potential paths which lead to end. When it is
		// empty, all potential
		// paths have been exhausted.
		while (nodeLists.size() > 0) {
			currentChain = nodeLists.remove(0); // pop nodeLists
			binString = (currentChain.get(currentChain.size() - 1)).toUpperCase().toCharArray();

			for (int j = 0; j < currentChain.size(); j++) {
				dict.remove(currentChain.get(j));
			}

			// Find all adjacent, and test if the end is reached.
			for (int i = 0; i < wordLength; i++) {
				for (char charChange = 'A'; charChange <= 'Z'; charChange++) {
					// test for the case when iterating when binString is not
					// changed
					if (binString[i] == charChange) {
						continue;
					}

					replacedChar = binString[i];
					binString[i] = charChange;
					String test = new String(binString);

					if (dict.contains(test) == true) {
						currentChain.add(test);

						// Test for end
						if (test.equals(end.toUpperCase())) {
							currentChain.trimToSize();
							return currentChain;
						}

						// Add string to chain, and prepare currentChain for
						// reuse.
						addChain = new ArrayList<String>(currentChain);
						nodeLists.add(addChain);
						currentChain.remove(currentChain.size() - 1);

					}

					binString[i] = replacedChar;

				}
			}

		}
		currentChain.clear();
		return currentChain;
	}

	/**
	 * This method generates a set which represents a dictionary of words. This
	 * method extracts these words from a text file.
	 * 
	 * @return: The dictionary set.
	 */
	public static Set<String> makeDictionary() {

		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner(new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}

	/**
	 * This function prints the ladder passed to if. If the ladder is empty, the
	 * function prints a statement, explaining that there is no word ladder
	 * between two input words.
	 * 
	 * @param ladder:
	 *            The ladder to be printed. If this is empty, then print the no
	 *            ladder statement.
	 */
	public static void printLadder(ArrayList<String> ladder) {
		int counter;
		counter = 0;
		if (ladder.size() == 0) {
			System.out.println("no word ladder can be found between " + parseStart + " and " + parseEnd + ".\n");
			return;
		}
		System.out.println("");
		System.out.println("a " + (ladder.size() - 2) + "-rung word ladder exists between "
				+ ladder.get(0).toLowerCase() + " and " + ladder.get(ladder.size() - 1).toLowerCase() + ".");
		while (counter < ladder.size()) {
			System.out.println((ladder.get(counter)).toLowerCase());
			counter = counter + 1;
		}
	}

	/**
	 * This method removes certain redundancies in a word ladder. suppose we
	 * have a word ladder. If position 1 and position 8 are one letter different
	 * from one another, then positions 2-7 are redundant in this word ladder.
	 * This function detects redundancies of this nature, and removes them.
	 * 
	 * @param ladder:
	 *            the ladder to have its redundancies removed.
	 */
	private static void removeRedundancies(ArrayList<String> ladder) {
		char replacedChar;
		char[] binString = new char[ladder.get(0).length()];

		for (int listIndex = 0; listIndex < ladder.size() - 1; listIndex++) {
			// The below line prevents the remove loop from functioning if no
			// remove index is found
			int finalRemoveIndex = listIndex + 2;
			for (int redundantIndex = listIndex + 1; redundantIndex < ladder.size() - 1; redundantIndex++) {
				for (int i = 0; i < wordLength; i++) {
					for (char charChange = 'A'; charChange <= 'Z'; charChange++) {

						binString = ladder.get(redundantIndex).toCharArray();
						if (binString[i] == charChange) {
							continue;
						}

						replacedChar = binString[i];
						binString[i] = charChange;
						String test = new String(binString);

						if (ladder.get(listIndex).equals(test)) {
							finalRemoveIndex = redundantIndex;
						}
					}
				}

			}
			for (int removeIndex = listIndex + 1; removeIndex < finalRemoveIndex; removeIndex++) {
				if (ladder.size() != listIndex + 2) {
					ladder.remove(listIndex + 1);
				}
			}
		}
	}
}