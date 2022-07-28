package com.ikhokha.techcheck;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {
	static int SHORTER_THAN_15 = 0;
	static int MOVER_MENTIONS = 0;
	static int SHAKER_MENTIONS = 0;
	public static void main(String[] args) {
		
		Map<String, Integer> totalResults = new HashMap<>();
				
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));
		
		for (File commentFile : commentFiles) {			
			CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
			Map<String, Integer> fileResults = commentAnalyzer.analyze();
			addReportResults(fileResults, totalResults);
		}
			
		setResults(totalResults);
		System.out.println("RESULTS\n=======");
		totalResults.forEach((k,v) -> System.out.println(k + " : " + v));		
	}

	/**
	 * This method adds the calculated result counts to the totalResults object 
	 * @param source the totalResults map	 * 
	 */
	private static void setResults(Map<String, Integer> totalResults) {
		totalResults.put("SHORTER_THAN_15", SHORTER_THAN_15);
		totalResults.put("MOVER_MENTIONS", MOVER_MENTIONS);
		totalResults.put("SHAKER_MENTIONS", SHAKER_MENTIONS);
	}	

	/**
	 * This method adds the result counts from a source map to the target map 
	 * @param source the source map
	 * @param target the target map
	 */
	private static void addReportResults(Map<String, Integer> source, Map<String, Integer> target) {
		for (Map.Entry<String, Integer> entry : source.entrySet()) {
			if (entry.getKey() == "SHORTER_THAN_15") {
				SHORTER_THAN_15 += entry.getValue();
			} else if (entry.getKey() == "MOVER_MENTIONS") {
				MOVER_MENTIONS += entry.getValue();
			} else {
				SHAKER_MENTIONS += entry.getValue();
			}
		}	
	}
}
