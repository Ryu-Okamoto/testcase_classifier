package r_okamot.testcase_classifier;

import java.util.Set;

public class TestcaseProfile {
    private String path = "";
    private String packageName = "";
    private String className = "";
    private String methodName = "";
    private Set<String> calledPackages = null;
    private Set<String> calledClasses = null;
    
    public TestcaseProfile(
            String path,
            String packageName,
            String className,
            String methodName,
            Set<String> calledPackages,
            Set<String> calledClasses
    ) {
        this.path = path;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.calledPackages = calledPackages;
        this.calledClasses = calledClasses;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public String getClassName() {
        return className;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public String getName() {
        return className + "#" + methodName;
    }
    
    public Set<String> getCalledPackages() {
        return calledPackages;
    }
    
    public int numOfCalledPackages() {
        return calledPackages.size();
    }
    
    public Set<String> getCalledClasses() {
        return calledClasses;
    }
    
    public int numOfCalledClasses() {
        return calledClasses.size();
    }
}