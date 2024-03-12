package r_okamot.testcase_classifier_istqb;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class TestCodeVisitorTest {
    
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
        TestCodeVisitor v = new TestCodeVisitor(map);
        cu.accept(v, null);
        assertEquals("ATest", v.getTestClassName());
        assertIterableEquals(Arrays.asList("test00", "test01"), v.getTestMethodNames());
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
        TestCodeVisitor v = new TestCodeVisitor(map);
        cu.accept(v, null);
        assertEquals(0, v.getNumOfImportedPackages());
        assertEquals(1, v.getNumOfCalledUserClasses());
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
        TestCodeVisitor v = new TestCodeVisitor(map);
        cu.accept(v, null);
        assertEquals(0, v.getNumOfImportedPackages());
        assertEquals(3, v.getNumOfCalledUserClasses());
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
        TestCodeVisitor v = new TestCodeVisitor(map);
        cu.accept(v, null);
        assertEquals(1, v.getNumOfImportedPackages());
        assertEquals(2, v.getNumOfCalledUserClasses());
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
        TestCodeVisitor v = new TestCodeVisitor(map);
        cu.accept(v, null);
        assertEquals(1, v.getNumOfImportedPackages());
        assertEquals(4, v.getNumOfCalledUserClasses());
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
        TestCodeVisitor v = new TestCodeVisitor(map);
        cu.accept(v, null);
        assertEquals(1, v.getNumOfImportedPackages());
        assertEquals(4, v.getNumOfCalledUserClasses());
    }
}