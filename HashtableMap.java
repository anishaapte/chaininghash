// --== CS400 Fall 2023 File Header Information ==--
// Name: Anisha Apte
// Email: aaapte@wisc.edu
// Group: E04
// TA: Lakshika Rathi
// Lecturer: Gary Dahl
// Notes to Grader: No optional notes

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class implements the MapADT in inserting,removing,getting,clearing the keys associated with a value into a 
 * HashtableMap. If the HashtableMap has a load factor that is greater than or equal to 75%, then the capacity of the 
 * HashtableMap will double. 
 * @author anishaapte
 *
 * @param <KeyType> - generic type of the key
 * @param <ValueType> - generic type of the value associated with the key 
 */
public class HashtableMap<KeyType,ValueType> implements MapADT<KeyType, ValueType> {
    
    //creating the linked list of pairs 
    protected LinkedList<Pair>[] table;
    public ArrayList<Pair> pairs = null;
    
    /**
     * This class contains and creates the pair constructor 
     * @author anishaapte
     *
     */
    protected class Pair {

        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
            
        }
        public String toString() {
            return key + "-" + value;
        }
    }
    
    /**
     * Constructor for the HashtableMap. Creating a HashtableMap with a defined capacity
     * @param capacity - given capacity of the array, inputed by user
     */
    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity) {
        this.table = (LinkedList<Pair>[])new LinkedList[capacity];
        this.pairs = (ArrayList<Pair>)new ArrayList<Pair>(capacity);
        
    }
    /**
     * Constructor for the HashtableMap. Creating a HashtableMap with no capacity.
     * Default capacity is set to 32 
     */
    public HashtableMap() { // with default capacity = 32
        this(5);
    }
    
    /**
     * Adds a new key,value pair/mapping to this collection.
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException if key is null
     */
    @SuppressWarnings("unchecked")
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        //throwing exceptions if key is null or already contained in array
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (this.containsKey(key)) {
            throw new IllegalArgumentException("key is already  stored " + key);
        }
        
        //getting the index at which key is going to be placed in array and creating a new Pair out of the key-value
        int index = Math.abs(key.hashCode()) % this.getCapacity();
        Pair p = new Pair(key,value);
        
        //if the index of the array is null, then creating a new LinkedList at that index
        if (this.table[index] == null) {
            LinkedList<Pair> list = new LinkedList<Pair>();
            this.table[index] = list;
        }
        //adding the new pair at the index in the array
        //should happen after if statement, because this step should happen regardless
        this.table[index].add(p);
        
        //rehashing if needed
        double loadFactor = this.getLoadFactor();
        if (loadFactor >= 0.75) {
            //saving off the original array, before the capacity gets expanded
            LinkedList<Pair>[] origTable = this.table;
            //changing the current array to have a capacity 2 times the original capacity
            this.table = (LinkedList<Pair>[]) new LinkedList[this.getCapacity() * 2];
            //getting each element from the original array and inserting it into the new double capacity array
            for (int i = 0; i < origTable.length; i++) {
                if (origTable[i] != null) {
                    //getting the elements of the linkedlist within the index
                    for (int j = 0; j < origTable[i].size(); j++) {
                        p = origTable[i].get(j);
                        
                        //reinserting into the new table                       
                        index = Math.abs(p.key.hashCode()) % this.getCapacity();
                        if (this.table[index] == null) {
                            LinkedList<Pair> list = new LinkedList<Pair>();
                            this.table[index] = list;
                        }
                        this.table[index].add(p);
                        
                    }
                }
            }
            
        }
        //linear probe code
//        if (pairs.get(index) == null) {
//            pairs.set(index, p);
//        }
//        else {
            while(pairs.get(index) != null) {
                index++;
                index = index % this.getCapacity();
                
            }
            pairs.set(index, p);
