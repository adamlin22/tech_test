package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Analyzer {
    public Report analyze() {
        var report = new Report();
        try {
            var files = getFiles();
            while (files.size() != 0) {
                File cFile = files.pop();
				try (BufferedReader reader = new BufferedReader(new FileReader(cFile))) {
					List<String> lines = reader.lines().collect(Collectors.toList());
					
					long shortComments = lines.stream().filter(line -> line.length() < 15).count();
					long movers = lines.stream().filter(line -> line.toLowerCase().contains("mover")).count();
					long shakers = lines.stream().filter(line -> line.toLowerCase().contains("shaker")).count();
					long questions = lines.stream().filter(line -> line.contains("?")).count();

					Pattern rgx = Pattern.compile("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)", Pattern.CASE_INSENSITIVE);
					long spam = lines.stream().filter(line -> rgx.matcher(line).find()).count();

					report.Shorter += shortComments;
					report.Shakers += shakers;
					report.Movers += movers;
					report.Questions += questions;
					report.Spam += spam;
				} catch (FileNotFoundException e) {
					throw e;
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return report;
    }
    
    private Stack<File> getFiles() {
        Stack<File> fileStack = new Stack<>();
        File[] files = new File("docs").listFiles((file, name) -> name.endsWith(".txt"));
        if (files != null && files.length > 0) {
            for (File file : files) {
                fileStack.push(file);
            }
        }
        return fileStack;
    }
    
}