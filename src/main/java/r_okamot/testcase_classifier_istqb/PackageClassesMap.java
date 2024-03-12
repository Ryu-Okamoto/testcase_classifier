package r_okamot.testcase_classifier_istqb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PackageClassesMap {
    
    private Map<String, Set<String>> map = new HashMap<String, Set<String>>();
    
    // map-like interface
    public boolean containsKey(String packageName) {
        return map.containsKey(packageName);
    }
    public boolean contains(String packageName, String className) {
        if (!containsKey(packageName))
            return false;
        Set<String> classNames = get(packageName);
        return classNames.contains(className);
    }
    public Set<String> keySet() {
        return map.keySet();
    }
    public Set<String> get(String packageName) {
        return map.get(packageName);
    }
    
    public void add(String packageName, String className) {
        if (!map.containsKey(packageName)) {
            map.put(packageName, new HashSet<String>());
        }
        Set<String> classNames = map.get(packageName);
        classNames.add(className);
    }
    
    public void add(String packageName, Set<String> classNames) {
        if (!map.containsKey(packageName)) {
            map.put(packageName, classNames);
        }
        else {
            Set<String> oldSet = map.get(packageName);
            oldSet.addAll(classNames);
        }
    }

    public PackageClassesMap copy() {
        PackageClassesMap newMap = new PackageClassesMap();
        for (String packageName : keySet()) {
            newMap.add(packageName, new HashSet<String>(get(packageName)));
        }
        return newMap;
    }
}
