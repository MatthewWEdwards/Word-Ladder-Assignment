/* WORD LADDER Main.java
 
* EE422C Project 3 submission by
 
* Replace <...> with your actual data.
 
* <Student1 Name>
 * <Student1 EID>
 
* <Student1 5-digit Unique No.>
 
* <Student2 Name>
 * <Student2 EID>
 
* <Student2 5-digit Unique No.>
 
* Slip days used: <0>
 
* Git URL:
 
* Fall 2016
 
*/

package assignment3;

import java.util.*;
import java.io.*;

/**
 * @author Matthew Edwards
 *
 */
public class Main {
	private static boolean flagDFS;
	private static boolean firstCallDFS;
	private static String firstStringDFS;
	private static Set<String> dictDFS;
	private static ArrayList<String> ladderDFS;
	public static int wordLength; // length of words the world ladder is constructing from
	private static String parseStart;
	private static String parseEnd;

	public static void main(String[] args) throws Exception {
		
		
		Scanner kb;	// input Scanner for commands
		
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		
		initialize();
		ArrayList<String> test = new ArrayList<String>();
		ArrayList<String> input = new ArrayList<String>();
		initialize();
		while(true){
			input = parse(kb);
			test = getWordLadderDFS(input.get(0), input.get(1));
			printLadder(test);
			test = getWordLadderBFS(input.get(0), input.get(1));
			printLadder(test);
		}
		
		// TODO methods to read in words, output ladder
	}

