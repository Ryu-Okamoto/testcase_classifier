package r_okamot.testcase_classifier;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

public class PackageClassesMapTest {
    
    @Test
    public void testAdd() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("pack", "A");
        assertTrue(map.contains("pack", "A"));
    }
    
    @Test
    public void testAddAsSet() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("pack", "A");
        Set<String> added = new HashSet<String>();
        added.add("B");
        added.add("C");
        map.add("pack", added);
        Set<String> expected = new HashSet<String>();
        expected.add("A");
        expected.add("B");
        expected.add("C");
        assertIterableEquals(expected, map.get("pack"));
    }
    
    @Test
    public void testFindFalse() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("pack", "A");
        assertFalse(map.contains("pack", "B"));
        assertFalse(map.contains("kcap", "A"));
    }
    
    @Test
    public void testAddWithSameKey() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("pack", "A");
        map.add("pack", "B");
        map.add("pack", "C");
        Set<String> expected = new HashSet<String>();
        expected.add("A");
        expected.add("B");
        expected.add("C");
        assertIterableEquals(expected, map.get("pack"));
    }
    
    @Test
    public void testAddWithDifferentKey() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("xxx", "A");
        map.add("yyy", "B");
        assertTrue(map.contains("xxx", "A"));
        assertTrue(map.contains("yyy", "B"));
    }
    
    @Test
    public void testCopy() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("xxx", "A");
        map.add("yyy", "B");
        PackageClassesMap newMap = map.copy();
        assertTrue(newMap.contains("xxx", "A"));
        assertTrue(newMap.contains("yyy", "B"));
        map.add("xxx", "C");
        assertTrue(map.contains("xxx", "C"));
        assertFalse(newMap.contains("xxx", "C"));
    }
}
