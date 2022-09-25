package com.ikhokha.techcheck;

public class Main {

	public static void main(String[] args) {
		
		Analyzer analyzer = new Analyzer();
		Report report = analyzer.analyze();
		
		System.out.println("SHORTER_THAN_15: " + report.Shorter);
		System.out.println("MOVERS_MENTIONS: " + report.Movers);
		System.out.println("SHAKER_MENTIONS: " + report.Shakers);
		System.out.println("QUESTIONS: " + report.Questions);
		System.out.println("SPAM: " + report.Spam);
	}

}
