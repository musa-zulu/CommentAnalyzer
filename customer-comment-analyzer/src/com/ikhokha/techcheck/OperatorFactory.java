package com.ikhokha.techcheck;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OperatorFactory {
	static Map<String, Operation> operationMap = new HashMap<>();

	static {
		operationMap.put("SHORTER_THAN_15", new OperationHandler());
		operationMap.put("MOVER_MENTIONS", new OperationHandler());
		operationMap.put("QUESTIONS", new OperationHandler());
		operationMap.put("SHAKER_MENTIONS", new OperationHandler());
		operationMap.put("SPAM", new OperationHandler());
	}

	public static Optional<Operation> getOperation(String operator) {
		return Optional.ofNullable(operationMap.get(operator));
	}
}
