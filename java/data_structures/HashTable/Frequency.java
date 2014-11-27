/**
 * Represents the stores the average frequency of a word in our Dictionary
 * 
 * @author Dhananjay Thiruvady
 * @modified Brendon Taylor (Oct 2012 - Comments)
 * @since May 2011
 */
public class Frequency {
    public enum Freq {RARE, UNCOMMON, COMMON, UNKNOWN};
    public Freq value;

    /**
     * @param value The numeric value of our frequency
     */
    public Frequency(int value) {
		switch (value) {
		    case 1: this.value = Freq.RARE;
		    		break;
		    case 2: this.value = Freq.UNCOMMON;
		    		break;
		    case 3: this.value = Freq.COMMON;
		    		break;
		    case 0: this.value = Freq.UNKNOWN;
		    		break;
		    default: System.out.println("Unrecognised frequency");
			   		this.value = Freq.UNKNOWN;
		    		break;
		}
    }

    /**
     * @return Converts our enumerated frequency to a number
     */
    public int getFrequency(){
		switch (value) {
    	    case RARE: 		return 1;
		    case UNCOMMON: 	return 2;
		    case COMMON: 	return 3;
		    case UNKNOWN: 	return 0;
	  	    default: 		System.out.println("ERROR"); // should never execute
	  	    				return -7;
		}
    }

    /**
     * @return A String representation of our frequency
     */
    public String toString(){
		switch (value) {
    	    case RARE: 		return "Rare";
		    case UNCOMMON: 	return "Uncommon";
		    case COMMON:	return "Common";
		    case UNKNOWN:	return "Unknown";
	  	    default: 		System.out.println("ERROR"); // should never execute
	  	    				return "ERROR";
		}
    }
}
