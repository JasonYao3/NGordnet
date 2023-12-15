package ngordnet.main;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class WordNet  {
    private Graph graph;
    //wrapper for a graph


    public WordNet(String hyponymsFileName, String synsetsFileName) {
        // build the graph -> add all the edges
        // get edges from wordnet 11.txt

        // read from wordnet files
        In hyponymsFile = new In(hyponymsFileName);
        readHyponmsFile(hyponymsFile);
        //In synsetsFile = new In(synsetsFileName);

        // build a graph
        //graph = new Graph();

    }
    private void readHyponmsFile(In hyponymsFile) {
        while (hyponymsFile.hasNextLine()) {
            String line = hyponymsFile.readLine();
            String[] tokens = line.split(",");

            if (tokens.length > 0) {

                int hypernym = Integer.parseInt(tokens[0]);
                ArrayList<Integer> hyponyms = new ArrayList<>();

                for (int i = 1; i < tokens.length; i++) {
                    graph.addEdge(hypernym, i);
                    //hyponyms.add(Integer.parseInt(tokens[i]));
                }
                System.out.println("Hypernym: " + hypernym);
                System.out.println("Hyponyms: " + hyponyms);
            }
        }

        hyponymsFile.close();
    }

    public static void main(String[] args) {
        WordNet wn= new WordNet("./data/wordnet/synsets11.txt","./data/wordnet/hyponyms11.txt");

    }

    // graph helper functions
}
