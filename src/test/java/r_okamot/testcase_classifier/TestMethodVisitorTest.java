package r_okamot.testcase_classifier;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class TestMethodVisitorTest {
    
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
    
    @Test
    public void testSimpleMethod() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        Set<String> importedPackages = new HashSet<String>();
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  public void test00() {}"
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestMethodVisitor v = new TestMethodVisitor(map, "r_okamot.pack", "ATest", importedPackages);
        cu.accept(v, null);
        TestcaseProfile profile = v.makeProfile();
        assertTestcaseProfile("ATest#test00", 0, 0, profile);
    }
    
    @Test
    public void testMethodCallingAClass() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        Set<String> importedPackages = new HashSet<String>();
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  public void test00() {"
                + "    A v = new A();"
                + "  }"
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestMethodVisitor v = new TestMethodVisitor(map, "r_okamot.pack", "ATest", importedPackages);
        cu.accept(v, null);
        TestcaseProfile profile = v.makeProfile();
        assertTestcaseProfile("ATest#test00", 0, 1, profile);
    }
    
    @Test
    public void testMethodCallingSomeClass() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.pack", "B");
        Set<String> importedPackages = new HashSet<String>();
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  public void test00() {"
                + "    A v = new A();"
                + "    B w = new B();"
                + "  }"
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestMethodVisitor v = new TestMethodVisitor(map, "r_okamot.pack", "ATest", importedPackages);
        cu.accept(v, null);
        TestcaseProfile profile = v.makeProfile();
        assertTestcaseProfile("ATest#test00", 0, 2, profile);
    }
    
    @Test
    public void testMethodCallingSomePackages() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.pack_other", "B");
        Set<String> importedPackages = new HashSet<String>();
        importedPackages.add("r_okamot.pack_other");
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "import r_okamot.pack_other.B;"
                + ""
                + "public class ATest {"
                + ""
                + "  @Test"
                + "  public void test00() {"
                + "    A v = new A();"
                + "    B w = new B();"
                + "  }"
                + "}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestMethodVisitor v = new TestMethodVisitor(map, "r_okamot.pack", "ATest", importedPackages);
        cu.accept(v, null);
        TestcaseProfile profile = v.makeProfile();
        assertTestcaseProfile("ATest#test00", 1, 2, profile);
    }
}