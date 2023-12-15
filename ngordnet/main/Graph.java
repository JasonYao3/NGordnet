package ngordnet.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Graph {
    //adjList, adjMatrix
    //adjList - hashmap
    private Map<Vertex, List<Vertex>> adjList;

    public Graph(Map<Vertex, List<Vertex>> adjList) {
        this.adjList = adjList;
    }

    public void addVertex(String label) {
        adjList.putIfAbsent(new Vertex(label), new ArrayList<>());
    }

    public void addEdge(String label1, String label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        adjList.get(v1).add(v2);
        adjList.get(v2).add(v1);
    }

    public Graph createGraph() {
        Graph graph = new Graph();
    }

    public List<Vertex> getAdjList(String label) {
        return adjList.get(new Vertex(label));
    }
}
