package r_okamot.testcase_classifier_istqb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class TestCodeVisitor extends VoidVisitorAdapter<Void> {

   private PackageClassesMap map = null;
   private Set<String> userDefinedPackages = null;
   private Set<String> userDefinedClasses = null;
    
    public TestCodeVisitor(PackageClassesMap map) {
        this.map = map.copy();
        userDefinedPackages = map.keySet();
        userDefinedClasses = new HashSet<String>();
        for (String packageName : userDefinedPackages)
            userDefinedClasses.addAll(map.get(packageName));
    }
    
    private String packageName = "";
    private Set<String> importedPackages = new HashSet<String>();
    private Set<String> calledClasses = new HashSet<String>();
    
    @Override
    public void visit(PackageDeclaration n, Void v) {
        packageName = n.getNameAsString();
        userDefinedClasses.addAll(map.get(packageName));
    }
    @Override
    public void visit(ImportDeclaration n, Void v) {
        String importedPackage = n.getNameAsString().replaceAll("\\.\\*|\\.[A-Z]\\w*", "");
        importedPackages.add(importedPackage); 
        userDefinedClasses.addAll(map.get(importedPackage));
    }
    @Override
    public void visit(ClassOrInterfaceType n, Void v) {
        String className = n.asString();
        calledClasses.add(className);
    }
    
    private String testClassName = "";
    private List<String> testMethodNames = new ArrayList<String>();
    
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void v) {
        if (n.isPublic())
            testClassName = n.getNameAsString();
        super.visit(n, v);
    }
    @Override
    public void visit(MethodDeclaration n, Void v) {
        if (n.isAnnotationPresent("Test"))
            testMethodNames.add(n.getNameAsString());
        super.visit(n, v);
    }
    
    public String getTestClassName() {
        return testClassName;
    }
    public List<String> getTestMethodNames() {
        return testMethodNames;
    }
    public int getNumOfImportedPackages() {
        Set<String> intersection = new HashSet<String>(importedPackages);
        intersection.retainAll(userDefinedPackages);
        return intersection.size();
    }
    public int getNumOfCalledUserClasses() {
        Set<String> intersection = new HashSet<String>(calledClasses);
        intersection.retainAll(userDefinedClasses);
        return intersection.size();
    }
}