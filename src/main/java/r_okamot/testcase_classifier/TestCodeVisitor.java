package r_okamot.testcase_classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class TestCodeVisitor extends VoidVisitorAdapter<Void> {

    private PackageClassesMap map = null;
    private String packageName = "";
    private String className = "";
    private Set<String> importedPackages = new HashSet<String>();
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
        if (n.isPublic())
            className = n.getNameAsString();
        super.visit(n, v);
    }
    
    @Override
    public void visit(MethodDeclaration n, Void v) {
        if (n.isAnnotationPresent("Test") && n.isPublic()) {
            TestMethodVisitor visitor = new TestMethodVisitor(map, packageName, className, importedPackages);
            visitor.visit(n, v);
            TestcaseProfile profile = visitor.makeProfile();
            profiles.add(profile);
        }
    }
}