//        }
    }
    /**
     * Helper method to get the load factor of the given array
     * @return load factor of the array - size / capacity
     */
    private double getLoadFactor() {
        //getting count using size method 
        int count = this.getSize();
        double loadFactor = 0.0;
        //finding load factor - count / capacity
        loadFactor = ((double)count) /((double)this.getCapacity());
        
        return loadFactor;        
    }
    
    /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    @Override
    public boolean containsKey(KeyType key) {
        //checking the linked list within each index of the table array to check for key
        for (int i = 0; i < this.table.length; i++) {  
            //continuing if the element at index i is null
            if (this.table[i] == null) {
                continue;
            }
            //checking each element of the linkedlist at the index
            for (int j = 0; j < this.table[i].size(); j++) {
                //key found, returning true
                if(this.table[i].get(j).key.equals(key)) {
                    return true;
                }              
            }
        }
        return false;
    }
    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        //throwing exception if key is not stored in collection
        if (!this.containsKey(key)) {
            throw new NoSuchElementException("key to get is not in the array");
        }
        //if element at the index of the array is null, then continuing 
        for (int i = 0; i < this.table.length; i++) {  
            if (this.table[i] == null) {
                continue;
            }
            //checking each element of the linkedlist within the index of the array and 
            //returning the value associated with the key
            for (int j = 0; j < this.table[i].size(); j++) {
                if(this.table[i].get(j).key.equals(key)) {
                    return this.table[i].get(j).value;
                }              
            }
        }
        return null;
    }
    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        //throwing exception if key is not stored in collection 
        if (!this.containsKey(key)) {
            throw new NoSuchElementException("key to remove is not in the array");
        }
        //if element at the index of the array is null, then continuing 
        for (int i = 0; i < this.table.length; i++) {  
            if (this.table[i] == null) {
                continue;
            }
            //checking each element of the linkedlist within the index of the array and 
            //removing that element and returning the value to remove
            for (int j = 0; j < this.table[i].size(); j++) {
                if(this.table[i].get(j).key.equals(key)) {
                    ValueType removeVal = this.table[i].get(j).value;
                    this.table[i].remove(j);
                    return removeVal;
                }              
            }
        }
        
        return null;
    }
    /**
     * Removes all key,value pairs from this collection.
     */
    @Override
    public void clear() {
        //using a for loop to iterate through all of the elements in the collection
        //if the linkedlist at the index position is not null, then using the clear method
        //on the linkedlist class to remove all of the elements from the linkedlist
        for (int i = 0; i < this.table.length; i++) {
            if (this.table[i] != null) {
                this.table[i].clear();
            }
        }
        
    }
    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    @Override
    public int getSize() {
        int size = 0;
        //iterating through each element of the array. If the linkedlist at that index is not null and  
        //the size iss greater than zero, then am counting all of the elements within that linked list and
        //am adding it to the size variable. Will do this for each element of the array
        for (int i = 0; i < this.table.length; i++) {
            if (this.table[i] != null && this.table[i].size() > 0) {
                //count++;
                size+= this.table[i].size();
            }
        }
        return size;
    }
    /**
     * Retrieves this collection's capacity.
     * @return the size of the underlying array for this collection
     */
    @Override
    public int getCapacity() {
        //returning the length of the array, which is the capacity
        return this.table.length;
    }
    
    /**
     * This method will check the size and capacity of the put() method when the 
     * loadfactor of the Hashtable map is below 75%
     */
    @Test
    public void testValidPut() {
        //creating the hashtable map, and inputting into the 2 collisions. However, 
        //rehashing will not be required
        HashtableMap<String,String> map = new HashtableMap<String,String>(10);
        map.put("austin", "texas");
        map.put("albany", "new york");
        map.put("madison", "wisconsin");
        map.put("boston", "mass");
        map.put("66", "number");
        map.put("11", "hello");



        //checking the size and capacity of the Hashtable map
        Assertions.assertEquals(10, map.getCapacity(), "checking capacity");
        Assertions.assertEquals(6, map.getSize(), "checking size");

    }
    
    /**
     * Testing the put() method by inserting a duplicate, an IllegalArgumentException
     * should be thrown
     */
    @Test
    public void testInValidPut() {
        HashtableMap<String,String> map = new  HashtableMap<String,String>();
        //creating the map with a duplicate 
        try {
            map.put("austin", "texas");
            map.put("albany", "new york");
            map.put("albany", "new york");
            
            //code should not come to this point, as exception should be thrown
            Assertions.assertTrue(false, "should get exception, but didn't");
            
        }
        catch(IllegalArgumentException e) {
            //asserting that the code should throw an exception, and make it into the catch block
            Assertions.assertTrue(true, "exception as expected");
        }

     
    }
    /**
     * This method will test the size and capacity of the put() method when 
     * doubling the capacity is required. That is when the load factor is greater than 
     * or equal to 75% 
     */
    @Test
    public void testRehashing() {
        //creating a map that has a load factor less than 75%
        HashtableMap<String,String> map = new HashtableMap<String,String>(4);
        map.put("austin", "texas");
        map.put("albany", "new york");
        //checking the capacity, making sure it is still 4
        Assertions.assertEquals(4, map.getCapacity(), "checking capacity before rehash");
        //inserting a collision, where load factor will now become 75%, map will double
        //in capacity
        map.put("long island", "new york");
        //checking the capacity of the new Hashtable map
        Assertions.assertEquals(8, map.getCapacity(), "checking capacity after rehash");
    }
    
    /**
     * This method will test the remove() method with a value that is inside of the map
     * and a value that is not in the map. The value that is not in the map should throw a
     * NoSuchElementException
     */
    @Test
    public void testRemove() {
        //creating the map
        HashtableMap<String,String> map = new HashtableMap<String,String>();
        map.put("austin", "texas");
        map.put("albany", "new york");
        map.put("madison", "wisconsin");
        
        //saving the value in a string when the remove() method is called on "albany"
        String value = map.remove("albany");
        
        //checking that the value is what it should be
        Assertions.assertEquals("new york", value);
        //also checking that the size of the map has decremented by 1 after remove method has been called
        Assertions.assertEquals(2, map.getSize(), "checking size after remove");
        
        //trying the removal of a value not in the map
        try {
            map.remove("sacramento");
            //asserting that this part is not reached as an exception should be thrown
            Assertions.assertTrue(false, "should get exception, but didn't");

        }
        catch(NoSuchElementException e) {
            //asserting that this part should be reached as exception should be thrown
            Assertions.assertTrue(true, "exception as expected");
            //also checking that the size of the map has remained the same
            Assertions.assertEquals(2, map.getSize(), "checking size after remove - remains same");

        }
    }
    
    /**
     * This method will test the containsKey() and get() methods. The method will try
     * invalid and valid keys
     */
    @Test
    public void testContainsKeyAndGet() {
        //creating the map
        HashtableMap<String,String> map = new HashtableMap<String,String>();
        map.put("austin", "texas");
        map.put("albany", "new york");
        map.put("madison", "wisconsin");
        
        //checking that the containsKey() method is returning true for a valid key
        Assertions.assertTrue(map.containsKey("austin"), "checking valid contains");
        //checking that the containsKey() method returns false for an invalid key
        Assertions.assertFalse(map.containsKey("sacramento"), "checking invalid contains");
        //checking that the get() method returns the correct value for a valid key
        Assertions.assertEquals("texas", map.get("austin"), "checking valid get");
        
        //checking that the get() method throws an exception when a key that is not in the map
        //has been inputed
        try {
            map.get("sacramento");
        }
        catch (NoSuchElementException e) {
            Assertions.assertTrue(true, "exception as expected");
        }

        
    }
    public static void main(String[] args) {
        HashtableMap<Integer,String> map = new HashtableMap<Integer,String>();
        map.put(3, "texas");
        map.put(4, "new york");
        map.put(4, "wisconsin");
        
        System.out.println(map.pairs);
    }
    
    
}
