package com.dijure.stella;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
@ImportRuntimeHints(StellaApplication.StellaHints.class)
public class StellaApplication {

    /**
     * Tell GraalVM that streetcar.txt has to be inside the native image.
     *
     * A native image contains only what the compiler can PROVE is reachable, and a resource
     * fetched by name at runtime proves nothing at compile time. Without this hint the file
     * is left out, getResourceAsStream returns null, and the application dies on its first
     * read:
     *
     *   Exception in thread "main" java.lang.NullPointerException
     *       at java.io.InputStreamReader.<init>(InputStreamReader.java:82)
     *       at com.dijure.stella.StellaApplication.wordFrequency(StellaApplication.java:32)
     *
     * The JVM builds have never needed this, which is the point worth noticing: distilling
     * an application to a native binary changes what "on the classpath" means.
     */
    static class StellaHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerPattern("streetcar.txt");
        }
    }

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
