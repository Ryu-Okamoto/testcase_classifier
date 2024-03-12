package r_okamot.testcase_classifier_istqb;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class ProductCodeVisitorTest {

    @Test
    public void testOneClass() {
        String productCode = ""
                + "package r_okamot.xxx;"
                + ""
                + "public class A {}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(productCode);
        ProductCodeVisitor v = new ProductCodeVisitor();
        cu.accept(v, null);
        assertEquals("r_okamot.xxx", v.getPackageName());
        assertEquals("A", v.getClassName());
    }
    
    @Test
    public void testSomeClasses() {
        String productCode = ""
                + "package r_okamot.xxx;"
                + ""
                + "public class A {}"
                + "class B {}"
                + "class C {}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(productCode);
        ProductCodeVisitor v = new ProductCodeVisitor();
        cu.accept(v, null);
        assertEquals("r_okamot.xxx", v.getPackageName());
        assertEquals("A", v.getClassName());
    }
    
    @Test
    public void testDeepNestedPackage() {
        String productCode = ""
                + "package r_okamot.xxx.yyy.zzz.www;"
                + ""
                + "public class A {}"
                + "";
        CompilationUnit cu = StaticJavaParser.parse(productCode);
        ProductCodeVisitor v = new ProductCodeVisitor();
        cu.accept(v, null);
        assertEquals("r_okamot.xxx.yyy.zzz.www", v.getPackageName());
        assertEquals("A", v.getClassName());
    }
}
