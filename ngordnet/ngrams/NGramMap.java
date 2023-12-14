package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    private Map<String, TimeSeries> wordsResult = new HashMap<>();
    private TimeSeries countsResult = new TimeSeries();

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {

        In wordsFile = new In(wordsFilename);

        readWordsFile(wordsFile);

        In countsFile = new In(countsFilename);

        readCountsFile(countsFile);

    }

    private void readWordsFile(In wordsFile) {
        while (wordsFile.hasNextLine()) {
            String word = wordsFile.readString(); // word
            int yearWord = wordsFile.readInt(); // year
            double value1 = wordsFile.readDouble(); // number of times the word appeared in any book that year

            wordsResult.putIfAbsent(word, new TimeSeries());
            wordsResult.get(word).put(yearWord, value1);

            wordsFile.readLine();

        }

        wordsFile.close();
    }

    private void readCountsFile(In countsFile) {
        while (countsFile.hasNextLine()) {
            String line = countsFile.readLine();
            String[] tokens = line.split(",");

            if (tokens.length >= 4) {

                int yearCounts = Integer.parseInt(tokens[0]); // year
                Double wordsCounts = Double.parseDouble(tokens[1]); //  total number of words recorded from all texts that year

                countsResult.put(yearCounts, wordsCounts);

            }
        }

        countsFile.close();
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries result = new TimeSeries();

        if (wordsResult.containsKey(word)) {
            TimeSeries wordTs = wordsResult.get(word);

            for (Map.Entry<Integer, Double> entry : wordTs.entrySet()) {
                int year = entry.getKey();

                // Check if the year is within the specified range
                if (year >= startYear && year <= endYear) {
                    // Add the entry to the result TimeSeries
                    result.put(year, entry.getValue());
                }
            }
        }
        // Return the result TimeSeries
        return result;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        TimeSeries result = new TimeSeries();

        if (wordsResult.containsKey(word)) {
            TimeSeries wordTs = wordsResult.get(word);

            for (Map.Entry<Integer, Double> entry : wordTs.entrySet()) {

                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries result = new TimeSeries();

        result = countsResult;

        return result;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries result = new TimeSeries();
        TimeSeries wordFreq = countHistory(word, startYear, endYear);
        TimeSeries totalCount = totalCountHistory();

        for (Integer year : wordFreq.keySet()) {
            if (totalCount.containsKey(year)) {
                double wordFreqValue = wordFreq.get(year);
                double totalCountValue = totalCount.get(year);

                if (totalCountValue != 0) {
                    result.put(year, wordFreqValue / totalCountValue);
                } else {
                    throw new NullPointerException(word + "is Null");
                }
            }
        }
        return result;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries result = new TimeSeries();
        TimeSeries wordFreq = countHistory(word);

        if (wordFreq.isEmpty()) {
            return result;
        }
        TimeSeries totalCount = totalCountHistory();

        for (Integer year : wordFreq.keySet()) {
            if (totalCount.containsKey(year)) {
                double wordFreqValue = wordFreq.get(year);
                double totalCountValue = totalCount.get(year);

                if (totalCountValue != 0) {
                    result.put(year, wordFreqValue / totalCountValue);
                } else {
                    throw new NullPointerException(word + "is Null");
                }
            }
        }
        return result;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            TimeSeries wordWeightHistory = weightHistory(word, startYear, endYear);
            wordWeightHistory.forEach((year, value) -> result.merge(year, value, Double::sum));
        }
        return result;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            TimeSeries wordWeightHistory = weightHistory(word);
            wordWeightHistory.forEach((year, value) -> result.merge(year, value, Double::sum));
        }
        return result;
    }

    @Override
    public String toString() {
        return "test test ngrammap";
    }

    public static void main(String[] args) {

        NGramMap ngm = new NGramMap("./data/ngrams/very_short.csv", "./data/ngrams/total_counts.csv");
        TimeSeries request2006to2007 = ngm.countHistory("request", 2006, 2007);
        System.out.println(request2006to2007);
//        In countsFile = new In("./data/ngrams/total_counts.csv");
//
//        ngm.readCountsFile(countsFile);
//
//        ngm.printCountsResult();
    }
}
