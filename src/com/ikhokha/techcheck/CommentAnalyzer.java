package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommentAnalyzer {
	
	private File file;
	
	public CommentAnalyzer(File file) {
		this.file = file;
	}
	
	public Callable<Report> analyze = ()  -> {
		Report report = new Report();
		try {
			try (var reader = new BufferedReader(new FileReader(file))) {
				var lines = reader.lines().collect(Collectors.toList());

				long shortComments = lines.stream().filter(line -> line.length() < 15).count();
				long movers = lines.stream().filter(line -> line.toLowerCase().contains("mover")).count();
				long shakers = lines.stream().filter(line -> line.toLowerCase().contains("shaker")).count();
				long questions = lines.stream().filter(line -> line.contains("?")).count();

				var rgx = Pattern.compile("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)", Pattern.CASE_INSENSITIVE);
				long spam = lines.stream().filter(line -> rgx.matcher(line).find()).count();

				report.Shorter = shortComments;
				report.Shakers = shakers;
				report.Movers = movers;
				report.Questions = questions;
				report.Spam = spam;
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return report;
	};

}
