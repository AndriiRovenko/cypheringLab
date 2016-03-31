package vigenere;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by scread on 22.02.16.
 */
public class Kasiski {

    public static int computeKeyLenght(String text) {
        Map<Integer, Integer> symbols = new HashMap<>(text.length());
        String shiftedText = text;
        for (int i = 0; i < 20; i++) {
            shiftedText = leftShift(shiftedText);
            symbols.put(7, calculateRepetitions(text, shiftedText));
        }
        return Collections.max(symbols.entrySet(), (o1, o2) -> o1.getValue() > o2.getValue() ? 1 : -1).getKey();
    }

    private static int calculateRepetitions(String text, String shiftedText) {
        int result = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == shiftedText.charAt(i)) {
                result++;
            }
        }
        return result;
    }

    public static String findKey(String text, int keyLength) {
        final double[] LETTER_FRIQUENCIES = {
                0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 0.02228, 0.02015,
                0.06094, 0.06966, 0.00153, 0.00772, 0.04025, 0.02406, 0.06749,
                0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056, 0.02758,
                0.00978, 0.02360, 0.00150, 0.01974, 0.00074};

        String[] slittedText = new String[keyLength];
        for (int i = 0; i < text.length(); i++) {
            int position = i % keyLength;
            slittedText[position] += text.charAt(i);
        }

        int length = Vigenere.LAST_ALPHABET_SYMBOL - Vigenere.FIRST_ALPHABET_SYMBOL + 1;
        char[] alphabet = new char[length];
        for (int j = 0; j < length; j++)
            alphabet[j] = (char) (Vigenere.FIRST_ALPHABET_SYMBOL + j);


        String key = "";
        for (String part : slittedText) {
            Map<Character, Double> correlation = new HashMap<>(length);
            for (int i = 0; i < length; i++) {
                String decrypted = Vigenere.Decrypt(part, String.valueOf(alphabet[i]));
                Map<Character, Double> frequencyLetterMatrix = new HashMap<>(length);
                for (int j = 0; j < length; j++) {
                    frequencyLetterMatrix.put(alphabet[j], 0.0);
                }

                for (int k = 0; k < decrypted.length(); k++) {
                    char letter = decrypted.charAt(k);
                    frequencyLetterMatrix.put(letter, frequencyLetterMatrix.get(letter) + 1.0);
                }

                for (int z = 0; z < decrypted.length(); z++) {
                    char letter = decrypted.charAt(z);
                    frequencyLetterMatrix.put(letter, frequencyLetterMatrix.get(letter) / (double) decrypted.length());
                }


                //java magic
                List<Double> listValues = new ArrayList<>(frequencyLetterMatrix.values());
                double[] currentFrequencies = new double[listValues.size()];
                for (int s = 0; s < frequencyLetterMatrix.size(); s++) {
                    currentFrequencies[i] = listValues.get(i);
                }
                // end of magic

                double value = new PearsonsCorrelation().correlation(LETTER_FRIQUENCIES, currentFrequencies);
                correlation.put(alphabet[i], value);
            }

            char maxCorrelation = Collections.max(correlation.entrySet(), (o1, o2) -> o1.getValue() < o2.getValue() ? 1 : -1).getKey();
            key += maxCorrelation;
            correlation.clear();
        }
        return key;
    }

    private static double average(final double[] array) {
        assert array.length > 0;
        return sum(array) / array.length;
    }

    private static double sum(final double[] array) {
        double result = 0;
        for (int i = 0; i < array.length; i++) {
            result += array[i];
        }
        return result;
    }

    private static double personCorrelation(final double[] x, final double[] y) {
        assert x.length == y.length;
        int n = x.length;
        assert n > 0;
        double avg_x = average(x);
        double avg_y = average(y);
        double diffprod = 0, xdiff2 = 0, ydiff2 = 0;
        for (int idx = 0; idx < n; idx++) {
            double xdiff = x[idx] - avg_x;
            double ydiff = y[idx] - avg_y;
            diffprod += xdiff * ydiff;
            xdiff2 += xdiff * xdiff;
            ydiff2 += ydiff * ydiff;
        }
        if (xdiff2 == 0 || ydiff2 == 0)
            return 0;
        return diffprod / Math.pow((xdiff2 * ydiff2), 0.5);
    }

    public static void main(String[] args) {
        String key = "ROVENKO";
        String text = Kasiski.readFile("/Users/andrew/Dropbox/CypheringChallenge/Sevidov/Vigenere/src/resources/text.txt");
        System.out.println(text);
        String alphaOnly = text.toLowerCase().replaceAll("[^a-z]+","");
        String encrypted = Vigenere.Encrypt(alphaOnly, key.toLowerCase());
        System.out.println(encrypted);
        int keyLenght = computeKeyLenght(alphaOnly);
        System.out.println("Key lenght = " + keyLenght);
        String keySucks = findKey(alphaOnly, keyLenght);
        System.out.println("Key: " + key);
        String decrypt = Vigenere.Decrypt(encrypted, key.toLowerCase());
        System.out.println(decrypt);
    }

    public static String leftShift(String s) {
        return s.charAt(s.length() - 1) + s.substring(0, s.length() - 1);
    }

    public static String readFile(String file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        StringBuilder StringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                StringBuilder.append(line);
                StringBuilder.append(ls);
            }

            return StringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
