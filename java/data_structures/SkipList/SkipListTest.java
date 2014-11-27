import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class SkipListTest {
	public static void main(String args[]){
		SkipList<Integer> skipList = new SkipList<Integer>();
		String input;
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter integers to insert, space seperated: ");
		try{
			input = keyboard.readLine();
			String[] words = input.split(" ");
			int nextInt = 0;
			for(int x=0; x<words.length; x++){
				nextInt = Integer.parseInt(words[x]);
				System.out.println(nextInt);
				//skipList.printList();
				skipList.insert(nextInt);
			}
		} catch(IOException e){
			System.out.println(e);
		}
		
		skipList.search(10);
		
	}
}
