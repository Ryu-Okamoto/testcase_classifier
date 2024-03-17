package r_okamot.testcase_classifier;

import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class TestMethodVisitor extends VoidVisitorAdapter<Void> {
    private PackageClassesMap map = null;
    private String path = "";
    private String packageName = "";
    private String className = "";
    private String methodName = "";
    private Set<String> importedPackages = null;
    private Set<String> calledPackages = new HashSet<String>();
    private Set<String> calledClasses = new HashSet<String>();
    
    public TestMethodVisitor(PackageClassesMap map, String path, String packageName, String className, String methodName, Set<String> importedPackages) {
        this.map = map;
        this.path = path;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.importedPackages = importedPackages;
    }
    
    public TestcaseProfile makeProfile() {
        return new TestcaseProfile(
                    path,
                    packageName,
                    className,
                    methodName,
                    calledPackages,
                    calledClasses
                );
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
