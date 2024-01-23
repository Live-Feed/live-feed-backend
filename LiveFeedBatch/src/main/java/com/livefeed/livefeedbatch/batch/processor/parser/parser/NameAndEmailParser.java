package com.livefeed.livefeedbatch.batch.processor.parser.parser;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NameAndEmailParser {

    private static final String NAME_SUFFIX = "기자";
    private static final List<String> nameEmailPattern = List.of("([\\p{L}\\s]+)(?:\\s?\\(([^)]+)\\)|\\s([^\\s]+))");

    public Pair<String, String> extractNameAndEmail(String input) {
        for (String patternString : nameEmailPattern) {
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                String name = addSuffix(matcher.group(1).trim()); // 이름 추출
                String email = matcher.group(2) != null ? matcher.group(2) : matcher.group(3); // 이메일 추출

                return Pair.of(name, email);
            }
        }
        return null;
    }

    private String addSuffix(String name) {
        int index = name.indexOf(NAME_SUFFIX);
        if (index == -1) {
            return name + " " + NAME_SUFFIX;
        }
        return name;
    }
}
