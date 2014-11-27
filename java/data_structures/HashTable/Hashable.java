/**
 * Force the class to implement a mode hash function (that mods by 
 * tablesize and uses prime as coefficient)
 * 
 * @author Dhananjay Thiruvady
 * @modified Dhananjay Thiruvady (May 2011)
 * @modified Brendon Taylor (Oct 2012 - Comments)
 * @since April 2007
 */

public interface Hashable {
    public int hash(); 
}

