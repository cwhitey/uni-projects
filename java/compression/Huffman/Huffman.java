/*
 * Author: Callum White
 * Date: 18/11/2013
 * 
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * A class that can be used to create a huffman code from a file. It also prints out the size of the file before and after encoding.
 */
public class Huffman {
	ArrayList<Node> nodes = new ArrayList<Node>();
	int sizeBeforeEncode, sizeAfterEncode;
	
	protected class Node{
		String character;
		int freq;
		Node left, right;
		
		public Node(String character, int freq){
			this.character = character;
			this.freq = freq;
			this.left = null;
			this.right = null;
		}
		
		public Node(String character, int freq, Node left, Node right){
			this.character = character;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}
		
		public void incFreq(){
			this.freq++;
		}
		
		public int compareTo(Node node){
			if(this.freq < node.freq)
				return -1;
			else if(this.freq > node.freq)
				return 1;
			else
				return 0;
		}
	}
	
	public class CustomComparator implements Comparator<Node> {
	    @Override
	    public int compare(Node o1, Node o2) {
	        return o1.compareTo(o2);
	    }
	}
	
	public void parseFile(String fileName){
		String nextLine;
		boolean endOfFile = false;
		String character; 
		int charCount = 0;
		try{
			BufferedReader file = new BufferedReader(new FileReader(fileName));
			while(!endOfFile){			//while there are still more lines to be read...
				nextLine = file.readLine();
				if(nextLine == null){
					endOfFile = true;
				} else{
					for(int x=0; x<nextLine.length(); x++){
						charCount++;
						character = Character.toString(nextLine.charAt(x));
						if(nodes.size() == 0){
							nodes.add(new Node(character, 1));
							System.out.println("added: " + character);
						}
						else{
							Iterator<Node> iter = nodes.iterator();
							Node current;
							boolean found = false;
							while(iter.hasNext() && !found){
								current = iter.next();
								if(current.character.compareTo(character) == 0){
									current.incFreq();
									found = true;
								}
							}
							if(!found){
								nodes.add(new Node(character, 1));
								System.out.println("Added: " + character);
							}
						}
					}
					
					
				}
			}
			//file has now been parsed and our list of nodes is complete
			
			sizeBeforeEncode = charCount * 8;
			file.close();
		} catch(FileNotFoundException e){
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException: " + e.getMessage());
		} catch(NumberFormatException e){
			System.out.println("NumberFormatException: "+ e.getMessage());
		}
	}
	
	public void encode(){
		Node node1, node2, root;
		if(nodes.size() > 0){
			while(nodes.size() > 1){
				Collections.sort(nodes, new CustomComparator());
				//remove and get the nodes with the two smallest frequencies
				node1 = nodes.remove(0);
				node2 = nodes.remove(0);
				nodes.add(new Node("", node1.freq + node2.freq, node1, node2));
				System.out.println("Encode: Added a new node. Size: " + nodes.size());
			}
			root = nodes.get(0);
			printEncodings(root);
			System.out.println("Size before encoding: " + sizeBeforeEncode);
			System.out.println("Size after encoding: " + sizeAfterEncode);
		} else
			System.out.println("Nothing to encode.");
	}
	
	private void printEncodings(Node root){
		printEncodings(root.left, "0");
		printEncodings(root.right, "1");
	}
	
	private void printEncodings(Node node, String encoding){
		if(isLeaf(node)){
			System.out.println(node.character + ": " + encoding);
			sizeAfterEncode += encoding.length() * node.freq;
		}
		else{
			if(node.left != null)
				printEncodings(node.left, encoding + "0");
			if(node.right != null)
				printEncodings(node.right, encoding + "1");
		}
				
	}
	
	public boolean isLeaf(Node node){
		return (node.left==null && node.right==null);
	}
	
	public static void main(String[] args) {
		Huffman huffman = new Huffman();
		huffman.parseFile("Q3b.txt");
		huffman.encode();

	}

}
