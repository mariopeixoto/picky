package com.picky.core;

import com.picky.core.api.DependencyTracker;
import com.picky.core.api.TestSelector;
import com.picky.core.impl.DependencyTrackerImpl;
import com.picky.core.impl.TestSelectorImpl;
import com.picky.core.model.DependencyMap;
import org.reflections.Reflections;

import java.io.File;
import java.util.List;

public class Picky {

    private TestSelector testSelector;
    private DependencyTracker dependencyTracker;

    public Picky(Reflections reflections) {
        this.testSelector = new TestSelectorImpl(reflections);
        this.dependencyTracker = new DependencyTrackerImpl();
    }

    public List<String> findNonAffectedTests() {
        return testSelector.findNonAffectedTests();
    }

    public void saveNewHashes() {
        DependencyMap dependencies = this.dependencyTracker.loadPreviousDependencies();
        this.dependencyTracker.saveHashes(dependencies);
    }

//    public static void main (String[] args) {
//
//        Options options = new Options();
//
//        Option basedir = new Option("b", "basedir", true, "Project base folder");
//        basedir.setRequired(true);
//        options.addOption(basedir);
//
//        Option pickydir = new Option("p", "pickydir", true, "Picky folder");
//        options.addOption(pickydir);
//
//        CommandLineParser parser = new DefaultParser();
//        HelpFormatter formatter = new HelpFormatter();
//        CommandLine cmd;
//
//        try {
//            cmd = parser.parse(options, args);
//        } catch (ParseException e) {
//            logger.info(e.getMessage());
//            formatter.printHelp("utility-name", options);
//
//            System.exit(1);
//            return;
//        }
//
//        String basedirPath = cmd.getOptionValue("basedir");
//        String pickydirPath = cmd.getOptionValue("pickydir", basedirPath + File.separator + ".picky");
//
//        Picky picky = new Picky(new File(basedirPath), new File(pickydirPath));
//        List<String> nonAffected = picky.findNonAffectedTests();
//
//        for (String c : nonAffected) {
//            logger.info("NonAffected: {}", c);
//        }
//    }

}
