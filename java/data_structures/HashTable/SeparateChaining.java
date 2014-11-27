/*****************************************************************************
 MONASH UNIVERSITY, Faculty of Information Technology, Clayton School of IT.
 Student Declaration for FIT1008 Submission. I Callum White, ID:24571520
 declare that this submission is my own work and has not been copied from any
 other source without attribution. I acknowledge that severe penalties exist
 for any copying of code without attribution, including a fail mark for this prac.
 *****************************************************************************/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class SeparateChaining<K extends Hashable,D> extends HashTable<K, D> {  
		protected LinkedList<Entry>[] hashTable;
		
		
		public SeparateChaining(int size){
			hashTable = new LinkedList[size];	//would not let me go LinkedList<Entry>[size] for some silly reason
		}
		
		/**
		 * @complexity Best and worst: O(hash + add) - this is the complexity of adding something to the linked list
		 */
		public boolean insert(K key, D data){	//MUST CHECK FOR DUPLICATES
			int position = key.hash();
			if(hashTable[position] == null){
				hashTable[position] = new LinkedList<Entry>();
			} else
				incrementCollisionCounter();	//there is already something in the list so we can consider this a collision
			hashTable[position].add(new Entry(key, data));
			//if we want to replace the data of a matching key in the list (if exists)
//			if(hashTable[position] == null){
//				hashTable[position] = new LinkedList<Entry>();
//			}
//			ListIterator<Entry> i = linkedList.listIterator(0);
//			boolean alreadyExists = false;
//			while(i.hasNext() && !alreadyExists){		//loop until end of list or until key is found
//				if(i.next().getKey().equals(key)){
//					alreadyExists = true;
//					i.set(new Entry(key, data));
//				}
//			}
//			if(!alreadyExists){
//				i.add(new Entry(key, data));
//			}
			return true; 				//how can this ever be false? we simply add it to the linked list corresponding with the hash position
		}
		
		/**
		 * @complexity Best and worst case: O(hash + i) - where i is the length of the linked list at the hashed position
		 */
		public D search(K key){
			D data = null;
			int position = key.hash();
			if(hashTable[position] != null){
				LinkedList<Entry> linkedList = hashTable[position];
				ListIterator<Entry> i = linkedList.listIterator(0);
				Entry entry;
				if(i.hasNext()){			//check if it's the first element in the linked list
					entry = i.next();
					if(entry.getKey().equals(key))
						data = entry.getData();
					else {					//if it's not the first element in the list, we can consider this a collision and probe the rest of the list
						incrementCollisionCounter();
						while(i.hasNext()){		//traverse entire linked list comparing keys and remember the data of the key that was equal.
							entry = i.next();
							incrementProbeCounter();
							if(entry.getKey().equals(key))
								data = entry.getData();
						}
					}
				}
			}
			return data;
		}
		
		/**
		 * @complexity Best and worst case: O(n * i), where n is the number of elements in the hashtable, 
		 * and i is the number of elements stored in each linked list of each of n's elements. Therefore, n*i is the total number of entries stored in the table.
		 */
		public String toString(){
			String result = "";
			for(int x=0; x < hashTable.length; x++){
				if(!(hashTable[x] == null)){
					ListIterator<Entry> i = hashTable[x].listIterator();
					while(i.hasNext())
						result += "\n" + i.next().toString();
				}
			}
			return result;
		}
}
