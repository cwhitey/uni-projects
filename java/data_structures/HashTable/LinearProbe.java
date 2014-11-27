/*****************************************************************************
 MONASH UNIVERSITY, Faculty of Information Technology, Clayton School of IT.
 Student Declaration for FIT1008 Submission. I Callum White, ID:24571520
 declare that this submission is my own work and has not been copied from any
 other source without attribution. I acknowledge that severe penalties exist
 for any copying of code without attribution, including a fail mark for this prac.
 *****************************************************************************/

import java.util.ArrayList;

/**
 * Generic Hash Table data type implemented using linear probe techniques
 * 
 * @author Maria Garcia de la Banda
 * 
 * @modified May 2011
 * @modified Brendon Taylor (Oct 2012 - Implements HashTable)
 * @since April 2007
 * @input none
 * @output none
 * @errorHandling none
 * @knownBugs none
 */

public class LinearProbe<K extends Hashable,D> extends HashTable<K, D> {  
	protected Entry[] hashTable;	//we have use Entry as a type because we are extending HashTable. It is implemented as a protected inner class in HashTable.
	
	/**
	 * Creates a new object of the class: empty array of the given size
	 *
	 * @param       size of the table
	 * @pre         size is positive
	 * @post        an empty array of the given size
	 * @complexity  best and worst case: O(1) (multiplied by the 
	 *              complexity of "new")
	 */
	public LinearProbe(int size) {
		hashTable = new HashTable.Entry[size];
	}                                                                            

	/**
	 * @post if it returns false, hashTable is full and left unchanged. If
	 *       it returns true, hashTable has a new element, and all cells from
	 *       "position" to where Entry(key,data) is finally added to, are full
	 * @complexity Best case: when the first position tried is empty (i.e., 
	 *       the length of the probe is 1). Then, O(1)+A where A is the 
	 *       complexity of hash(), which is usually O(M) where M is the max
	 *       size of the key, which is assume to be a small constant
	 * @complexity Worst case: when the table is full. Then, O(TABLESIZE)+A, 
	 *       since the length of the probe is equal to the size of the table.
	 *       (Average is O(1) if properly constructed)
	 *       
	 * @see HashTable#insert(Hashable, Object)
	 */
	@Override	
	public boolean insert(K key, D data) {
		int position = key.hash();
		if(hashTable[position] != null){
			incrementCollisionCounter();				//increments when a key's hash position is already occupied
			position = (position+1)%hashTable.length;	//could just go position++ but oh well
		}
		for (int count = 1; count < hashTable.length; count++) {	//start at 1 because we've already checked 1 position
			if (hashTable[position] == null) { // empty cell
				hashTable[position] = new Entry(key,data);
				return true;
			} else { // still looking
				incrementProbeCounter();		//increments every iteration after a collision occurs
				position = (position+1)%hashTable.length;
			}
		}
		return false;
	}

	/**
	 * @complexity Best case: when D is in the first position (i.e., the length
	 *      of the probe is 1): A+B where A is the complexity of hash(), which
	 *      is usually  O(M) (where M is the max size of the elements in the 
	 *      key), B is the complexity of equals() which is usually O(N) where 
	 *      N is the max size of the data (note that the complexity of getKey 
	 *      and getData() is O(1))
	 * @complexity Worst case: when the table is full and the element is not 
            there: A+O(TABLESIZE)*B+, since the length of the probe is equal to 
	 *      the size of the table. Again, average is O(1) if properly 
	 *      constructed.
	 *      
	 * @see HashTable#search(Hashable)
	 */
	@Override
	public D search(K key) {
		int position = key.hash();
		if(hashTable[position] != null){
			if(!key.equals(hashTable[position].getKey())){
				incrementCollisionCounter();				//increments when a key's hash position is already occupied
				position = (position+1)%hashTable.length;	//could just go position++ but oh well
			}
		}
		for (int count = 1; count < hashTable.length; count++) {	//start at 1 because we've already checked 1 position
			if (hashTable[position] == null) {
				return null;
			} else if (key.equals(hashTable[position].getKey())) {
				return hashTable[position].getData();
			} else {
				incrementProbeCounter();		//increments every iteration after a collision occurs
				position = (position + 1) % hashTable.length;
			}
		}
		
		return null;
	}
	
	/**
	 * @complexity Best and worst cases are the same: O(size)*A where 
	 *      A is the complexity of the toString method for Entry 
	 * 
	 * @see HashTable#toString()
	 */
	@Override	
	public String toString() {
		String table = "";
		
		for (int i = 0; i < hashTable.length; i++) {
			if (hashTable[i] != null)
				table += hashTable[i] + "\n";
		}
		
		return table;
	}		
}

