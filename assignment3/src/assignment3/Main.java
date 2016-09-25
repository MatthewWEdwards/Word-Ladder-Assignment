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
	public static int wordLength = 5; // length of words the world ladder is constructing from

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
		getWordLadderBFS("seats", "money");
		initialize();
		
		// TODO methods to read in words, output ladder
	}

	public static void initialize() {
		// initialize your static variables or constants here.

		// We will call this method before running our JUNIT tests. So call it

		// only once at the start of main.
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

		// TO DO

		return null;

	}

	public static ArrayList<String> getWordLadderDFS(String start, String end) {

		// Returned list should be ordered start to end. Include start and end.
		// Return empty list if no ladder.
		// TODO some code

		Set<String> dict = makeDictionary();

		// TODO more code

		return null; // replace this line later with real return
	}

	public static ArrayList<String> getWordLadderBFS(String start, String end) {

		ArrayList <String> currentChain = new ArrayList<String>();
		ArrayList <String> addChain = new ArrayList<String>(); // need to work on the use of this
		ArrayList <ArrayList<String>> nodeLists = new ArrayList<ArrayList<String>>();
		
		//TODO: initialize ArrayLists to size = 0
		
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
			
			//Remove items in current chain
			//TODO: make this more efficient
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
			
			//Remove analyzed node from dict
			//TODO: analyze if this is redundent
			String remove = new String(binString);
			if(!dict.contains(remove)){
				int temp = dict.size();
				temp = temp + 0;
			}
			dict.remove(remove.toLowerCase());
			

		}
		
	
		return null; // replace this line later with real return
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

	}
	// TODO
	// Other private static methods here
}
