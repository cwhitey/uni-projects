import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


public class Question4 {
	
	public static class Node {
		public Node left, right;
		public int data;
		
		public Node(int data){
			left = null;
			right = null;
			this.data = data;
		}
	}
	
	public static void main(String[] args) {
		//Question 4a
//		Node bst;
//		int[] array = readIntoArrayFromFile("textFile");
//		bst = insertArray(array); //can be tested with text file named 'textFile'
//		//Question 4b
//		searchPrint(10, bst);
		//searchPrint(20, bst);
		//searchPrint(70, bst);
		//searchPrint(100, bst);
		
		//Question 4c & 4d
		for(int i=0; i<5; i++){
			heightRandomBST(8);
		}
	}
	
	//Question 4a
	//Takes an array and inserts all elements into a BST in order, and returns it.
	public static Node insertArray(int[] array){
		Node root = new Node(array[0]);
		for(int i = 1; i < array.length; i++){
			System.out.println(array[i]);
			insertIntoBST(root, array[i]);
		}
		return root;
	}
	
	//Insert a value into a BST. Any duplicate values won't be added. Does not re-shuffle like an AVL.
	//Complexity: O(log n)
	public static void insertIntoBST(Node node, int value){
		if(node == null){
			System.out.println("Root node cannot be null.");
		} else if(value < node.data){
			if(node.left == null){
				node.left = new Node(value);
			} else {
				insertIntoBST(node.left, value);
			}
		} else if(value > node.data){
			if(node.right == null){
				node.right = new Node(value);
			} else {
				insertIntoBST(node.right, value);
			}
		} else {		//if value already exists in tree
			System.out.println("Value already exists in tree.");
		}
	}
	
	//Takes in a fileName and returns a list of all tab-separated integers in the file.
	//Complexity: O(n), where n is the number of integers in the file
	public static int[] readIntoArrayFromFile(String fileName){
		BufferedReader inputReader;
		String[] stringInts = null;
		int[] ints = null;
		try {
			inputReader = new BufferedReader(new FileReader(fileName));
			String line = inputReader.readLine();	//read the file contents
			stringInts = line.split("\t");			//split on all tabs
			ints = new int[stringInts.length];
			inputReader.close();
			for(int i = 0; i < ints.length; i++)	//convert all string integers to ints
				ints[i] = Integer.parseInt(stringInts[i]);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e){
			e.printStackTrace();
		}
		return ints;
	}
	
	//Question 4b
	//Search for a given integer, k, starting at node T, and print out the path to it.
	//We have an aux part to the recursive call because we don't want to print an array for the first value
	//Complexity: O(log n)
	public static void searchPrint(int k, Node root){
		if(root == null){
			System.out.println("The input number was not found in the BST");
			return;
		} else if(k < root.data){	//less than root, go left
			searchPrintAux(k, root.left, String.valueOf(root.data));
		} else if(k > root.data){	//more than root, go right
			searchPrintAux(k, root.right, String.valueOf(root.data));
		} else {					//integer matches root, print it
			System.out.println(root.data);
		}
	}
	
	public static void searchPrintAux(int k, Node T, String s){
		if(T == null){
			System.out.println("The input number was not found in the BST");
		} else if(k < T.data){		//less than, so go left
			searchPrintAux(k, T.left, s + " -> " + T.data);
		} else if(k > T.data){		//more than, so go right
			searchPrintAux(k, T.right, s + " -> " + T.data);
		} else {					//must be equal to current node, so print path
			System.out.println(s + " -> " + T.data);
		}
	}
	
	//Question 4c
	//Randomly permute integer positions of a given array
	//Complexity: O(n)
	public static int[] permuteArray(int[] array){
		Random r = new Random();
		int j, temp;
		for(int i=0; i<array.length; i++){
			j = r.nextInt(array.length - i) + i;	//randomly generate a number in the range of [i, n-1]
			
			//swap integers are indexes i and j
			temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
		return array;
	}
	
	//Question 4d
	//Takes an integer, n, as input, creates an array of integers 1 -> N, randomly permutes the position 
	//of integers in the array, and returns the height of the BST created from inserting integers from the array
	//Complexity: O(4n) = O(n)
	public static void heightRandomBST(int N){
		//create an array of integers in the range of [1, N]
		int[] intArray = new int[N];
		for(int i = 0; i < N; i++){
			intArray[i] = i+1;
		}
		
		int[] randomArray = permuteArray(intArray);				//jumble up the integer positions
		Node root = new Node(randomArray[0]);					//assign root the first value in the random array
		for(int i = 1; i < N; i++){
			insertIntoBST(root, randomArray[i]);
		}
		System.out.println(getBSTHeight(root));
		
	}
	
	//Returns the depth of a given tree
	//Complexity: O(n) (has to visit every node)
	public static int getBSTHeight(Node root){
		if(root == null){
			System.out.println("Tree is empty.");
			return -1;
		} else {
			return getBSTHeight(root, 0);
		}
	}
	
	public static int getBSTHeight(Node node, int depth){
		if(node == null){
			return depth;
		} else {
			depth += 1;
			int left = getBSTHeight(node.left, depth);
			int right = getBSTHeight(node.right, depth);
			if(left >= right){
				return left;
			} else {
				return right;
			}
		}
	}
	
	
}
