/*****************************************************************************
 MONASH UNIVERSITY, Faculty of Information Technology, Clayton School of IT.
 Student Declaration for FIT1008 Submission. I Callum White, ID:24571520
 declare that this submission is my own work and has not been copied from any
 other source without attribution. I acknowledge that severe penalties exist
 for any copying of code without attribution, including a fail mark for this prac.
 *****************************************************************************/

import java.io.*;

/**
 * Part of a prac to give students experience using hash tables. 
 * Modified from prac 4
 * 
 * @author          Dhananjay Thiruvady
 * @modified		Brendon Taylor (Oct 2012 - Comments)
 * @since           May 2011
 */

public class Dictionary {
	private final int SIZE, HASH_PRIME;
	
	protected class Word implements Hashable{
		protected String word;
		
		public Word(String newWord){
			word = newWord;
		}
		
		/**
		 * @complexity O(word length)
		 */
		public int hash() {
			//from page 24
//			int value = 0, a = 31415, b = 27183;
//			for (int i = 0; i < word.length(); i++) {
//				value = (word.charAt(i) + a*value) % SIZE;
//				a = a * b % (SIZE-1);
//			}
//			return value;
			//from page 18
			int value = 0;
			for (int i = 0; i < word.length(); i++) {
				value = (word.charAt(i) + HASH_PRIME*value) % SIZE;
			}
			return value;

		}

		/**
		 * @complexity O(compareTo)
		 */
		public boolean equals(Object otherObject){
			if(otherObject instanceof Word){
				if(this.toString().compareTo(otherObject.toString()) == 0){
					return true;					
				}
			}
			return false;
		}
		
		public String toString(){
			return word;
		}

		
	}
	
	public Dictionary(int size, int prime){
		SIZE = size;
		HASH_PRIME = prime;
	}
	
	/**
	 * @comp O(U) * OCommand, where U is the number of times the user 
	 *       chooses to enter a command (a large number) and OCommand is the 
	 *       complexity of the best/worst command
	 */
	public static void menu(int size, int prime) {
		boolean quit = false;
		String inputLine = null;
		String[] command;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		Dictionary dict = new Dictionary(size, prime);
		LinearProbe<Word, Frequency> lpHashTable = new LinearProbe<Word, Frequency>(size);
		String menu = "Prac10 Dictionary Menu Options\n" + 
				"  1. Read file\n" +
				"  2. Search word\n" +
				"  3. List words\n" +
				"  4. Quit\n";

		// Command parsing loop
		try {
			while(!quit) {
				System.out.println(menu);			// Print the menu
				inputLine = console.readLine();		// Read a command
				command = inputLine.split(" ");
				if (command[0].equals("1")) {		// Read file
					String fileName;
					System.out.print("Enter the name of a file: ");
					fileName = console.readLine();
					BufferedReader file = new BufferedReader(new FileReader(fileName));
					String line;
					String[] items;
					Word newWord;
					Frequency frequency;
					try {
			            while ((line = file.readLine()) != null){	//insert all dictionary words and word frequencies into the linear probe hashtable 
			            	items = line.split(" ");
			            	//System.out.println("item: " + items[0]);
			            	if(items[0].length() > 0 && isAlpha(items[0])){
			            		newWord = dict.new Word(items[0]);
			            		int num = Integer.parseInt(items[1]);
			            		if(items[1].length() > 0 && num < 4 && num >= 0){
			            			frequency = new Frequency(num);
			            			lpHashTable.insert(newWord, frequency);
			            		} else {
			            			System.out.println("Error reading from file: " + fileName + ". Incorrect format");
			            		}
			            	} else{
			            		System.out.println("Error reading from file: " + fileName + ". Incorrect format");
			            	}
			            }
			            lpHashTable.displayCounters();
			            lpHashTable.resetCounters();
			            
			        } catch (IOException e) {
			            // if we got here, then the readLine() died
			            System.out.println("Error reading from file: " + fileName);
			        } catch (NumberFormatException e) {
			        	System.out.println("Error reading from file: " + fileName + ". Incorrect format");
			        }
				} else if (command[0].equals("2")) {	// Search word
					System.out.print("Enter a word to search the dictionary for: ");
					String input = console.readLine();
					if(isAlpha(input)){
						Frequency frequency = lpHashTable.search(dict.new Word(input.toLowerCase()));
						if(frequency == null){			//print error if not found, otherwise print frequency
							System.out.println("\"" + input + "\" was not found.");
						} else{
							System.out.println("\"" + input + "\" found. \nFrequency: " + frequency.toString());
						}
						lpHashTable.displayCounters();
						lpHashTable.resetCounters();
					}
					
				} else if (command[0].equals("3")) { // List words
					System.out.println(lpHashTable.toString());
				} else if (command[0].equals("4")) { // Quit
					quit = true;
				} else {
					System.out.println("Unrecognized command.");
				}       
			}
		} catch (IOException e) {
			System.out.println("Error reading from console");
			inputLine = "";
		}
	}
	
