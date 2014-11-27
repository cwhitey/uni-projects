import java.util.ArrayList;
import java.util.Random;


public class SkipList<K extends Comparable<K>> {
	private Random rand = new Random();
	//static final int MAX_HEIGHT = 10;
	//private ArrayList<Node> root = new ArrayList<Node>(1); //root is simply a list of Node pointers instead of an actual Node
	private int currentHeight = 0;				//if only one level, height = 0
	private Node headBottom = new Node(null);
	private Node headTop = headBottom;			//we will start here when searching/inserting
	
	protected class Node{
		protected K key;
		protected Node left, right, up, down;

		
		protected Node(K newKey){
			key = newKey;
			this.left = null;
			this.right = null;
			this.up = null;
			this.down = null;
		}
		
		protected Node(K newKey, Node left, Node right, Node up, Node down){
			key = newKey;
			this.left = left;
			this.right = right;
			this.up = up;
			this.down = down;
		}
		
	}
	
	/**
	 * Search for a key in the skip list
	 * Complexity: O(log n)
	 */
	public void search(K key){
		if(isEmpty())
			System.out.println("No");
		else{
			searchAux(key, headTop);	//call auxiliary function with the last element in the root list(top-left node essentially)
		}
	}
	
	public void searchAux(K key, Node node){
		if(node.key == null){
			System.out.println("Head");
			searchAux(key, node.right);
		} else if(node.key.compareTo(key) == 0){		//if key matches, print
			System.out.println(node.key);
			System.out.println("Yes");
		} else if(node.down == null){					//must be on the bottom level
			while(node.key.compareTo(key) > 0){			//tower can't possibly be less than key at this point
				System.out.println(node.key);			//we need to keep checking to the left until we find it or hit a node that is less than the key
				node = node.left;
				if(node.key.compareTo(key) == 0){		//if equal
					System.out.println(node.key);
					System.out.println("Yes");
				} else if(node.key.compareTo(key) < 0 || node == null){	//if node is less than key
					System.out.println(node.key);
					System.out.println("No");
				}
			}
		} else if(node.key.compareTo(key) < 0){	//if tower < key
			System.out.println(node.key);
			searchAux(key, node.right);					//to avoid missing certain towers, we don't go down when the tower is less than
		} else if(node.key.compareTo(key) > 0){	//if tower > key
			System.out.println(node.key);
			searchAux(key, node.down.left);
		} else if(node.right == null){					//if there is nothing to the right of the tower, loop until you find something
			searchAux(key, node.down);
		} else{
			System.out.println("HOW DID WE GET HERE");
		}
	}
	
	
	//public K remove(K key){
		
	//}
	
	public void insert(K key){
		Node newNode;
		if(isEmpty()){		//if skip list currently empty
			System.out.println("Is empty!!!");
			newNode = new Node(key, headBottom, null, null, null);
			headBottom.right = newNode;
		} else {
			newNode = insertAux(key, headTop);
		}
		Node origNewNode = newNode; //just making a copy for later
		//determine height of tower for inserted key
		int newHeight = getNewTowerHeight();
		System.out.println("new height: " + newHeight);
		System.out.println("current height: " + currentHeight);
		for(int x=0; x<newHeight; x++){			//-1 because we already have a base node created for the tower
			newNode.up = new Node(key, null, null, null, newNode);
			newNode = newNode.up;
			if((x+1) > currentHeight){				//x+1 is the current level we're on. if it's more than the current height, we want to extend headTop
				headTop.up = new Node(null, null, newNode, null, headTop);
				headTop = headTop.up;
				//if(headBottom.up == null){			//for when this is the first time adding a new level (joining headBottom and headTop)
				//	headBottom.up = headTop.down;
				//}
			}
		}
		if(newHeight>currentHeight)
			currentHeight = newHeight;
		linkUpNewTower(origNewNode);								//go through the list and link up all object references for the new tower.
	}
	
