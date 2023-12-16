package ngordnet.main;

import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class WordNet  {
    //wrapper for a graph
    private Graph graph = new Graph();
    private String hyponymsFileName;
    private String synsetsFileName;
    private Map<Integer, String> synsets = new HashMap<>();

    public WordNet(String hyponymsFileName, String synsetsFileName) {

        this.hyponymsFileName = hyponymsFileName;
        // read from wordnet files
        In hyponymsFile = new In(this.hyponymsFileName);
        readHyponmsFile(hyponymsFile);

        this.synsetsFileName = synsetsFileName;
        In synsetsFile = new In(this.synsetsFileName);
        readSynsetsFile(synsetsFile);
    }

    private void readHyponmsFile(In hyponymsFile) {

        while (hyponymsFile.hasNextLine()) {
            String line = hyponymsFile.readLine();
            String[] tokens = line.split(",");

            if (tokens.length > 0) {
                int hypernym = Integer.parseInt(tokens[0]);

                LinkedList<String> hyponyms = new LinkedList<>();
                for (int i = 1; i < tokens.length; i++) {
                    hyponyms.add(tokens[i]);
                }

                for (int i = 0; i < hyponyms.size(); i++) {
                    graph.addEdge(hypernym, Integer.valueOf(hyponyms.get(i)));
                }
            }
        }
        hyponymsFile.close();
    }

    private void readSynsetsFile(In synsetsFile) {
        while (synsetsFile.hasNextLine()) {
            String line = synsetsFile.readLine();
            String[] tokens = line.split(",");

            if (tokens.length > 0) {
                int id = Integer.parseInt(tokens[0]);

                String word = tokens[1];

                synsets.put(id, word);
            }
        }
    }

    public Graph getGraph() {
        return graph;
    }


    public Map<Integer, String> getSynsets() {
        return synsets;
    }

    //TODO This class should also be able to take a word and return its hyponyms
    // loop through valueSet in Synsets, if word == valueSet, then return key


    public static void main(String[] args) {
        WordNet wn= new WordNet("./data/wordnet/hyponyms16.txt", "./data/wordnet/synsets16.txt");
        Graph g = wn.getGraph();

        Map<Integer, String> s = wn.getSynsets();
        System.out.println("Synsets: \n" + s);
        System.out.println("Graph: ");
        g.printGraph();
    }
}
