package com.ikhokha.techcheck;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	static int SHORTER_THAN_15 = 0;
	static int MOVER_MENTIONS = 0;
	static int SHAKER_MENTIONS = 0;

	public static void main(String[] args) {
		List<File> files = new ArrayList<>();
		Map<String, Integer> totalResults = new HashMap<>();

		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));

		int threadsCountToExecute = 2;
		int filesPerThread = (commentFiles.length / threadsCountToExecute);
		int remainingFiles = (commentFiles.length % threadsCountToExecute);
		Thread[] threads = new Thread[threadsCountToExecute];

		int i = 0;
		while (i < threadsCountToExecute) {
			int currentThread = i;
			threads[i] = new Thread() {
				@Override
				public void run() {
					runThread(commentFiles, threadsCountToExecute, currentThread, filesPerThread, remainingFiles,
							files);
				}
			};
			i++;
		}

		for (int j = 0; j < threads.length; j++) {
			Thread thread = threads[j];
			thread.start();
		}

		for (int j = 0; j < threads.length; j++) {
			Thread thread = threads[j];
			try {
				thread.join();
			} catch (InterruptedException ex) {
				System.out.println("an error occured : " + ex);
			}
		}

		for (File commentFile : files) {
			CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
			Map<String, Integer> fileResults = commentAnalyzer.analyze();
			addReportResults(fileResults, totalResults);
		}

		setResults(totalResults);
		System.out.println("RESULTS\n=======");
		totalResults.forEach((k, v) -> System.out.println(k + " : " + v));
	}

	private static void runThread(File[] commentFiles, int threadsCountToExecute, int currentThread, int filesPerThread,
			int remainingFiles, List<File> files) {

		int k = (currentThread * filesPerThread);
		while (k < (currentThread + 1)) {
			files.add(commentFiles[k]);
			k++;
		}

		if (currentThread == (threadsCountToExecute - 1) && remainingFiles > 0) {
			int c = (commentFiles.length - remainingFiles);
			while (c < commentFiles.length) {
				files.add(commentFiles[c]);
				c++;
			}
		}
	}

	/**
	 * This method adds the calculated result counts to the totalResults object	 * 
	 * @param source the totalResults map 
	 */
	private static void setResults(Map<String, Integer> totalResults) {
		totalResults.put("SHORTER_THAN_15", SHORTER_THAN_15);
		totalResults.put("MOVER_MENTIONS", MOVER_MENTIONS);
		totalResults.put("SHAKER_MENTIONS", SHAKER_MENTIONS);
	}

	/**
	 * This method adds the result counts from a source map to the target map	 * 
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
