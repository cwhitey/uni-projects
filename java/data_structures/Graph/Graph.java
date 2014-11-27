/*
 * Author: Callum White
 * Date: 18/11/2013
 * 
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph<E>
{
	protected LinkedList<E> vertices;	// 1-d array to store the vertices
	protected LinkedList<LinkedList<E>> edges;	// 1-d array to store adjacencies between vertices,
	protected int numVertices;	
	protected int numEdges;

	// Constructor that sets aside as much capacity as specified by the user
	public Graph() {
		vertices = new LinkedList<E>();
		edges = new LinkedList<LinkedList<E>>();
	}
	
	public int numberOfVertices(){
       	return numVertices;
    }

    public int numberOfEdges() {
        return numEdges;
    }

	// Finds the location at which a vertex is stored in vertices list, and returns it's adjacent edges list. 
	// Returns null if vertex not found
    // O(|V|)
	protected LinkedList<E> getEdges(E vertex){
		Iterator<E> iter = vertices.listIterator();
		int index = 0;
		E current;
		while(iter.hasNext()){
			current = iter.next();
			if(current.equals(vertex))
				return edges.get(index);
			index++;
		}
		return null;
	}
	
	
	// Finds the location at which a vertex is stored in Vertices. 
	// Returns -1 if vertex not found
	// O(|V|)
	protected int getIndex(E vertex){
		Iterator<E> iter = vertices.listIterator();
		int index = 0;
		E current;
		while(iter.hasNext()){
			current = iter.next();
			if(current.equals(vertex))
				return index;
			index++;
		}
		return -1;
	}

	
	// Adds a new vertex
	// Only add if the vertex doesn't already exist in the graph
	// Returns true if vertex was added to graph. False if it already existed
	// O(|V|)
	public boolean addVertex(E value){
		boolean status = true;
		if(getEdges(value) != null)
			status = false;
		else {
			vertices.addLast(value);
			edges.addLast(new LinkedList<E>());
		}
		return status;
	}


	// Adds a new edge
	// O(|V|)
	public void addEdge(E vertex1, E vertex2){
		LinkedList<E> vertex1Index = getEdges(vertex1);					
		if(vertex1Index == null)					//add fails if the first vertex doesn't exist
		{
			System.out.print("addEdge failed: " + vertex1 + "does not exist.");
            return;
        }

		LinkedList<E> vertex2Index = getEdges(vertex2);
		if(vertex2Index == null)					//add fails if second vertex doesn't exist
		{
			System.out.print("addEdge failed: " + vertex2 + "does not exist.");	
			return;
        }
		
		//adding each vertex to the other's list if they are not there already
		if(!isEdge(vertex1, vertex2))
			vertex1Index.add(vertex2);
		if(!isEdge(vertex2, vertex1))
			vertex2Index.add(vertex1);


		numEdges++;
	}
	
	
	// Print the adjacency list of every vertex in the graph.
	// O(|V| * (|V| adjacent edges))
	public void printGraph(){
		Iterator<E> iter = vertices.listIterator();
		while(iter.hasNext()){
			printAdjacencyList(iter.next());
			System.out.println();
		}
	}
	
	// Prints the neighbours of the given vertex
	// O(|V| + adjacent edges)
	public void printAdjacencyList (E vertex) {
		LinkedList<E> i = getEdges(vertex);
		if(i == null) {
			System.out.print("addEdge failed: " + vertex + "does not exist.");
            return;
        }
		System.out.println(vertex + " is connected to: ");
		Iterator<E> iter = i.listIterator();
		while(iter.hasNext()){
			System.out.println("\t" + iter.next() + " ");
		}
	}

	
    // returns the degree of a vertex
	// O(1)
    public int degree(E vertex) {
        // Get the index of the vertex
        LinkedList<E> i = getEdges(vertex);
        if(i == null)
                return -1;

        // Call the other degree function that returns the degree
        // of a vertex, given its index
        return i.size();
    }

    
	// Boolean method that tells us if {v1, v2} is an edge in the graph
    // O(|V|)
	public boolean isEdge(E vertex1, E vertex2){
		LinkedList<E> i = getEdges(vertex1);
		if(i == null)
            return false;

		// if vertex2 exists in the adjacency list of
		// vertex1, then return true
		return i.contains(vertex2);
	}
	
	
	// returns the names of all the neighbours of a given vertex in a String array
	// O(|V| + adjacent edges)
	public LinkedList<E> getNeighbours(E vertex){
		LinkedList<E> source = getEdges(vertex);
		if(source == null){
			System.out.print("getNeighbours failed: Vertex " + vertex + " does not exist.");
			return null;
		}
		LinkedList<E> tempList = new LinkedList<E>();
		Iterator<E> iter = source.listIterator();
		while(iter.hasNext())
			tempList.addLast(iter.next());
		return tempList;
	}
	
	// Breadth first search function that takes a vertex index as argument; 
	// returns  a breadth first search tree
	// stored in an array of integers with the entry in slot i containing
	// the index of the parent of the vertex with index i
	// parent of source is itself; unvisited nodes have parent -1
	// BFS complexity: O(|V| + |E|)
	// My implementation complexity: O(|V|^2 + |E|), as my graph implementation is inefficient
	public void breadthFirstSearch(E source){
		// Initialize the bfsTree array; the entry -1 means
		// not yet visited.
		int[] bfsTree = new int[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)
			bfsTree[i] = -1;
		
		int sourceIndex = getIndex(source);
		// The parent of the tree root is itself
		bfsTree[sourceIndex] = sourceIndex;

		// Then initialize the visited array
		boolean[] visited = new boolean[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)
			visited[i] = false;

		visited[sourceIndex] = true;

		// Then initialize the queue. We will only use offer(E) and pollFirst(): E, so it acts like a queue.
		LinkedList<E> Q = new LinkedList<E>();
		Q.offer(source);

		while(!Q.isEmpty())		//while Q is not empty
		{
			// get the index of the vertex first in line
			E current = Q.pollFirst();

			// Get the indices of the neighbours of the current vertex
			LinkedList<E> neighbours = getNeighbours(current);

			Iterator<E> iterNeighbours = neighbours.listIterator();
			E currentNeighbour;
			int currentNeighbourIndex;
			// Scan the neighbours
			while(iterNeighbours.hasNext()){
				// Get index of current neighbour
				currentNeighbour = iterNeighbours.next();
				currentNeighbourIndex = getIndex(currentNeighbour);
				
				// Check if the neighbor is new, i.e., not visited
				// If so, mark the neighbor as visited, enqueue the neighbor, and 
				// set the neighbor's parent in bfsTree
				if(!visited[currentNeighbourIndex]){
					visited[currentNeighbourIndex] = true;
					Q.offer(currentNeighbour);
					bfsTree[currentNeighbourIndex] = getIndex(current);
				}
			}
		}
		for(int x=0; x<bfsTree.length; x++){
			if(bfsTree[x] > -1)
				System.out.println("Vertex: " + getValue(x) + ". Parent: " + getValue(bfsTree[x]));
		}
	}	
	
	
	// Find all friendship groups in the graph and print them out
	// Friendship group algorithm complexity O(|V| + |E|)
	// My implementation complexity: O(|V|^2 + |E|), as my graph implementation is inefficient
	public void findFriendshipGroups(E source){
		//number of the current friendship group being calculated
		int friendshipGroupNum = 1;
		
		// Initialize the friendshipGroup array; the entry -1 means
		// it's not yet in the current friendship group being calculated.
		int[] friendshipGroup = new int[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)
			friendshipGroup[i] = -1;
		
		int sourceIndex = getIndex(source);
		// The parent of the tree root is itself
		friendshipGroup[sourceIndex] = sourceIndex;

		// Then initialize the visited array
		boolean[] visited = new boolean[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)
			visited[i] = false;

		visited[sourceIndex] = true;
		boolean allVisited = false;

		// Then initialize the queue. We will only use offer(E) and pollFirst(): E, so it acts like a queue.
		LinkedList<E> Q = new LinkedList<E>();
		Q.offer(source);
		
		while(!allVisited){
			if(friendshipGroupNum != 1){
				for(int i = 0; i < vertices.size(); i++)
					friendshipGroup[i] = -1;
			}
			while(!Q.isEmpty()){		//while Q is not empty
				// get the index of the vertex first in line
				E current = Q.pollFirst();
	
				// Get the indices of the neighbours of the current vertex
				LinkedList<E> neighbours = getNeighbours(current);
	
				Iterator<E> iterNeighbours = neighbours.listIterator();
				E currentNeighbour;
				int currentNeighbourIndex;
				// Scan the neighbours
				while(iterNeighbours.hasNext()){
					// Get index of current neighbour in the vertices linked list
					currentNeighbour = iterNeighbours.next();
					currentNeighbourIndex = getIndex(currentNeighbour);
					
					// Check if the neighbor is new, i.e., not visited
					// If so, mark the neighbor as visited, enqueue the neighbor, and 
					// set the neighbor's parent in bfsTree
					if(!visited[currentNeighbourIndex]){
						visited[currentNeighbourIndex] = true;
						Q.offer(currentNeighbour);
						friendshipGroup[currentNeighbourIndex] = getIndex(current);
					}
				}
			}
			System.out.println("Vertices in frienship group " + friendshipGroupNum + ": ");
			for(int x=0; x<friendshipGroup.length; x++){
				if(friendshipGroup[x] > -1)
					System.out.println("\t" + getValue(x));
			}
			//check for an unvisited node. if found, add it to the queue. this is the beginning of a new friendship group
			boolean foundUnvisited = false;
			int x=0;
			while(!foundUnvisited && x<vertices.size()){
				if(!visited[x]){
					Q.offer(getValue(x));
					foundUnvisited = true;
				}
				x++;
			}
			//if every node has been visited, we need to finish
			if(!foundUnvisited)
				allVisited = true;
			//increment the current friendship group number (so the next calculated friendship group has a different number)
			friendshipGroupNum++;
		}
	}
	
	
	// Search the tree starting at v1, looking for v2. Distances from v1 will be updated for every node visited.
	// The shortest distance between v1 and v2 is then printed.
	public void findDistanceBetween(E v1, E v2){
		// Initialise the array that will hold distances for each vertex in the graph during our BFS search
		int[] distances = new int[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)
			distances[i] = -1;
		
		int sourceIndex = getIndex(v1);
		// The distance for our starter vertex is 0
		distances[sourceIndex] = 0;

		// Then initialize the queue. We will only use offer(E) and pollFirst(): E, so it acts like a queue.
		LinkedList<E> Q = new LinkedList<E>();
		Q.offer(v1);
		
		int currentIndex;
		while(!Q.isEmpty()){		//while Q is not empty
			// get the index of the vertex first in line
			E current = Q.pollFirst();
			currentIndex = getIndex(current);
			// Get the indices of the neighbours of the current vertex
			LinkedList<E> neighbours = getNeighbours(current);

			Iterator<E> iterNeighbours = neighbours.listIterator();
			E currentNeighbour;
			int currentNeighbourIndex;
			// Scan the neighbours
			while(iterNeighbours.hasNext()){
				// Get index of current neighbour in the vertices linked list
				currentNeighbour = iterNeighbours.next();
				currentNeighbourIndex = getIndex(currentNeighbour);
				
				// Check if the neighbor is new, i.e., not visited
				// If so, mark the neighbor as visited, enqueue the neighbor, and 
				// set the neighbor's parent in bfsTree
				if(distances[currentNeighbourIndex] < 0){
					distances[currentNeighbourIndex] = distances[currentIndex] + 1;
					Q.offer(currentNeighbour);
				}
			}
		}
		if(distances[getIndex(v2)] < 0)
			System.out.println("A path between " + v1 + " and " + v2 + " does not exist.");
		else
			System.out.println("The distance from " + v1 + " to " + v2 + " is " + distances[getIndex(v2)]);
	}
	
	
	// Get a vertex when given an index of the vertices array
	protected E getValue(int index){
		return vertices.get(index);
	}
	
	public void isBipartite(){
		// Initialize the bfsTree array; the entry -1 means
		// not yet visited.
		int[] bfsTree = new int[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)
			bfsTree[i] = -1;
		
		// Start the breadth first search at the first element
		bfsTree[0] = 0;

		// Initialize the array which keeps track of the colour of each node
		// Let's say: 0 = blue, 1 = red;
		int[] colours = new int[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)
			colours[i] = -1;
		
		// Holds the current colour
		int currentColour = 0;
		// Set starting element to blue
		colours[0] = currentColour;
		//currentColour = toggleColour(currentColour);
		
		boolean notBipartite = false;
		
		// Then initialize the queue. We will only use offer(E) and pollFirst(): E, so it acts like a queue.
		LinkedList<E> Q = new LinkedList<E>();
		Q.offer(getValue(0));
		
		while(!Q.isEmpty())		//while Q is not empty
		{
			// get the index of the vertex first in line
			E current = Q.pollFirst();

			// Get the indices of the neighbours of the current vertex
			LinkedList<E> neighbours = getNeighbours(current);
			//currentColour = toggleColour(currentColour);
			currentColour = colours[getIndex(current)];
			Iterator<E> iterNeighbours = neighbours.listIterator();
			E currentNeighbour;
			int currentNeighbourIndex;
			// Scan the neighbours
			while(iterNeighbours.hasNext()){
				// Get index of current neighbour
				currentNeighbour = iterNeighbours.next();
				currentNeighbourIndex = getIndex(currentNeighbour);
				// Check if the neighbor is new, i.e., not visited
				// If so, mark the neighbor as visited, enqueue the neighbor, and 
				// set the neighbor's parent in bfsTree
				if(colours[currentNeighbourIndex] == -1){
					System.out.println("Setting " + currentNeighbour + " to " + toggleColour(currentColour));
					colours[currentNeighbourIndex] = toggleColour(currentColour);
					Q.offer(currentNeighbour);
				} else{
					if(colours[currentNeighbourIndex] == currentColour){	//if true, graph is NOT BIPARTITE
						System.out.println("Colour clash: " + currentNeighbour + " is already set to " + colours[currentNeighbourIndex]);
						notBipartite = true;
					}
				}
			}
		}
		for(int x=0; x<colours.length; x++){
			String colour;
			if(colours[x] > -1){
				if(colours[x] == 0)
					colour = "blue";
				else
					colour = "red";
				System.out.println("Vertex: " + getValue(x) + ". Colour: " + colour);
			}
		}
		if(notBipartite)
			System.out.println("NOT BIPARTITE");
		else
			System.out.println("Graph is bipartite");
	}
	
	private int toggleColour(int currentColour){
		if(currentColour == 0)
			return 1;
		else
			return 0;
	}
	
	public void populateGraphFromFile(String fileName){
		String nextLine, newVertex, newAdjVertex;
		String[] nextLineArray, adjVertices;
		boolean endOfFile = false;
		try{
			BufferedReader file = new BufferedReader(new FileReader(fileName));
			while(!endOfFile){													//while there are still more lines to be read...
				nextLine = file.readLine();
				if(nextLine == null){
					endOfFile = true;
				} else{
					nextLineArray = nextLine.split(":");
					newVertex = nextLineArray[0].trim();						//create new vertex 
					addVertex((E)newVertex);									//add new vertex to graph
					adjVertices = nextLineArray[1].trim().split(",");			//all joint vertices, comma separated are in the second element
					if(adjVertices[0].compareTo("") != 0){
						for(int x=0; x<adjVertices.length; x++){				//iterate over all joint vertices
							newAdjVertex = adjVertices[x].trim();
							addVertex((E)newAdjVertex);							//add to graph if not there already
							addEdge((E)newVertex, (E)newAdjVertex);				//join vertices
						}
					}
				}
			}
			//graph is now intact and complete
			file.close();
		} catch(FileNotFoundException e){
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException: " + e.getMessage());
		} catch(NumberFormatException e){
			System.out.println("NumberFormatException: "+ e.getMessage());
		}
	}
	
	//testing
	public static void main(String[] args){
		Graph<String> graph = new Graph<String>();
		
		
		//populate graph from information in a file
		graph.populateGraphFromFile("Q1.txt");
		
//		graph.printGraph();
		
//		Question 1
		System.out.println("Checking if graph is bipartite...");
		graph.isBipartite();
		
	}
}