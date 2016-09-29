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

public class Main {
	private static boolean flagDFS;
	private static boolean firstCallDFS;
	private static String firstStringDFS;
	private static Set<String> dictDFS;
	private static ArrayList<String> ladderDFS;
	public static int wordLength; // length of words the world ladder is constructing from

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
		test = getWordLadderBFS("FLAGS", "CAGES");
		test = getWordLadderBFS("CODES", "CAGES");
		test = getWordLadderBFS("COOLS", "BUILD");
		test = getWordLadderBFS("REACH", "CATCH");

		test = getWordLadderBFS("LOOPS", "FRUIT");
		test = getWordLadderBFS("CREWS", "ABOUT");
		test = getWordLadderDFS("ALOOF", "CLANK");
		test = getWordLadderDFS("CODES", "CAGES");
		test = getWordLadderBFS("BREAK", "CAGES");
		test = getWordLadderBFS("MONEY", "CAGES");
		test.size();
		
		// TODO methods to read in words, output ladder
	}

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
		String read;
		int index = 0;
		ArrayList<String> temp = new ArrayList<String>();
		String first = "";
		String last = "";
		hold = "";
		read = keyboard.nextLine();
		if (read.equals("/quit")){
			temp.clear();
			return temp;
		}
		else{
			hold = keyboard.nextLine();
			for (int k = 0; hold.charAt(k) != ' '; k++ ){
				first = first + hold.charAt(k);
				index = k;
			}
			index = index + 1;
			while (true){
				if ((hold.charAt(index) == ' ') || (hold.charAt(index) == '\t') || (hold.charAt(index) == '\n')){
					index = index + 1;
				}
				else{
					break;
				}
			}
			for (int l = index; hold.charAt(l) != 0; l++ ){
				last = last + hold.charAt(l);
			}
			temp.add(first.toUpperCase());
			temp.add(last.toUpperCase());
			return temp;
		}

	}

	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		//The returned array is currently in reverse order.
		//Also there are some trailing nulls strings for some reason

		char[] binString = new char[wordLength];
		binString = start.toCharArray();
		char replacedChar;
		Set<String> nodeMatch = new HashSet<String>();
		ArrayList<String> prioritySearch = new ArrayList<String>();
		
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

	public static void printLadder(ArrayList<String> ladder) {
		int counter;
		counter = 0;
		while (ladder.get(counter) != null){
			System.out.println(ladder.get(counter));
			counter = counter + 1;
		}
	}
	
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