package r_okamot.testcase_classifier;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class PackageClassesMapBuilder {
    
    private Path productDir = Paths.get(".");
    
    public void setProductDir(String productDir) {
        this.productDir = Paths.get(productDir);
    }
    public String getProductDir() {
        return productDir.toString();
    }
    
    public PackageClassesMap build() throws IOException {
        PackageClassesMap map = new PackageClassesMap();
        for (Path productFile : getProductFiles()) {
            String productCode = readAll(productFile);
            
            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(productCode);
            }
            catch (ParseProblemException e) {
                System.err.println("compile error occured");
                System.err.println(e.getMessage());
            }
            
            ProductCodeVisitor visitor = new ProductCodeVisitor();
            cu.accept(visitor, null);
            String packageName = visitor.getPackageName();
            String className = visitor.getClassName();
            map.add(packageName, className);
        }
        return map;
    }
    
    private List<Path> getProductFiles() throws IOException {
        return Files.walk(productDir)
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
