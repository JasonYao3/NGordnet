package ngordnet.main;

//public class Graph {
//    private int vertex;
//    private LinkedList<Integer>[] list;
//
//    public Graph(int vertex) {
//        this.vertex = vertex;
//        list = new LinkedList[vertex];
//        for (int i = 0; i < vertex; i++) {
//            list[i] = new LinkedList<>();
//        }
//    }
//
//    public void addEdge(int v, int w) {
//
//        //add edge
//        list[v].addFirst(w);
//    }
//
//    public void printGraph(){
//        for (int i = 0; i <vertex ; i++) {
//            if(list[i].size()>0) {
//                System.out.print("Vertex " + i + " is connected to: ");
//                for (int j = 0; j < list[i].size(); j++) {
//                    System.out.print(list[i].get(j) + " ");
//                }
//                System.out.println();
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        Graph graph = new Graph(5);
//        graph.addEdge(0,1);
//        graph.addEdge(0, 4);
//        graph.addEdge(1, 2);
//        graph.addEdge(1, 3);
//        graph.addEdge(1, 4);
//        graph.addEdge(2, 3);
//        graph.addEdge(3, 4);
//        graph.printGraph();
//    }
//}

import java.util.*;

public class Graph {
    private Map<Integer, LinkedList<Integer>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addEdge(int v, int w) {
        // Add edge v -> w
        adjacencyList.computeIfAbsent(v, k -> new LinkedList<>()).add(w);

        // Uncomment the following line if you want the graph to be undirected
        // adjacencyList.computeIfAbsent(w, k -> new LinkedList<>()).add(v);
    }

    public List<Integer> getNeighbors(int v) {
        return adjacencyList.getOrDefault(v, new LinkedList<>());
    }

    //TODO Write code that takes a word, and uses a graph traversal to find all hyponyms of that word in the given graph.
    // find reachable vertices
    // dfs or bfs?
    // pass in v = 0; return list = [1, 2]
    public List<Integer> findReachableVertices(int v) {
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> visited = new ArrayList<>();

        queue.add(v);
        visited.add(v);

        while (!queue.isEmpty()) {
            int front = queue.poll();
            for (int n : getNeighbors(front)) {
                if (!visited.contains(n)) {
                    queue.add(n);
                    visited.add(n);
                }
            }
        }
        return visited;
    }

    public void printGraph() {
        for (Map.Entry<Integer, LinkedList<Integer>> entry : adjacencyList.entrySet()) {
            int vertex = entry.getKey();
            LinkedList<Integer> neighbors = entry.getValue();

            System.out.print("Vertex " + vertex + " is connected to: ");
            for (int neighbor : neighbors) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }

    public Map<Integer, LinkedList<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(3,4);
        graph.addEdge(5,6);
        graph.addEdge(5,7);
        graph.addEdge(8,10);
        graph.addEdge(9,10);

        graph.printGraph();
        int vertexToGetNeighbors = 0;
        List<Integer> neighbors = graph.getNeighbors(vertexToGetNeighbors);

        System.out.println("Neighbors of vertex " + vertexToGetNeighbors + ": " + neighbors);
        System.out.println("find reachable vertices (bfs) " + graph.findReachableVertices(0));
        graph.findReachableVertices(0);
    }
}
