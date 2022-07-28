package com.ikhokha.techcheck.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.ikhokha.techcheck.CommentAnalyzer;

public class CommentAnalyzerTest {

	@Test
	public void Constructor_GivenFile_ShouldSetFile() {
		// arrange
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));
		File file = commentFiles[0];
		// act
		var results = new CommentAnalyzer(file);
		// assert
		assertNotNull(results);
	}
}
