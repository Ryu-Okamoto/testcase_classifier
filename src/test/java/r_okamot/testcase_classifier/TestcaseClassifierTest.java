package r_okamot.testcase_classifier;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.util.List;

public class TestcaseClassifierTest {
    
    private static TestcaseProfile makeUniqueProfile(
            PackageClassesMap map,
            String testPath,
            String testCode
    ) 
    {
        CompilationUnit cu = StaticJavaParser.parse(testCode);
        TestCodeVisitor v = new TestCodeVisitor(map, testPath);
        cu.accept(v, null);
        List<TestcaseProfile> profiles = v.getTestcaseProfiles();
        assertEquals(1, profiles.size());
        return profiles.get(0);
    }
    
    @Test
    public void testTrivialUnit() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        String testPath = "src/test/ATest.java";
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + "  @Test"
                + "  public void test00() {}"
                + "}"
                + "";
        TestcaseProfile profile = makeUniqueProfile(map, testPath, testCode);
        assertTrue(TestcaseClassifier.judgeUnitOnISTQB(profile));
        assertTrue(TestcaseClassifier.judgeUnitOnIEEE(profile));
        assertTrue(TestcaseClassifier.judgeUnitOnDEV(map, profile));
    }
    
    @Test
    public void testUnit_ISTQB_IEEE_DEV() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        String testPath = "src/test/ATest.java";
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + "  @Test"
                + "  public void test00() {"
                + "    A a = new A();"
                + "  }"
                + "}"
                + "";
        TestcaseProfile profile = makeUniqueProfile(map, testPath, testCode);
        assertTrue(TestcaseClassifier.judgeUnitOnISTQB(profile));
        assertTrue(TestcaseClassifier.judgeUnitOnIEEE(profile));
        assertTrue(TestcaseClassifier.judgeUnitOnDEV(map, profile));
    }
    
    @Test
    public void testUnit_IEEE_DEV() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.pack", "B");
        String testPath = "src/test/ATest.java";
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + "  @Test"
                + "  public void test00() {"
                + "    A a = new A();"
                + "    B b = new B();"
                + "  }"
                + "}"
                + "";
        TestcaseProfile profile = makeUniqueProfile(map, testPath, testCode);
        assertFalse(TestcaseClassifier.judgeUnitOnISTQB(profile));
        assertTrue(TestcaseClassifier.judgeUnitOnIEEE(profile));
        assertTrue(TestcaseClassifier.judgeUnitOnDEV(map, profile));
    }
    
    @Test
    public void testUnit_IEEE() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.pack", "B");
        String testPath = "src/test/integration/ATest.java";
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class ATest {"
                + "  @Test"
                + "  public void test00() {"
                + "    A a = new A();"
                + "    B b = new B();"
                + "  }"
                + "}"
                + "";
        TestcaseProfile profile = makeUniqueProfile(map, testPath, testCode);
        assertFalse(TestcaseClassifier.judgeUnitOnISTQB(profile));
        assertTrue(TestcaseClassifier.judgeUnitOnIEEE(profile));
        assertFalse(TestcaseClassifier.judgeUnitOnDEV(map, profile));
    }
    
    @Test
    public void testUnit_DEV() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.pack.sub", "B");
        String testPath = "src/test/ATest.java";
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "import r_okamot.pack.sub.B;"
                + ""
                + "public class ATest {"
                + "  @Test"
                + "  public void test00() {"
                + "    A a = new A();"
                + "    B b = new B();"
                + "  }"
                + "}"
                + "";
        TestcaseProfile profile = makeUniqueProfile(map, testPath, testCode);
        assertFalse(TestcaseClassifier.judgeUnitOnISTQB(profile));
        assertFalse(TestcaseClassifier.judgeUnitOnIEEE(profile));
        assertTrue(TestcaseClassifier.judgeUnitOnDEV(map, profile));
    }
    
    @Test
    public void testIntegration_DEV() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.pack.sub", "B");
        String testPath = "src/test/HogeTest.java";
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "public class HogeTest {"
                + "  @Test"
                + "  public void test00() {"
                + "    A a = new A();"
                + "  }"
                + "}"
                + "";
        TestcaseProfile profile = makeUniqueProfile(map, testPath, testCode);
        assertTrue(TestcaseClassifier.judgeUnitOnISTQB(profile));
        assertTrue(TestcaseClassifier.judgeUnitOnIEEE(profile));
        assertFalse(TestcaseClassifier.judgeUnitOnDEV(map, profile));
    }
    
    @Test
    public void testTrivialIntegration() {
        PackageClassesMap map = new PackageClassesMap();
        map.add("r_okamot.pack", "A");
        map.add("r_okamot.pack.sub", "B");
        String testPath = "src/test/integration/ATest.java";
        String testCode = ""
                + "package r_okamot.pack;"
                + ""
                + "import r_okamot.pack.sub.B;"
                + ""
                + "public class ATest {"
                + "  @Test"
                + "  public void test00() {"
                + "    A a = new A();"
                + "    B b = new B();"
                + "  }"
                + "}"
                + "";
        TestcaseProfile profile = makeUniqueProfile(map, testPath, testCode);
        assertFalse(TestcaseClassifier.judgeUnitOnISTQB(profile));
        assertFalse(TestcaseClassifier.judgeUnitOnIEEE(profile));
        assertFalse(TestcaseClassifier.judgeUnitOnDEV(map, profile));
    }    
}
