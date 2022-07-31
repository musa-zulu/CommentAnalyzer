package com.ikhokha.techcheck;

import java.util.Map;

public class OperationHandler implements Operation {
	int results = 0;
	@Override
	public int calculateResults(Map<String, Integer> source) {		
		for (Map.Entry<String, Integer> entry : source.entrySet()) {			
			results += entry.getValue();
		}
		return results;
	}
}