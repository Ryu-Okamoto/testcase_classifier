package r_okamot.testcase_classifier;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class App {
    
    public static void main(String[] args) {
        
        if (args.length != 3) {
            System.out.println("Usage: java -jar testcase_classifier.jar ${product dir} ${test dir} ${output dir}");
            return;
        }
        String productDir = args[0];
        String testDir = args[1];
        String outputPath = args[2];
        
        try {
            PackageClassesMap map = new PackageClassesMapBuilder(productDir).build();
            List<TestcaseProfile> profiles = new TestcaseProfiler(testDir, map).make();
            String summary = summaryProfiles(profiles);
            outputSummary(outputPath, summary);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
    }
    
    public static String summaryProfiles(List<TestcaseProfile> profiles) {
        return String.join("\n", 
                profiles.stream()
                        .map(profile->profile.getName() + "," + profile.numOfCalledPackages() + "," + profile.numOfCalledClasses())
                        .toList()
        );
    }
    
    private static void outputSummary(String outputDir, String summary) throws IOException {
        if (!Files.exists(Paths.get(outputDir)))
            Files.createDirectories(Paths.get(outputDir));
        Path outputPath = Paths.get(outputDir, "testcase_profiles_summary.txt");
        BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardOpenOption.CREATE);
        writer.write(summary);
        writer.close();
    }
}
