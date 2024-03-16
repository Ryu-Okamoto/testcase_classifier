package r_okamot.testcase_classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class TestCodeVisitor extends VoidVisitorAdapter<Void> {
    private PackageClassesMap map = null;
    private String packageName = "";
    private Set<String> importedPackages = new HashSet<String>();
    private Stack<String> classNameNest = new Stack<String>();
    private List<TestcaseProfile> profiles = new ArrayList<TestcaseProfile>();

    public TestCodeVisitor(PackageClassesMap map) {
        this.map = map.copy();
    }
    
    public List<TestcaseProfile> getTestcaseProfiles() {
        return profiles;
    }
    
    @Override
    public void visit(PackageDeclaration n, Void v) {
        packageName = n.getNameAsString();
    }
    
    @Override
    public void visit(ImportDeclaration n, Void v) {
        String importedPackage = n.getNameAsString().replaceAll("\\.\\*|\\.[A-Z]\\w*", "");
        importedPackages.add(importedPackage); 
    }
    
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void v) {
        if (n.isPublic()) {
            classNameNest.push(n.getNameAsString());
            super.visit(n, v);
            classNameNest.pop();
        }
    }
    
    @Override
    public void visit(MethodDeclaration n, Void v) {
        if (n.isAnnotationPresent("Test") && n.isPublic()) {
            TestMethodVisitor visitor = new TestMethodVisitor(map, packageName, foldClassNameNest(), n.getNameAsString(), importedPackages);
            visitor.visit(n, v);
            TestcaseProfile profile = visitor.makeProfile();
            profiles.add(profile);
        }
    }
    
    private String foldClassNameNest() {
    	return String.join("#", classNameNest);
    }
}