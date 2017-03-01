package test;

import uniud.trecevaloo.control.EvaluatorManager;
import uniud.trecevaloo.metrics.definitions.AllTRECMetrics;
import uniud.trecevaloo.metrics.definitions.MetricSet;
import uniud.trecevaloo.metrics.results.FileResultExporter;
import uniud.trecevaloo.metrics.results.ResultExporter;
import uniud.trecevaloo.metrics.results.ResultViewer;
import uniud.trecevaloo.relevance.NumericRelevanceType;
import uniud.trecevaloo.relevance.NumericalCategoryRelevanceType;
import uniud.trecevaloo.relevance.RelevanceType;
import uniud.trecevaloo.run.AdHocTRECRunSet;
import uniud.trecevaloo.run.RunSet;
import uniud.trecevaloo.testcollection.AdHocTRECCollection;
import uniud.trecevaloo.testcollection.Collection;

import java.io.IOException;

/**
 * Created by Francesco on 28/02/2017.
 */
public class Test {
    private static String qrelPath = "";
    private static String runPath = "";
    private static String outFilePath = "";
    private static int numOfDocsFlag = -1;
    private static boolean onlyJudgedFlag = false;
    private static boolean allTopicsFlag = true;

    private static MetricSet metrics = new AllTRECMetrics().getMetricSet();
    private static ResultViewer resultViewer = null;
    private static ResultExporter resultExporter = null;
    private static RelevanceType relevanceType = new NumericalCategoryRelevanceType(3, 1);
    private static RelevanceType runRelevanceType = new NumericRelevanceType(1.0);
    private static double totalTime = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("--------------- TESTING TRECEVALOO --------------------");

        checkInputFlags(args);

        String test = "";
        double time = compute();
        test += "Test time: " + time + "\n";
        totalTime += time;
        System.out.print(test);
    }


    private static double compute() {
        if (runPath.isEmpty()) {
            System.out.println("Missing run path (or runs folder path)");
            return 0;
        } else if (qrelPath.isEmpty()) {
            System.out.println("Missing qrels path");
            return 0;
        }

        System.out.println();
        System.out.println("qrelPath: " + qrelPath);
        System.out.println("runPath: " + runPath);
        System.out.println();

        Collection collection = new AdHocTRECCollection(relevanceType, "", "", qrelPath);
        RunSet runSet = new AdHocTRECRunSet(runRelevanceType, runPath);

        EvaluatorManager evaluatorManager = EvaluatorManager.getInstance();
        evaluatorManager.init(collection, runSet, metrics);

        // Opzioni
        if (numOfDocsFlag > -1) {
            System.out.println("Num of docs per topic: " + numOfDocsFlag);
            evaluatorManager.setNumOfDocsPerTopic(numOfDocsFlag);
        }

        if (onlyJudgedFlag) {
            System.out.println("Consider only judged docs: active");
            evaluatorManager.considerOnlyJudgedDocs();
        }

        if (allTopicsFlag) {
            System.out.println("Average over all topics in collection: active");
            evaluatorManager.averageOverAllTopicsInCollection();
        }

        // Inizio computazione
        evaluatorManager.evaluate();


        // Mostra risultati
        if (resultViewer != null) {
            evaluatorManager.showResults(resultViewer);
        }

        // Esporta risultati in un file
        if (!outFilePath.isEmpty()) {
            resultExporter = new FileResultExporter(outFilePath);
            evaluatorManager.exportResults(resultExporter);
        }

        return evaluatorManager.time;

    }

    private static void checkInputFlags(String[] args) {
        if(args.length > 1) {
            if (!args[0].contains("none")) {
                qrelPath = args[0];
            }

            if (!args[1].contains("none")) {
                runPath = args[1];
            }

            if (!args[2].contains("none")) {
                outFilePath = args[2];
            }
        }
    }
}
