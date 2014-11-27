/**
 * 
 * @author callum
 *
 * @param <K> data type
 */
public class SplayTree<K extends Comparable<K>>{
	private Node root;
	
	
	protected class Node{
		protected K key;
		protected Node left;
		protected Node right;
		protected Node parent;
		
		protected Node(K newKey, Node l, Node r, Node parent){
            key = newKey;
            left = l;
            right = r;
            this.parent = parent;
        }
		
		protected Node(K newKey, Node parent){		//when we want to initialise a Node that has a parent but no children (probably the most common case).
			key = newKey;
            left = null;
            right = null;
            this.parent = parent;
		}
		
		protected Node(K newKey){
			key = newKey;
            left = null;
            right = null;
            parent = null;
        }
	}	
	
	public SplayTree(){
		
	}
	
	//initialise tree with specified root
	public SplayTree(K rootKey){
		root = new Node(rootKey);
	}
	
	
	/**
	 * Insert a key/element into the tree like a normal BST and then splay this new node.
	 */
	public void insert(K key){
		if(root == null){
			root = new Node(key);
		}
		else{
			insertAux(key, root);
		}
	}
	
	//auxiliary recursive method belonging to insert()
	public void insertAux(K thisKey, Node current){
		
	}
	
	/**
	 * Looks for a key “k” and writes down the path from the
	 * root to the “k” on the screen, if the key “k” exist in the tree. Otherwise, it should write down the path,
	 * which is followed from the root to a leaf node of the tree while the search walks down the tree.
	 * Then the last node we found is splayed (even if it's just the node that ended our search).
	 */
	public void searchPrint(K key){
		if(this.root == null){
			System.out.println("The input number was not found in the BST");
		} else {
			int comparison = key.compareTo(root.key);
			if(comparison < 0)		//less than root, go left
				if(root.left != null)
					searchPrintAux(key, this.root.left, String.valueOf(root.key));
			else if(comparison > 0)	//more than root, go right
				if(root.right != null)
					searchPrintAux(key, this.root.right, String.valueOf(root.key));
			else 					//integer matches root, print it
				System.out.println(root.key);
		}
	}
	
	public void searchPrintAux(K key, Node node, String s){
		int comparison = key.compareTo(node.key);
		if(comparison < 0){			//less than, so go left
			if(node.left != null)
				searchPrintAux(key, node.left, s + " -> " + node.key);
			else{
				splay(node);
				System.out.println("The input number was not found in the tree.");
			}
		}
	 	else if(comparison > 0)		//more than, so go right
	 		if(node.right != null)
	 			searchPrintAux(key, node.right, s + " -> " + node.key);
	 		else{
	 			splay(node);
	 			System.out.println("The input number was not found in the tree.");
	 		}
		else						//must be equal to current node, so print path
			System.out.println(s + " -> " + node.key);
	}
	
	/**
	 * Searches for an element in the tree. Returns the last key checked if the specified key isn't found.
	 */
	public Node searchNearest(K key){
		if(this.root == null){
			return this.root;
		} else{
			return searchNearestAux(key, this.root);
		}
	}
	
	public Node searchNearestAux(K key, Node node){
		int comparison = key.compareTo(node.key);
		if(comparison < 0){			//less than, so go left
			if(node.left != null)
				return searchNearestAux(key, node.left);
			else{
				splay(node);
				return node;
			}
		}
	 	else if(comparison > 0)		//more than, so go right
	 		if(node.right != null)
	 			return searchNearestAux(key, node.right);
	 		else{
	 			splay(node);
	 			return node;
	 		}
		else{						//must be equal to current node
			 splay(node);
			 return node;
		}
	}
	
	/**
	 * Remove a key/element from the tree, follow normal BST procedures, and then splay the parent of the node that replaced the deleted node.
	 */
	public void remove(K key){
		splayKey(key);
		//....
	}
	
	public void remove(K key, Node current){
		
	}
	
	/**
	 * Splays the specified node.
	 */
	public Node splay(Node node){
		Node temp;
		if(node == root){
			return root;
		} else if(node.parent == root){		//root is the parent of the node
			return singleRotation(node);	//perform a single rotation on the node.
		//} else if(){
			
		}
	}
	
	/**
	 * Searches for the node containing the key, and then splays it.
	 */
	public void splayKey(K key){
		searchNearest(key);
	}
	
	/**
	 * Perform the zig-zig operation on three nodes.
	 */
	public Node zig_zig(Node parent, Node child, Node grandchild){
		int comparison = parent.key.compareTo(child.key);
		Node grandparent = parent.parent;
		if(comparison < 0){			//must be ( \ )
			parent.right = child.left;		//change parent's left node
			parent.right.parent = parent;
			child.right = grandchild.left;	//change child's left node
			child.right.parent = child;
			grandchild.left = child;		//change grandchild's right node
			child.parent = grandchild;
			child.left = parent;			//change child's right node
			parent.parent = child;
		} else if(comparison > 0){	//must be ( / )
			parent.left = child.right;		//change parent's left node
			parent.left.parent = parent;
			child.left = grandchild.right;	//change child's left node
			child.left.parent = child;
			grandchild.right = child;		//change grandchild's right node
			child.parent = grandchild;
			child.right = parent;			//change child's right node
			parent.parent = child;
		}
		grandchild.parent = grandparent;
	}
	
	/**
	 * Perform the zig-zig operation on three nodes.
	 */
	public Node zig_zag(Node parent, Node child, Node grandchild){
		int comparison = parent.key.compareTo(child.key);
		Node grandparent = parent.parent;
		if(comparison < 0){				//must be right-left ( > )
			child.left = grandchild.right;
			child.left.parent = child;
			parent.right = grandchild.left;
			parent.right.parent = parent;
			grandchild.left = parent;
			grandchild.left.parent = grandchild;
			grandchild.right = child;
			grandchild.right.parent = grandchild;
		} else if(comparison > 0) {		//must be left-right ( < )
			child.right = grandchild.left;
			child.right.parent = child;
			parent.left = grandchild.right;
			parent.left.parent = parent;
			grandchild.right = parent;
			grandchild.right.parent = grandchild;
			grandchild.left = child;
			grandchild.left.parent = grandchild;
		}
		grandchild.parent  = grandparent;
		return grandchild;
	}
	
	/**
	 * Perform a single rotation on the node. We can assume the node has a parent.
	 */
	public Node singleRotation(Node node){
		Node parent = node.parent;
		Node grandparent = parent.parent;
		if(node.parent.left == node){	//node is less than root
			parent.left = node.right;
			parent.left.parent = parent;
			node.right = parent;
			parent.parent = node;
		} else{							//node is more than root
			parent.right = node.left;
			parent.right.parent = parent;
			node.left = parent;
			parent.parent = node;
		}
		node.parent = grandparent;
		return node;
	}
	
	
}
