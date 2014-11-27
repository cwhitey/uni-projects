/*****************************************************************************
 MONASH UNIVERSITY, Faculty of Information Technology, Clayton School of IT.
 Student Declaration for FIT1008 Submission. I Callum White, ID:24571520
 declare that this submission is my own work and has not been copied from any
 other source without attribution. I acknowledge that severe penalties exist
 for any copying of code without attribution, including a fail mark for this prac.
 *****************************************************************************/

/** 
 * Represents a generic hash table
 * 
 * @author Brendon Taylor
 * @since Oct 2012
 * @version 1.0
 */
public abstract class HashTable<K extends Hashable, D> {
	private int collisions = 0, probes = 0;
	/*
	 * Linear Probing Invariants:
	 * 
	 * if an Entry is stored in position N, then
	 *     N is either the hash value of the Entry's key K, or the first empty 
	 *     position from the hash value when Entry was inserted. 
	 * (corollary of the above, really): there are no empty spaces between items 
	 *     with the same hash value.
	 */
	protected class Entry {  
		K key;
		D data;

		protected Entry(K newKey, D newData) {
			key = newKey; 
			data = newData;
		} 

		protected K getKey() {
			return key;
		}   

		protected D getData() {
			return data;
		}

		public String toString() {
			return key + "::"  + data;
		}
	}
	
	/**
	 * Inserts a new element into the hash table. It returns true if the item 
	 * is inserted in the hash table, false if there are no empty slots
	 *
	 * @pre  the element does not already appear in the table
	 */	
	public abstract boolean insert(K key, D data);

	
	/**
	 * Searches for the data associated to a particular key (if any).
	 * It returns D if it can find the item, null if it cannot
	 *
	 * @post If it returns null, there is no cell in hashTable with 
	 *       the chosen key, if it returns D, then a cell with that key exists, 
	 *       and has D as data. Also, hashTable is unchanged.
	 */	
	public abstract D search(K key);
	
	/**
	 * Used to return all elements in the hashTable, each in a line
	 *
	 * @return a String representation of this hashTable
	 * @post the hashTable is unchanged
	 */
	public abstract String toString();
	
	public void incrementCollisionCounter(){
		collisions++;
	}
	
	public void incrementProbeCounter(){
		probes++;
	}
	
	public void resetCounters(){
		collisions = 0;
		probes = 0;
	}
	
	public void displayCounters(){
		System.out.println("Collisions: " + collisions);
		System.out.println("Probes: " + probes);
	}
}
