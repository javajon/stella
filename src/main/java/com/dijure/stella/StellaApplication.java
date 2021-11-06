package com.dijure.stella;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class StellaApplication {

    private static final Pattern WORD_PATTERN = Pattern.compile("[A-Za-z][a-z]+");

    public static void main(String[] args) {
        SpringApplication.run(StellaApplication.class, args);
        System.out.println("\"Stella\" counts are " + wordFrequency
                ("stella", "stellahhhhh", "stelllahhhhh"));
    }

    public static TreeMap<String, Integer> wordFrequency(String... wordSearch) {

        TreeMap<String, Integer> histogram = new TreeMap<>();
        InputStream is = StellaApplication.class.getClassLoader()
                .getResourceAsStream("streetcar.txt");

        try (BufferedReader infile = new BufferedReader(new InputStreamReader(is))) {
            while (infile.ready()) {
                String line = infile.readLine().toLowerCase(Locale.ROOT);
                if (!line.endsWith(":")) { // Ignore script character instruction, e.g. "Stella:"
                    Matcher matcher = WORD_PATTERN.matcher(line);
                    while (matcher.find()) {
                        String word = matcher.group().trim();
                        histogram.put(word, histogram.containsKey(word) ? histogram.get(word) + 1 : 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TreeMap<String, Integer> results = new TreeMap<>();
        for (String word : wordSearch) {
            results.put(word, histogram.get(word));
        }

        return results;
    }
}
