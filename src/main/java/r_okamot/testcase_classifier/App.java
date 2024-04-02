package r_okamot.testcase_classifier;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;

import org.apache.commons.cli.*;

public class App {
    
    public static void main(String[] args) {
        
        String productDir = "./src/main";
        String testDir = "./src/test";
        String outputPath = ".";
        String specifiedVersion = "latest";
   
        Options options = new Options();
        options.addOption("h", "help", false, "print this message.");
        options.addOption("p", "product", true, "product dir path.");
        options.addOption("t", "test", true, "test dir path.");
        options.addOption("o", "output", true, "output path.");
        options.addOption(
                    Option.builder()
                        .longOpt("java-version")
                        .argName("8|11|17|latest")
                        .desc("java version of target project. ")
                        .hasArg()
                        
                        .build()
                );
        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            
            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("testcase_classeifier", options);
                return;
            }
            
            if (cmd.hasOption("p")) productDir = cmd.getOptionValue("p");
            if (cmd.hasOption("t")) testDir = cmd.getOptionValue("t");
            if (cmd.hasOption("o")) outputPath = cmd.getOptionValue("o");
            
            if (cmd.hasOption("java-version")) {
                specifiedVersion = cmd.getOptionValue("java-version");
                if (!specifiedVersion.equals("8") &&
                        !specifiedVersion.equals("11") &&
                        !specifiedVersion.equals("17") &&
                        !specifiedVersion.equals("latest")
                   ) {
                    System.out.println(specifiedVersion + "invalid java version.");
                    return;
                }
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        
        setJavaVersion(specifiedVersion);
        try {
            PackageClassesMap map = new PackageClassesMapBuilder(productDir).build();
            List<TestcaseProfile> profiles = new TestcaseProfiler(testDir, map).make();
            String summaryOfAll = summaryProfiles(profiles);
            String summaryOfISTQB = summaryProfiles(TestcaseClassifier.filterISTQBUnit(profiles));
            String summaryOfIEEE = summaryProfiles(TestcaseClassifier.filterIEEEUnit(profiles));
            String summaryOfDEV = summaryProfiles(TestcaseClassifier.filterDEVUnit(map, profiles));
            outputSummary(outputPath, "all_profiles.csv", summaryOfAll);
            outputSummary(outputPath, "istqb_profiles.csv", summaryOfISTQB);
            outputSummary(outputPath, "ieee_profiles.csv", summaryOfIEEE);
            outputSummary(outputPath, "dev_profiles.csv", summaryOfDEV);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
    }
    
    private static void setJavaVersion(String version) {
        ParserConfiguration config = new ParserConfiguration();
        switch (version) {
        case  "8": config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_8);  break;
        case "11": config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_11); break;
        case "17": config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17); break;
        default:   config.setLanguageLevel(ParserConfiguration.LanguageLevel.CURRENT); break;
        }
        StaticJavaParser.setConfiguration(config);
    }
    
    private static String summaryProfiles(List<TestcaseProfile> profiles) {
        return String.join("\n", 
                profiles.stream()
                        .map(profile->profile.getPath() + "," + profile.getName() + "," + profile.numOfCalledPackages() + "," + profile.numOfCalledClasses())
                        .toList()
        );
    }
    
    private static void outputSummary(String outputDir, String outputFileName, String summary) throws IOException {
        if (!Files.exists(Paths.get(outputDir)))
            Files.createDirectories(Paths.get(outputDir));
        Path outputPath = Paths.get(outputDir, outputFileName);
        BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardOpenOption.CREATE);
        writer.write(summary);
        writer.close();
    }
}