	/**
	 * @comp O(U) * OCommand, where U is the number of times the user 
	 *       chooses to enter a command (a large number) and OCommand is the 
	 *       complexity of the best/worst command
	 */
	public static void menu2(int size, int prime) {
		boolean quit = false;
		String inputLine = null;
		String[] command;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		Dictionary dict = new Dictionary(size, prime);
		SeparateChaining<Word, Frequency> lpHashTable = new SeparateChaining<Word, Frequency>(size);
		String menu = "Prac10 Dictionary Menu Options\n" + 
				"  1. Read file\n" +
				"  2. Search word\n" +
				"  3. List words\n" +
				"  4. Quit\n";

		// Command parsing loop
		try {
			while(!quit) {
				System.out.println(menu);			// Print the menu
				inputLine = console.readLine();		// Read a command
				command = inputLine.split(" ");
				if (command[0].equals("1")) {		// Read file
					String fileName;
					System.out.print("Enter the name of a file: ");
					fileName = console.readLine();
					BufferedReader file = new BufferedReader(new FileReader(fileName));
					String line;
					String[] items;
					Word newWord;
					Frequency frequency;
					try {
			            while ((line = file.readLine()) != null){	//insert all dictionary words and word frequencies into the linear probe hashtable 
			            	items = line.split(" ");
			            	//System.out.println("item: " + items[0]);
			            	if(items[0].length() > 0 && isAlpha(items[0])){
			            		newWord = dict.new Word(items[0]);
			            		int num = Integer.parseInt(items[1]);
			            		if(items[1].length() > 0 && num < 4 && num >= 0){
			            			frequency = new Frequency(num);
			            			lpHashTable.insert(newWord, frequency);
			            		} else {
			            			System.out.println("Error reading from file: " + fileName + ". Incorrect format");
			            		}
			            	} else{
			            		System.out.println("Error reading from file: " + fileName + ". Incorrect format");
			            	}
			            }
			            lpHashTable.displayCounters();
			            lpHashTable.resetCounters();
			            
			        } catch (IOException e) {
			            // if we got here, then the readLine() died
			            System.out.println("Error reading from file: " + fileName);
			        } catch (NumberFormatException e) {
			        	System.out.println("Error reading from file: " + fileName + ". Incorrect format");
			        }
				} else if (command[0].equals("2")) {	// Search word
					System.out.print("Enter a word to search the dictionary for: ");
					String input = console.readLine();
					if(isAlpha(input)){
						Frequency frequency = lpHashTable.search(dict.new Word(input.toLowerCase()));
						if(frequency == null){			//print error if not found, otherwise print frequency
							System.out.println("\"" + input + "\" was not found.");
						} else{
							System.out.println("\"" + input + "\" found. \nFrequency: " + frequency.toString());
						}
						lpHashTable.displayCounters();
						lpHashTable.resetCounters();
					}
					
				} else if (command[0].equals("3")) { // List words
					System.out.println(lpHashTable.toString());
				} else if (command[0].equals("4")) { // Quit
					quit = true;
				} else {
					System.out.println("Unrecognized command.");
				}       
			}
		} catch (IOException e) {
			System.out.println("Error reading from console");
			inputLine = "";
		}
	}
	
