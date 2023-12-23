package ngordnet.main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
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

    private List<Integer> findSynsetIdsByWord(String word) {
        ArrayList<Integer> synsetIdsContainingWord = new ArrayList<>();
        for (Map.Entry<Integer, String> synsetEntry : synsets.entrySet()) {
            String synsetValue = synsetEntry.getValue();
            String[] wordsInSynset = synsetValue.split(" ");

            if (Arrays.asList(wordsInSynset).contains(word)) {
                synsetIdsContainingWord.add(synsetEntry.getKey());
            }


        }
        return synsetIdsContainingWord;
    }

    public Map<Integer, String> getSynsets() {
        return synsets;
    }

    private List<Integer> findHyponymsIds(String word) {
        List<Integer> synsetIdsContainingWord  = findSynsetIdsByWord(word);
        List<Integer> hyponymIds  = new ArrayList<>();
        for (int i : synsetIdsContainingWord ) {
            hyponymIds.addAll(getGraph().findReachableVertices(i));
        }
        return hyponymIds;
    }

    public TreeSet<String> getReachableWords(List<String> words) {
        List<List<Integer>> hyponymsLists = new ArrayList<>();
        TreeSet<String> reachableWordSet = new TreeSet<>();
        for (String word : words) {
            List<Integer> hyponymIds = findHyponymsIds(word);
            hyponymsLists.add(hyponymIds);
        }

        List<Integer> commonHyponymIds = new ArrayList<>(hyponymsLists.get(0));
        for (List<Integer> list : hyponymsLists) {
            commonHyponymIds.retainAll(list);
        }
        for (int id : commonHyponymIds) {
            reachableWordSet.addAll(List.of(synsets.get(id).split(" ")));
        }
        return reachableWordSet;
    }


//    public static void main(String[] args) {
//        WordNet wn = new WordNet("./data/wordnet/hyponyms.txt",
//                "./data/wordnet/synsets.txt");
//        Graph g = wn.getGraph();
//
//        Map<Integer, String> s = wn.getSynsets();
//        System.out.println("Synsets: \n" + s);
//        System.out.println("Graph: ");
//        g.printGraph();
//
//        System.out.println("---------------");
//        //System.out.println(wn.findWordIds("action"));
//
//        List<String> stringList = new ArrayList<>();
//        stringList.add("pastry");
//        stringList.add("tart");
//
//        System.out.println("word is "  + wn.findSynsetIdsByWord("pastry"));
//        System.out.println("word is "  + wn.findSynsetIdsByWord("tart"));
//
//        System.out.println(wn.findHyponymsIds("pastry"));
//        System.out.println(wn.findHyponymsIds("tart"));
//
//        System.out.println(wn.getReachableWords(stringList));
//
//    }
}