	public Node insertAux(K key, Node node){
		Node retNode = new Node(null);
		if(node != null){
			if(node.key == null){
				if(node.right != null){
					retNode = insertAux(key, node.right);
				} else{
					System.out.println("insertAux: node.key is null and node.right is a null node.");
					//retNode = new Node(key, node, null, null, null); 
				};
			} else if(node.key.compareTo(key) == 0){		//if key matches, return node as it is a duplicate of the key they are adding
				retNode = node;
			} else if(node.down == null){					//must be on the bottom level
				while(node.key.compareTo(key) > 0){			//if more than key
					if(node.key.compareTo(key) == 0){		//if equal, return node as it is a duplicate of the key they are adding
						retNode = node;
					} else if(node.key.compareTo(key) < 0 || node == null){	//if node is less than key
						Node newNode = new Node(key, node, node.right, null, null);
						node.right.left = newNode;
						node.right = newNode;
						retNode = newNode;
					} else{
						System.out.println("HOW DID WE GET HERE");
						retNode = new Node(null);
					}
					node = node.left;
				}
				while(node.key.compareTo(key) < 0){			//if less than key
					if(node.key.compareTo(key) == 0){		//if equal, return node as it is a duplicate of the key they are adding
						retNode = node;
					} else if(node.key.compareTo(key) > 0 || node == null || node.right == null){	//if node is less than key
						Node newNode = new Node(key, node, node.left, null, null);
						node.left.right = newNode;
						node.left = newNode;
						retNode = newNode;
					} else{
						System.out.println("HOW DID WE GET HERE");
						retNode = new Node(null);
					}
					node = node.right;						
				}
			} else if(node.key.compareTo(key) < 0){			//if tower < key
				if(node.right != null){
					retNode = insertAux(key, node.right);	//to avoid missing certain towers, we don't go down when the tower is less than
				} else {
					retNode = insertAux(key, node.down.left);
				}
			} else if(node.key.compareTo(key) > 0){			//if tower > key
				retNode = insertAux(key, node.down.left);
			} else if(node.right == null){					//if there is nothing to the right of the tower, loop until you find something
				retNode = insertAux(key, node.down);
			} else{
				System.out.println("HOW DID WE GET HERE");
				retNode = new Node(null);
			}
		} else{System.out.println("insertAux: given a null node.");};
		return retNode;
	}
	
	/**
	 * Precondition: list cannot be empty (an item would have already been inserted at this point)
	 * Precondition: must be passed a node on the bottom level of the list
	 */
	public void linkUpNewTower(Node node){
		Node currentHead = headBottom;
		Node current = currentHead;
		if(node.key == null)
			System.out.println("linkUpNewTower: node's key is null.");
		while(node.up != null){					//while not at the top of the tower
			if(current.right.key.compareTo(node.key) == 0){
				currentHead = currentHead.up;	//move up a level
				current = currentHead;
			} else if(current.right.key.compareTo(node.key) > 0){
				current.right.left = node;		//update references
				node.right = current.right;
				current.right = node;
				node.left = current;
				currentHead = currentHead.up;	//move up a level
				current = currentHead;
			} else {
				current = current.right;		//move along one, ready for next check
			}
			node = node.up;						//move up a level in our tower
		}
	}
	
	public boolean newLayer(){
		return rand.nextBoolean();
	}
	
	public int getNewTowerHeight(){
		boolean keepGoing = true;
        int counter = 0;
        while(keepGoing){
        	keepGoing = newLayer();
        	if(keepGoing)
        		counter++;
        }
        return counter;
	}
	
	public boolean isEmpty(){
		return headBottom.right == null;
	}
	
	public void printList(){
		Node current = headBottom.right;
		if(current!=null){
			while(!(current.down == null && current.right == null)){//retarded
				System.out.print(current.key); System.out.print("\t");
				if(current.up == null){
					System.out.println("\n");
				}
			}
		}
	}
}
