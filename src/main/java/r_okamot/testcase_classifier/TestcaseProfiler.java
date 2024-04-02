package r_okamot.testcase_classifier;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class TestcaseProfiler {
    private Path testDir = null;
    private PackageClassesMap map = null;
    
    public TestcaseProfiler(String testDir, PackageClassesMap map) {
        this.testDir = Paths.get(testDir);
        this.map = map;
    }
    
    public List<TestcaseProfile> make() throws IOException {
        List<TestcaseProfile> results = new ArrayList<TestcaseProfile>();
        List<Path> testFiles = getTestFiles();
        for (Path testFile : testFiles) {
            String testCode = readAll(testFile);
            
            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(testCode);
            }
            catch (ParseProblemException e) {
                System.err.println("compile error occured at " + testFile.toString() + ".");
                System.err.println(e.getMessage());
                continue;
            }
            
            TestCodeVisitor visitor = new TestCodeVisitor(map, testFile.toString());
            cu.accept(visitor, null);
            List<TestcaseProfile> profiles = visitor.getTestcaseProfiles();
            results.addAll(profiles);
        }
        return results;
    }
    
    private List<Path> getTestFiles() throws IOException {
        return Files.walk(testDir)
                .map(path->path.toString())
                .filter(path->path.endsWith(".java"))
                .map(path->Paths.get(path))
                .toList();
    }
    
    private static String readAll(Path path) throws IOException {
        return Files.lines(path, Charset.forName("UTF-8"))
            .collect(Collectors.joining(System.getProperty("line.separator")));
    }
}