	/**
	 * used in menu() to check if a string contains all letters (no numbers or other characters)
	 * @param s
	 * @return
	 */
	private static boolean isAlpha(String s){
		if(s.length() == 0)
			return true;
		else if(Character.isLetter(s.charAt(0)) || s.charAt(0) == '.')
			return isAlpha(s.substring(1));
		else	//must be valid character
			return false;
	}
	
	/**
	 * @param  args command-line arguments (not used)
	 * @pre    none
	 * @post   see postconditions for individual methods in this class.
	 * @complexity O(menu())
	 */
	/*
	 * Please note that the complexities may change as you add 
	 * functionality to this class -- don't forget to update this comment 
	 * as you go.  
	 */
	public static void main(String[] args) {
		//LINEAR PROBING
//		menu(97, 53);
		//Read
		//		Collisions: 20028
		//		Probes: 1938270
		//		Number of collisions is close to (words - TABLESIZE)
		//		And as expected, probes is close to (collisions * TABLESIZE). (once the table is full, the algorithm iterates over the whole table every time there is a collision
		//Search
		//		Collisions: 1
		//		Probes: 96
		//		Word was not found as the table would have been full by the time that word was due to be added.
		//		Collision occurred when the expected position of the word did not hold the expected value.
		
//		menu(20089, 12289);
		//Read
		//		Collisions: 10149
		//		Probes: 1496935
		//Search
		//		Collisions: 1
		//		Probes: 13485
		//		Item was not found but something was at its expected position. 
		
//		menu(20089, 1);
		//Read
		//		Collisions: 19759
		//		Probes: 189655132
		//Search
		//		Collisions: 1
		//		Probes: 19381
		//		Item was not found but something was at its expected position. 
		
//		menu(20080, 2);
		//Read
		//		Collisions: 16408
		//		Probes: 46697643
		//Search
		//		Collisions: 1
		//		Probes: 3915
		//		Item was not found but something was at its expected position. 
		
//		menu(40283, 3);
		//Read
		//		Collisions: 9380
		//		Probes: 3929318
		//		Low prime led to a large amount of probes.
		//Search
		//		Collisions: 0
		//		Probes: 0
		//		No collision because the word was not found. 
		
//		menu(40283, 20089);
		//Read
		//		Collisions: 4987
		//		Probes: 5219
		//Search
		//		Collisions: 0
		//		Probes: 0
		//		No collision because the word was not found. 
		
		
		
		//SEPARATE CHAINING
//		menu2(97, 53);
		//Read
		//		Collisions: 19975
		//		Probes: 0
		//		We will never have any probes when inserting and using separate chaining. However, when something is inserted and there is 
		//		already an element in the linked list at its position, this is a collision. collisions = (inserts - TABLESIZE)
		//Search
		//		Collisions: 1
		//		Probes: 206
		//		The item was not found. This tells us there are 206 items in the linked list at the key's hashed position.
		
//		menu2(20089, 12289);
		//Read
		//		Collisions: 7413
		//		Probes: 0
		//		7413 items hashed to the same position as something else. 
		//Search
		//		Collisions: 1
		//		Probes: 0
		//		There was one item at the hashed position but it did not match the key
		
//		menu2(20089, 1);
		//Read
		//		Collisions: 18881
		//		Probes: 0
		//Search
		//		 Collisions: 1
		//		Probes: 43
		
//		menu2(20080, 2);
		//Read
		//		Collisions: 11776
		//		Probes: 0
		//Search
		//		 Collisions: 0
		//		Probes: 0
		
//		menu2(40283, 3);
		//Read
		//		Collisions: 6577
		//		Probes: 0
		//Search
		//		Collisions: 0
		//		Probes: 0
		
//		menu2(40283, 20089);
		//Read
		//		Collisions: 4198
		//		Probes: 0
		//		These primes provide a better distribution than previous combinations. Due to higher prime combined with high TABLESIZE.
		//Search
		//		Collisions: 0
		//		Probes: 0
		//		Nothing at position
		
		
		
		
	}
}
