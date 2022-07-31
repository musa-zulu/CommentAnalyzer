package com.ikhokha.techcheck;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
	static int SHORTER_THAN_15 = 0;
	static int MOVER_MENTIONS = 0;
	static int SHAKER_MENTIONS = 0;
	static int QUESTIONS = 0;
	static int SPAM = 0;

	public static void main(String[] args) {
		List<File> files = new ArrayList<>();
		Map<String, Integer> totalResults = new HashMap<>();

		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));

		handleThreading(files, commentFiles);

		for (File commentFile : files) {
			CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
			Map<String, Integer> fileResults = commentAnalyzer.analyze();
			
			calculateResults(fileResults);			
		}

		setResults(totalResults);
		System.out.println("RESULTS\n=======");
		totalResults.forEach((k, v) -> System.out.println(k + " : " + v));
	}

	private static void calculateResults(Map<String, Integer> fileResults) {
		SHORTER_THAN_15 = calculateUsingFactory(fileResults
				.entrySet()
		        .stream().filter(x -> x.getKey().toString().equals("SHORTER_THAN_15"))
		        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), "SHORTER_THAN_15");
		
		MOVER_MENTIONS = calculateUsingFactory(fileResults
				.entrySet()
		        .stream().filter(x -> x.getKey().toString().equals("MOVER_MENTIONS"))
		        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), "MOVER_MENTIONS");
		
		QUESTIONS = calculateUsingFactory(fileResults
				.entrySet()
		        .stream().filter(x -> x.getKey().toString().equals("QUESTIONS"))
		        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), "QUESTIONS");
		
		SHAKER_MENTIONS = calculateUsingFactory(fileResults
				.entrySet()
		        .stream().filter(x -> x.getKey().toString().equals("SHAKER_MENTIONS"))
		        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), "SHAKER_MENTIONS");
		
		SPAM = calculateUsingFactory(fileResults
				.entrySet()
		        .stream().filter(x -> x.getKey().toString().equals("SPAM"))
		        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), "SPAM");
	}

	/**
	 * This method handles threading on the system
	 * 
	 * @param files
	 * @param commentFiles
	 */
	private static void handleThreading(List<File> files, File[] commentFiles) {
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
	}

	/**
	 *
	 * @param commentFiles
	 * @param threadsCountToExecute
	 * @param currentThread
	 * @param filesPerThread
	 * @param remainingFiles
	 * @param files
	 */
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
	 * This method adds the calculated result counts to the totalResults object *
	 * 
	 * @param source the totalResults map
	 */
	private static void setResults(Map<String, Integer> totalResults) {
		totalResults.put("SHORTER_THAN_15", SHORTER_THAN_15);
		totalResults.put("MOVER_MENTIONS", MOVER_MENTIONS);
		totalResults.put("SHAKER_MENTIONS", SHAKER_MENTIONS);
		totalResults.put("QUESTIONS", QUESTIONS);
		totalResults.put("SPAM", SPAM);
	}

	public static int calculateUsingFactory(Map<String, Integer> source, String operator) {
		Operation targetOperation = OperatorFactory.getOperation(operator)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Operator"));
		return targetOperation.calculateResults(source);
	}	
	
}
