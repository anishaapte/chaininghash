import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.NoSuchElementException;

/**
 * This class extends the HashtableMap class to run submission checks on it.
 */
public class P25SubmissionChecker extends HashtableMap<Integer, Integer> {

  /**
   * Checks if hashtable resizes as expected.
   */
  @Test
  public void testTableResize() {
    HashtableMap<String, String> map = new HashtableMap<>(8);
    map.put("a", "1");
    map.put("b", "2");
    map.put("c", "3");
    map.put("d", "4");
    map.put("e", "5");

    // check capacity before resizing
    Assertions.assertEquals(8, map.getCapacity());   
 
    map.put("f", "6");
    map.put("g", "7");
    map.put("h", "8");
    map.put("i", "9");

    // check capacity after resizing
    Assertions.assertEquals(16, map.getCapacity());
  }

  /**
   * Checks if hashtable resizes when it reaches the load factor threshold.
   */
  @Test
  public void testTableResizeAtThreshold() {
    HashtableMap<String, String> map = new HashtableMap<>(12);
    map.put("1", "a");
    map.put("2", "b");
    map.put("3", "c");
    map.put("4", "d");
    map.put("5", "e");
    map.put("6", "f");
    map.put("7", "g");
    map.put("8", "h");
    
    // check capacity before resizing
    Assertions.assertEquals(12, map.getCapacity());   
    
    map.put("9", "i");
    map.put("10", "j");

    // check capacity after resizing
    Assertions.assertEquals(24, map.getCapacity());   
    
  }

  /**
   * Tests putting duplicate "a" keys into the HashtableMap and checks for IllegalArgumentExceptions
   * after the first insertion. Then checkes if calling get("a") returns the first value inserted.
   */
  @Test
  public void testDuplicateInsertions() {
    HashtableMap<String, Integer> map = new HashtableMap<>();
    map.put("a", 6);
    Assertions.assertThrows(
           IllegalArgumentException.class,
           () -> map.put("a", 7)
    );
    Assertions.assertThrows(
           IllegalArgumentException.class,
           () -> map.put("a", 8)
    );
    Assertions.assertEquals(6, map.get("a"));
  }
 
  /**
   * Tests checks the difference between adding a unique and duplicate key into the
   * HashtableMap by calling put.
   */ 
  @Test
  public void testMultipleDuplicateInsertions() {
    HashtableMap<String, Integer> map = new HashtableMap<>();
    map.put("a", 6);
    Assertions.assertThrows(
           IllegalArgumentException.class,
           () -> map.put("a", 7)
    );
    map.put("b", 5);
    Assertions.assertThrows(
           IllegalArgumentException.class,
           () -> map.put("a", 8)
    );
    Assertions.assertThrows(
           IllegalArgumentException.class,
           () -> map.put("b", 5)
    );
  }

  /**
   * Tests the get method on a small HashtableMap with keys 1, 2, and 3.
   */
  @Test
  public void testGetOnSmallMap123() {
    HashtableMap<Integer, String> map = new HashtableMap<>();
    map.put(1, "one");
    map.put(2, "two");
    map.put(3, "three");
    Assertions.assertEquals("two", map.get(2));
  }
  
  /**
   * Tests whether the get method throws a NoSuchElementException if key
   * is not in table.
   */
  @Test
  public void testGetException() {
    HashtableMap<Integer, String> map = new HashtableMap<>();
    map.put(6, "one");
    map.put(8, "two");
    map.put(9, "three");
    Assertions.assertThrows(
           NoSuchElementException.class,
           () -> map.get(7)
    );
  }

  /**
   * Tests get(13) and get(23) on a HashtableMap(10).
   */
  @Test
  public void testSimpleCollision() {
    HashtableMap<Integer, String> map = new HashtableMap<>(10);
    map.put(12, "one");
    map.put(13, "two");
    map.put(23, "three");
    Assertions.assertEquals("two", map.get(13));
    Assertions.assertEquals("three", map.get(23));
  }
  
  /**
   * Tests if get(27) on a HashtableMap(10) with keys: 17, 28, 77
   * throws a NoSuchElementException.
   */
  @Test
  public void testCollisionException() {
    HashtableMap<Integer, String> map = new HashtableMap<>(10);
    map.put(17, "one");
    map.put(28, "two");
    map.put(77, "three");
    Assertions.assertThrows(
           NoSuchElementException.class,
           () -> map.get(27)
    );
  }

  /**
   * Tests get for a key that has been removed.
   */
  @Test
  public void testGetAfterRemove() {
    HashtableMap<Integer, Integer> map = new HashtableMap<>();
    map.put(2, 6);
    map.put(3, 7);
    map.put(4, 8);
    map.get(3);
    map.remove(3);

    Assertions.assertThrows(
           NoSuchElementException.class,
           () -> map.get(3)
    );
  }

  /**
   * Tests removal of a key that is not contained in the HashtableMap.
   */
  @Test
  public void testRemoveThrowsException() {
    HashtableMap<Integer, Integer> map = new HashtableMap<>(10);
    map.put(2, 0);
    map.put(22, 0);
    map.put(32, 1);
    map.put(42, 1);

    Assertions.assertThrows(
           NoSuchElementException.class,
           () -> map.remove(12)
    );
  }

}
