package ngordnet.main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    //wrapper for a graph
    private Graph graph = new Graph();
    private String hyponymsFileName;
    private String synsetsFileName;
    //private Map<String, Integer> synsets = new HashMap<>();

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

    public String getWordById(int id) {
        for (Map.Entry<Integer, String> entry : synsets.entrySet()) {
            if (Objects.equals(id, entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    //Look up a word (e.g. “change”), what nodes contain that word?
    //Example in synsets16.txt: change is in synsets 2 and 8
    public List<Integer> lookupWord(String word) {
        ArrayList<Integer> idContainsWord = new ArrayList<>();

        for (Map.Entry<Integer, String> entry : synsets.entrySet()) {
            if (entry.getValue().contains(word) || entry.getValue().equals(word)) {
                idContainsWord.add(entry.getKey());
            }
        }
        return idContainsWord;
    }

    public Map<Integer, String> getSynsets() {
        return synsets;
    }

    public String returnWord(int id) {
        return synsets.get(id);
    }

    // TODO clean up
    private TreeSet extracted(String word) {
        List<Integer> input = lookupWord(word);;
        List<Integer> n = new ArrayList<>();
        List<String> output = new ArrayList<>();
        List<String> outputList = new ArrayList<>();
        TreeSet hSet = new TreeSet();
        Graph g = getGraph();

        for (int i : input) {
            for (int j : g.findReachableVertices(i)) {
                n.add(j);
            }
        }
        for (int m : n) {
            output.add(returnWord(m));
        }
        for (String w : output) {
            for (String v : w.split(" ")) {
                // System.out.println(v);
                outputList.add(v);
                hSet.addAll(outputList);
            }
        }
        return hSet;
    }


    public static void main(String[] args) {
        WordNet wn = new WordNet("./data/wordnet/hyponyms16.txt", "./data/wordnet/synsets16.txt");
        Graph g = wn.getGraph();

        Map<Integer, String> s = wn.getSynsets();
        System.out.println("Synsets: \n" + s);
        System.out.println("Graph: ");
        g.printGraph();

        System.out.println("---------------");
//        System.out.println("get word by id: " + wn.getWordById(2));
////        //System.out.println("get neighborsWord : " + wn.getNeighborsWord("change"));
//        System.out.println(wn.lookupWord("change"));
//        System.out.println("reachable vertices " + g.findReachableVertices(2));

        System.out.println(wn.extracted("change"));

    }
}