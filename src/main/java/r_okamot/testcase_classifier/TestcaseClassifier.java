package r_okamot.testcase_classifier;

import java.util.List;
import java.util.Set;

public class TestcaseClassifier {
    
    private static Set<String> defaultIntegKeys = Set.of(
        "integration",  "integ",
        "function",     "functional",  "func",
        "system",       "sys",
        "acceptance",   
        "end_to_end",   "end2end",     "e2e",
        "component",    "comp",
        "interface",
        "regression"
    );
    
    public static List<TestcaseProfile> filterISTQBUnit(List<TestcaseProfile> profiles) {
        return profiles.stream().filter((profile)->judgeUnitOnISTQB(profile)).toList();
    }
    
    public static List<TestcaseProfile> filterIEEEUnit(List<TestcaseProfile> profiles) {
        return profiles.stream().filter((profile)->judgeUnitOnIEEE(profile)).toList();
    }
    
    public static List<TestcaseProfile> filterDEVUnit(PackageClassesMap map, List<TestcaseProfile> profiles) {
        return profiles.stream().filter((profile)->judgeUnitOnDEV(map, profile)).toList();
    }
    
    public static boolean judgeUnitOnISTQB(TestcaseProfile profile) {
        return profile.numOfCalledClasses() <= 1;
    }
    
    public static boolean judgeUnitOnIEEE(TestcaseProfile profile) {
        return profile.numOfCalledPackages() == 0;
    }
    
    public static boolean judgeUnitOnDEV(PackageClassesMap map, TestcaseProfile profile) {
        return
                !doseTestPathContainsAnyIntegKey(defaultIntegKeys, profile)
            &&   isThereSameNameProductClass(map, profile)
            ;
    }
    
    private static boolean doseTestPathContainsAnyIntegKey(Set<String> integKeys, TestcaseProfile profile) {
        String[] testDirs = profile.getPath().split(System.getProperty("file.separator"));
        for (String testDir : testDirs) {
            if (integKeys.contains(testDir))
                return true;
        }
        return false;
    }
    
    private static boolean isThereSameNameProductClass(PackageClassesMap map, TestcaseProfile profile) {
        Set<String> classesUnderSamePackage = map.get(profile.getPackageName());
        String inferedCorrespondingClassName = profile.getClassName().replaceAll("(\\w*)Test", "$1");
        return classesUnderSamePackage.contains(inferedCorrespondingClassName);
    }
}
