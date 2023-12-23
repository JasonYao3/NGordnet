package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;

import java.util.List;

public class HyponymsHandler extends NgordnetQueryHandler{
    /*
    Assume that the “words” entered is only a single word.
    Ignore startYear, endYear, and k.
    Return a string representation of a list of the hyponyms of the single word, including the word itself.
    The list should be in alphabetical order, with no repeated words.
     */

    private WordNet wnet;

    public HyponymsHandler(WordNet wnet) {
        this.wnet = wnet;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        String response = "";

        response += wnet.getReachableWords(words);

        return response;
    }
}
