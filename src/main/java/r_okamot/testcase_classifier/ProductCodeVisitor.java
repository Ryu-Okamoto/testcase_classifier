package r_okamot.testcase_classifier;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;

public class ProductCodeVisitor extends VoidVisitorAdapter<Void> {  
    private String packageName = "";
    private String className = "";
    
    public String getPackageName() {
        return packageName;
    }
    public String getClassName() {
        return className;
    }
    
    @Override
    public void visit(PackageDeclaration n, Void v) {
        packageName = n.getNameAsString();
    }
    
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void v) {
        if (n.isPublic())
            className = n.getNameAsString();
    }
    
    @Override
    public void visit(RecordDeclaration n, Void v) {
        if (n.isPublic())
            className = n.getNameAsString();
    }
}
