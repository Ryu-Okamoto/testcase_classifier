package r_okamot.testcase_classifier;

import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class TestMethodVisitor extends VoidVisitorAdapter<Void> {
    
    private PackageClassesMap map = null;
    private String packageName = "";
    private String className = "";
    private String methodName = "";
    private Set<String> importedPackages = null;
    private Set<String> calledPackages = new HashSet<String>();
    private Set<String> calledClasses = new HashSet<String>();
    
    public TestMethodVisitor(PackageClassesMap map, String packageName, String className, Set<String> importedPackages) {
        this.map = map;
        this.packageName = packageName;
        this.className = className;
        this.importedPackages = importedPackages;
    }
    
    public TestcaseProfile makeProfile() {
        return new TestcaseProfile(
                    packageName,
                    className + "#" + methodName,
                    calledPackages,
                    calledClasses
                );
    }
    
    @Override
    public void visit(MethodDeclaration n, Void v) {
        methodName = n.getNameAsString();
        super.visit(n, v);
    }
    
    @Override
    public void visit(ClassOrInterfaceType n, Void v) {
        String typeName = n.asString();
        Set<String> classesUnderSamePackage = map.get(packageName);
        if (classesUnderSamePackage.contains(typeName)) {
            calledClasses.add(typeName);
            return;
        }
        for (String importedPackage : importedPackages) {
            Set<String> importedClasses = map.get(importedPackage);
            if (importedClasses.contains(typeName)) {
                calledPackages.add(importedPackage);
                calledClasses.add(typeName);
                return;
            }
        }
    }
}
