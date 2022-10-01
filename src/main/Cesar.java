package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class Cesar {
    public static Character[] alphabet = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', '.', ',', '"', '\'', ':', '-', '!', '?', ' '};

    public static String crypt(String raw, int key) {
        List<Character> alphabetList = Arrays.asList(alphabet);
        List<Character> resultList = new ArrayList<>();

        for (char c : raw.toCharArray()) {
            if (alphabetList.contains(c)) {
                int index = alphabetList.indexOf(c);
                resultList.add(alphabetList.get((index + key) % alphabet.length));
            } else {
                resultList.add(c);
            }
        }

        return getStringRepresentation(resultList);
    }

    public static String decrypt(String raw, int key) {
        List<Character> alphabetList = Arrays.asList(alphabet);
        List<Character> resultList = new ArrayList<>();

        for (char c : raw.toCharArray()) {
            if (alphabetList.contains(c)) {
                int index = alphabetList.indexOf(c);
                resultList.add(alphabetList.get((index - key < 0 ? alphabet.length - (key - index) : index - key)));
            } else {
                resultList.add(c);
            }
        }

        return getStringRepresentation(resultList);
    }

    static String getStringRepresentation(List<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (Character ch : list) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public static int textAnalyze(String text) {
        int failPoints = 0;

        if (text.indexOf(' ') == -1 && text.length() >= 25) {
            return 1000;
        }

        var words = text.split(" ");
        for (String word : words) {
            if (word.length() > 25) failPoints++;
        }

        int singleQuotesCount = 0, doubleQuotesCount = 0;

        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) == ',' && text.charAt(i + 1) != ' ') {
                failPoints++;
            }

            if (text.charAt(i) == '?' && (text.charAt(i + 1) != ' ' && text.charAt(i + 1) != '?')) {
                failPoints++;
            }

            if (text.charAt(i) == '!' && (text.charAt(i + 1) != ' ' && text.charAt(i + 1) != '!')) {
                failPoints++;
            }

            if (text.charAt(i) == '\'') {
                singleQuotesCount++;
            }

            if (text.charAt(i) == '"') {
                doubleQuotesCount++;
            }
        }

        if (singleQuotesCount % 2 != 0) failPoints += 5;
        if (doubleQuotesCount % 2 != 0) failPoints += 5;

        return failPoints;
    }
}
