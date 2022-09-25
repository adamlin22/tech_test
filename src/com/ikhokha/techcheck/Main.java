package com.ikhokha.techcheck;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

	public static void main(String[] args) {
		try {
			File[] files = new File("docs").listFiles((file, s) -> s.endsWith(".txt"));
			
			int nThread = files.length < 10 ? files.length : files.length / 4;
			ExecutorService executor = Executors.newFixedThreadPool(nThread);
			
			Stack<Future<Report>> futures = new Stack<>();
			ArrayList<Report> reports = new ArrayList<>();
			
			for (File file : files) {
				var c = new CommentAnalyzer(file);
				futures.push(executor.submit(c.analyze));
			}
			
			while (!futures.empty()) {
				var result = futures.pop();
				var comment = result.get();
				reports.add(comment);
			}
			executor.shutdown();
			printReportResult(reports);
		} catch (ExecutionException | InterruptedException e) {
			System.out.println("Thread execution error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void printReportResult(ArrayList<Report> reports) {
		Report r = new Report();
		for (int i = 0; i < reports.size(); i++) {
			r.Shorter += reports.get(i).Shorter;
			r.Movers += reports.get(i).Movers;
			r.Shakers += reports.get(i).Shakers;
			r.Questions += reports.get(i).Questions;
			r.Spam += reports.get(i).Spam;
		}

		System.out.printf("SHORTER_THAN_15: %s%n", r.Shorter);
		System.out.printf("MOVERS_MENTIONS: %s%n", r.Movers);
		System.out.printf("SHAKERS_MENTIONS: %s%n", r.Shakers);
		System.out.printf("QUESTIONS: %s%n", r.Questions);
		System.out.printf("SPAM: %s%n", r.Spam);
		
		
	}
}