	/**
	 * 
	 */
	public static void initialize() {
		// initialize your static variables or constants here.

		// We will call this method before running our JUNIT tests. So call it

		// only once at the start of main.
		
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
	 * 
	 * @param keyboard
	 *            Scanner connected to System.in
	 * 
	 * @return ArrayList of 2 Strings containing start word and end word.
	 * 
	 *         If command is /quit, return empty ArrayList.
	 * 
	 */


	public static ArrayList<String> parse(Scanner keyboard) {
		String hold;
		int length = 0;
		int index = 0;
		ArrayList<String> temp = new ArrayList<String>();
		String first = "";
		String last = "";
		hold = keyboard.nextLine();
		
		
		if (hold.contains("/quit")){
			temp.clear();
			System.exit(0);
			return temp;
		}
		else{
			for (int k = 0; hold.charAt(k) != ' '; k++ ){
				first = first + hold.charAt(k);
				index = k;
			}
			index = index + 1;
			length = index;
			while (true){
				if ((hold.charAt(index) == ' ') || (hold.charAt(index) == '\t') || (hold.charAt(index) == '\n')){
					index = index + 1;
				}
				else{
					break;
				}
			}
			for (int l = index; l < index + length; l++ ){
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
	 * @param start
	 * @param end
	 * @return
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		//The returned array is currently in reverse order.
		//Also there are some trailing nulls strings for some reason

		char[] binString = new char[wordLength];
		binString = start.toCharArray();
		char replacedChar;
		Set<String> nodeMatch = new HashSet<String>();
		ArrayList<String> prioritySearch = new ArrayList<String>();
		
		try{
			//flagDFS represents if the end has been found
			if(flagDFS == true){
				flagDFS = false;
			}
			
			if(firstCallDFS == true){
				ladderDFS.clear();
				dictDFS = makeDictionary();
				firstStringDFS = start;
				firstCallDFS = false;
			}
			
			
			//Test for recursion end-case
			if(start.equals(end)){
				flagDFS = true;
				return ladderDFS;
			}
			
			
			//Remove current string from dictionary
			dictDFS.remove(start);
			
			//Generate List
			while(true){
				//Generate Nodes
				for(int i = 0; i < wordLength; i++){
					for(char charChange = 'A'; charChange <= 'Z'; charChange++ ){
						
						//test for the case when iterating when binString is not changed
						if(binString[i] == charChange){
							continue;
						}
						
						replacedChar = binString[i];
						binString[i] = charChange;
						String test = new String(binString);
						
						if(dictDFS.contains(test) == true){
							nodeMatch.add(test);
						}
						binString[i] = replacedChar;	//Prepare binString for further modification
					}
				}
				
				//Order Nodes by priority
				String[] nodeMatchArray = new String[nodeMatch.size()];
				nodeMatchArray = nodeMatch.toArray(new String[wordLength]);
				for(int nodeMatchIndex = nodeMatch.size() - 1; nodeMatchIndex >= 0; nodeMatchIndex--){
					int charMatches = 0;
						for (int k = 0; k < wordLength; k++) {
							if (nodeMatchArray[nodeMatchIndex].charAt(k) == end.charAt(k)) {
								charMatches++;
							} 
						}
					nodeMatchArray[nodeMatchIndex] += charMatches;	
				}
				
				//Generate priority List
				for(char matchingChars =(char) (wordLength + 48); matchingChars >= '0'; matchingChars--){
					for(int nodeMatchIndex = nodeMatch.size() - 1; nodeMatchIndex >= 0; nodeMatchIndex--){
						if(nodeMatchArray[nodeMatchIndex].charAt(wordLength) == matchingChars){
							prioritySearch.add(nodeMatchArray[nodeMatchIndex]);
						}
					}
				}
				
				//Move on to next node, find end or call recursively
				for(int nodeMatchIndex = 0; nodeMatchIndex <= prioritySearch.size() - 1; nodeMatchIndex++){
					//Call recursively
					ladderDFS = (getWordLadderDFS(prioritySearch.get(nodeMatchIndex).substring(0, 5), end));
	
					//Handle returning from recursive calls if the end has been found
					if(flagDFS){
						if(firstStringDFS.equals(start)){ // prioritySearch.get(nodeMatchIndex) for end of list
							ladderDFS.add(prioritySearch.get(nodeMatchIndex).substring(0, 5));
							ladderDFS.add(start);
							break;
						}
						ladderDFS.add(prioritySearch.get(nodeMatchIndex).substring(0, 5));
						return (ladderDFS);
					} 
					
					//Handle returning from recursive calls if the end hasn't been found
					else{
						dictDFS.remove(prioritySearch.get(nodeMatchIndex));	//remove dead node from dictionary
					}
				}
				
				//Manage end of DFS
				if(flagDFS){    
					//Put list in proper order
					ArrayList <String> reverseDFS = new ArrayList<String>();
					for(int j = ladderDFS.size() - 1; j >= 0; j--){
						reverseDFS.add(ladderDFS.get(j));
					}
					
					//Remove redundant stuff from node list
					removeRedundancies(reverseDFS);
					reverseDFS.trimToSize();
					firstCallDFS = true; // Prepare for next DFS call
					return reverseDFS;
					
				}
				//If this if statement is true, we have run out of places to search
				if(start.equals(firstStringDFS)){
					firstCallDFS = true; //Prepare for next DFS call
					ladderDFS.clear();
					return ladderDFS;
				}
				//Return from a dead end
				return ladderDFS;
			}
		}
		catch(final Exception stackOverflowError){
			return getWordLadderDFS(end, start);
			
		}
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public static ArrayList<String> getWordLadderBFS(String start, String end) {

		ArrayList <String> currentChain = new ArrayList<String>();
		ArrayList <String> addChain = new ArrayList<String>(); // need to work on the use of this
		ArrayList <ArrayList<String>> nodeLists = new ArrayList<ArrayList<String>>();
		
		//TODO: initialize ArrayLists to size = 0 (Do i really need to do this?)
		//TODO: test to make sure BFS runs fast enough
		
		char[] binString = new char[wordLength];
		char replacedChar;
		
		//Generate dictionary
		Set<String> dict = makeDictionary();
		
		//test for trivial case
		if(start == end){
			return currentChain;
		}
		
		//initialize nodeLists
		ArrayList <String> first = new ArrayList<String>();
		first.add(start.toUpperCase());
		nodeLists.add(first);
		dict.remove(start);
		
		while(nodeLists.size() > 0){
			currentChain = nodeLists.remove(0); // pop nodeLists
			binString = (currentChain.get(currentChain.size() - 1)).toUpperCase().toCharArray();

			for(int j = 0; j < currentChain.size(); j++){
				dict.remove(currentChain.get(j));
			}
			
			//Find all adjacent, and test if the end is reached.
			for(int i = 0; i < wordLength; i++){
				for(char charChange = 'A'; charChange <= 'Z'; charChange++ ){
					//test for the case when iterating when binString is not changed
					if(binString[i] == charChange){
						continue;
					}
					
					replacedChar = binString[i];
					binString[i] = charChange;
					String test = new String(binString);
					
					if(dict.contains(test) == true){
						currentChain.add(test);
						
						//Test for end
						if(test.equals(end.toUpperCase())){
							currentChain.trimToSize();
							return currentChain;
						}
						
						//Add string to chain, and prepare currentChain for reuse.
						addChain = new ArrayList<String>(currentChain);
						nodeLists.add(addChain);
						currentChain.remove(currentChain.size() -1);
					
					}
					
					binString[i] = replacedChar;
					
				}
			}

		}
		currentChain.clear();
		return currentChain; 
	}

	/**
	 * @return
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
	 * function prints a statement, explaining that there is no word ladder between two
	 * input words.
	 * @param ladder
	 */
	public static void printLadder(ArrayList<String> ladder) {
		int counter;
		counter = 0; 
		if(ladder.size() == 0){
			System.out.println("no word ladder can be found between " + parseStart +  " and " + parseEnd + ".\n");
			return;
		}
		System.out.println("");
		System.out.println("a " + (ladder.size() - 2) + "-rung word ladder exists between " + ladder.get(0).toLowerCase() + " and " + ladder.get(ladder.size() - 1).toLowerCase() + ".");
		while (counter < ladder.size()){
			System.out.println((ladder.get(counter)).toLowerCase());
			counter = counter + 1;
		}
	}
	
	/**
	 * This function removes certain redundancies in a word ladder. suppose we have a word ladder.
	 * If position 1 and position 8 are one letter different from one another, 
	 * then positions 2-7 are redundant in this word ladder. 
	 * This function detects redundancies of this nature, and removes them.
	 * 
	 * @param ladder: the ladder to have its redundancies removed.
	 */
	private static void removeRedundancies(ArrayList<String> ladder){
		char replacedChar;
		char[] binString = new char[ladder.get(0).length()];
		
		
		for(int listIndex = 0; listIndex < ladder.size()-1; listIndex++){
			int finalRemoveIndex = listIndex + 2;//This line prevents the remove loop from functioning if no remove index is found
			for(int redundantIndex = listIndex + 1; redundantIndex < ladder.size()-1; redundantIndex++){
				for(int i = 0; i < wordLength; i++){
					for(char charChange = 'A'; charChange <= 'Z'; charChange++ ){
						
						binString = ladder.get(redundantIndex).toCharArray();
						if(binString[i] == charChange){
							continue;
						}
						
						replacedChar = binString[i];
						binString[i] = charChange;
						String test = new String(binString);
						
						if(ladder.get(listIndex).equals(test)){
							finalRemoveIndex = redundantIndex;
						}
					}
				}
				

			}
			for(int removeIndex = listIndex+1; removeIndex < finalRemoveIndex; removeIndex++){
				if(ladder.size() != listIndex+2){
					ladder.remove(listIndex+1);
				}
			}
		}
	}
	// TODO
	// Other private static methods here
}