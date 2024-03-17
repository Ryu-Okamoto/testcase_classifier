package r_okamot.testcase_classifier;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class TestCodeVisitorTest {
    
    private static void assertTestcaseProfile(
            String testcaseName,
            int numOfCalledPackages,
            int numOfCalledClasses,
            TestcaseProfile profile
    ) {
        assertEquals(testcaseName, profile.getName());
        assertEquals(numOfCalledPackages, profile.numOfCalledPackages());
        assertEquals(numOfCalledClasses, profile.numOfCalledClasses());
    }
    
    final static private String TEST_PATH = "src/test";
    
    @Test
    public void testTestMethods() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  public void test00() {}"
                + "  "
                + "  @Test"
                + "  public void test01() {}"
                + ""
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestCodeVisitor v = new TestCodeVisitor(map, TEST_PATH);
        cu.accept(v, null);
        List<TestcaseProfile> profiles = v.getTestcaseProfiles();
        assertEquals(2, profiles.size());
        assertTestcaseProfile("ATest#test00", 0, 0, profiles.get(0));
        assertTestcaseProfile("ATest#test01", 0, 0, profiles.get(1));
    }
    
    @Test
    public void testOnlyOnePackage() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  public void test00() {"
                + "    A v = new A();"
                + "  }"
                + "  "
                + "  @Test"
                + "  public void test01() {}"
                + ""
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestCodeVisitor v = new TestCodeVisitor(map, TEST_PATH);
        cu.accept(v, null);
        List<TestcaseProfile> profiles = v.getTestcaseProfiles();
        assertEquals(2, profiles.size());
        assertTestcaseProfile("ATest#test00", 0, 1, profiles.get(0));
        assertTestcaseProfile("ATest#test01", 0, 0, profiles.get(1));
    }
    
    @Test
    public void testOnlyOnePackageWithSomeClasses() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.pack", "B");
        map.add("r_okamot.pack", "C");
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  public void test00() {"
                + "    A v = new A();"
                + "    B w = new B();"
                + "    C x = new C();"
                + "  }"
                + "  "
                + "  @Test"
                + "  public void test01() {}"
                + ""
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestCodeVisitor v = new TestCodeVisitor(map, TEST_PATH);
        cu.accept(v, null);
        List<TestcaseProfile> profiles = v.getTestcaseProfiles();
        assertEquals(2, profiles.size());
        assertTestcaseProfile("ATest#test00", 0, 3, profiles.get(0));
        assertTestcaseProfile("ATest#test01", 0, 0, profiles.get(1));
    }
    
    @Test
    public void testImportOtherPackage() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.other_pack", "B");
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "import r_okamot.other_pack.B;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  public void test00() {"
                + "    A v = new A();"
                + "  }"
                + "  "
                + "  @Test"
                + "  public void test01() {"
                + "    B v = new B();"
                + "  }"
                + ""
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestCodeVisitor v = new TestCodeVisitor(map, TEST_PATH);
        cu.accept(v, null);
        List<TestcaseProfile> profiles = v.getTestcaseProfiles();
        assertEquals(2, profiles.size());
        assertTestcaseProfile("ATest#test00", 0, 1, profiles.get(0));
        assertTestcaseProfile("ATest#test01", 1, 1, profiles.get(1));
    }
    
    @Test
    public void testImportOtherPackageWithSomeClasses() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.other_pack", "B");
        map.add("r_okamot.other_pack", "C");
        map.add("r_okamot.other_pack", "D");
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "import r_okamot.other_pack.B;"
                + "import r_okamot.other_pack.C;"
                + "import r_okamot.other_pack.D;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  public void test00() {"
                + "    A v = new A();"
                + "  }"
                + "  "
                + "  @Test"
                + "  public void test01() {"
                + "    B v = new B();"
                + "    C w = new C();"
                + "    D x = new D();"
                + "  }"
                + ""
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestCodeVisitor v = new TestCodeVisitor(map, TEST_PATH);
        cu.accept(v, null);
        List<TestcaseProfile> profiles = v.getTestcaseProfiles();
        assertEquals(2, profiles.size());
        assertTestcaseProfile("ATest#test00", 0, 1, profiles.get(0));
        assertTestcaseProfile("ATest#test01", 1, 3, profiles.get(1));
    }
    
    @Test
    public void testImportOtherPackageWithWildcard() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.other_pack", "B");
        map.add("r_okamot.other_pack", "C");
        map.add("r_okamot.other_pack", "D");
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "import r_okamot.other_pack.*;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  public void test00() {"
                + "    A v = new A();"
                + "  }"
                + "  "
                + "  @Test"
                + "  public void test01() {"
                + "    B v = new B();"
                + "    C w = new C();"
                + "    D x = new D();"
                + "  }"
                + ""
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestCodeVisitor v = new TestCodeVisitor(map, TEST_PATH);
        cu.accept(v, null);
        List<TestcaseProfile> profiles = v.getTestcaseProfiles();
        assertEquals(2, profiles.size());
        assertTestcaseProfile("ATest#test00", 0, 1, profiles.get(0));
        assertTestcaseProfile("ATest#test01", 1, 3, profiles.get(1));
    }

    // for issue #1
    @Test
    public void testMethodInnerClass() {
    	PackageClassesMap map = new PackageClassesMap();
    	map.add("r_okamot.pack", "A");
    	String testCode = ""
    	        + "package r_okamot.pack;"
    	        + ""
    	        + "public class ATest {"
    	        + ""
    	        + "  @Test"
    	        + "  public void test00() {"
    	        + ""
    	        + "    class MethodInnerClass {"
    	        + "      public void hoge() {}"
    	        + "    }"
    	        + "  "
    	        + "    MethodInnerClass m = new MethodInnerClass();"
    	        + "  "
    	        + "  }"
    	        + "}"
    	        + "";
    	CompilationUnit cu = StaticJavaParser.parse(testCode);
    	TestCodeVisitor v = new TestCodeVisitor(map, TEST_PATH);
    	cu.accept(v, null);
    	List<TestcaseProfile> profiles = v.getTestcaseProfiles();
    	assertEquals(1, profiles.size());
    	assertTestcaseProfile("ATest#test00", 0, 0, profiles.get(0));
    }
    
    @Test
    public void testNonPublicMethods() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  void test00() {}"
                + "  "
                + "  @Test void test01() {}"
                + ""
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestCodeVisitor v = new TestCodeVisitor(map, TEST_PATH);
        cu.accept(v, null);
        List<TestcaseProfile> profiles = v.getTestcaseProfiles();
        assertEquals(2, profiles.size());
        assertTestcaseProfile("ATest#test00", 0, 0, profiles.get(0));
        assertTestcaseProfile("ATest#test01", 0, 0, profiles.get(1));
    }
    
    @Test
    public void testOtherAnnotatedMethods() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + ""
                + "  @RepeatedTest(5)"
                + "  void test00() {}"
                + "  "
                + "  @ParameterizedTest\n"
                + "  @ValueSource(strings = { \"racecar\", \"radar\", \"able was I ere I saw elba\" })"
                + "  void test01(String candidate) {}"
                + ""
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestCodeVisitor v = new TestCodeVisitor(map, TEST_PATH);
        cu.accept(v, null);
        List<TestcaseProfile> profiles = v.getTestcaseProfiles();
        assertEquals(2, profiles.size());
        assertTestcaseProfile("ATest#test00", 0, 0, profiles.get(0));
        assertTestcaseProfile("ATest#test01", 0, 0, profiles.get(1));
    }
}