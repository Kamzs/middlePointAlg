package com.company;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    int currentResult = 1000;

    public static void main(String args[]) throws FileNotFoundException {

        System.setIn(new FileInputStream("src/com/company/input"));
        Scanner s = new Scanner(System.in);

        int NO_CASES = s.nextInt();

        for (int a = 0; a < NO_CASES; a++) {
            long t1 = System.currentTimeMillis();

            int SizeOfMap = s.nextInt();
            int noOfPointsWithRareEl = s.nextInt();

            // szer tj. x wspol druga tablica
            int[][] pointsMatrix = new int[noOfPointsWithRareEl][2];

            for (int b = 0; b < noOfPointsWithRareEl; b++) {
                for (int c = 0; c < 2; c++) {
                    pointsMatrix[b][c] = s.nextInt() - 1;
                }
            }
            int[][] map = new int[SizeOfMap][SizeOfMap];
            for (int d = 0; d < SizeOfMap; d++) {
                for (int e = 0; e < SizeOfMap; e++) {
                    map[d][e] = s.nextInt();
                }
            }
            int[][] mapWithFieldsNumbers = new int[SizeOfMap][SizeOfMap];
            int count = 0;
            for (int i = 0; i < SizeOfMap; i++) {
                for (int j = 0; j < SizeOfMap; j++) {
                    mapWithFieldsNumbers[i][j] = count++;
                }
            }

            System.out.println(Arrays.deepToString(pointsMatrix));
            System.out.println(Arrays.deepToString(map));
            System.out.println(Arrays.deepToString(mapWithFieldsNumbers));

            Main main = new Main();

            Graph graph = main.createGraph(mapWithFieldsNumbers, map, SizeOfMap, SizeOfMap);
            System.out.println(graph);

            int resultYcordinate =0;
            int resultXcordinate =0;

            for (int m = 0; m < SizeOfMap; m++) {
                for (int n = 0; n < SizeOfMap; n++) {

                    if (map[m][n] == 1) {

                        int longestPathToThePoint = -1000;
                        int[] resultsForASinglePoint = new int[noOfPointsWithRareEl];

                        for(int r=0; r < noOfPointsWithRareEl; r++) {

                            resultsForASinglePoint[r] = main.BFS(
                                    mapWithFieldsNumbers[pointsMatrix[r][0]][pointsMatrix[r][1]],
                                    mapWithFieldsNumbers[m][n],
                                    graph);

                        }
                        for(int o =0; o < resultsForASinglePoint.length; o ++) {
                            if (resultsForASinglePoint[o] > longestPathToThePoint) longestPathToThePoint = resultsForASinglePoint[o];
                        }
                        if(longestPathToThePoint < main.currentResult) {
                            main.currentResult = longestPathToThePoint;
                            resultYcordinate = m;
                            resultXcordinate = n;

                        }
                    }

                }

            }

            System.out.println(main.currentResult);
            long t2 = System.currentTimeMillis();
            System.out.println(" || runtime was : " + (t2 - t1) + " ms || Y = " + resultYcordinate + " X = " + resultXcordinate);

        }
    }

    class ListFifo {
        int SIZE = 1;
        int index_of_first = 0;
        int index_of_new = 0;

        Object[] table = new Object[SIZE];

        Object pop_front() {
            if (index_of_first < index_of_new) {
                index_of_first++;
                return table[index_of_first - 1];
            } else
                return null;
        }

        void add_back(Object o) {
            if (index_of_new < SIZE) {
                table[index_of_new] = o;
                index_of_new++;
            } else {
                make_bigger();
                table[index_of_new] = o;
                index_of_new++;
            }

        }

        void make_bigger() {
            SIZE *= 2;
            Object[] newTable = new Object[SIZE];
            for (int f = 0; f < table.length; f++) {
                newTable[f] = table[f];
            }
            table = newTable;

        }

        Object getByIndex(int index) {
            if (index < index_of_new && index >= index_of_first) {
                return table[index];
            } else
                return null;
        }

        int length() {
            return index_of_new - index_of_first;
        }

        @Override
        public String toString() {
            return "ListFifo [SIZE=" + SIZE + ", index_of_first=" + index_of_first + ", index_of_new=" + index_of_new
                    + ", table=" + Arrays.toString(table) + "]";
        }

    }

    class Graph {

        int NO_OF_NODES;
        ListFifo[] edges;

        public Graph(int NO_OF_NODES) {
            this.NO_OF_NODES = NO_OF_NODES;
            this.edges = new ListFifo[NO_OF_NODES];
            for (int g = 0; g < NO_OF_NODES; g++) {
                edges[g] = new ListFifo();
            }
        }

        void addEdge(int from, int to) {
            edges[from].add_back(to);
        }

        @Override
        public String toString() {
            return "Graph [NO_OF_NODES=" + NO_OF_NODES + ", edges=" + Arrays.toString(edges) + "]";
        }

    }

    private Graph createGraph(int[][] nodesNumerated, int[][] map, int wysok, int szer) {
        Graph graph = new Graph(wysok * szer);

        for (int k = 0; k < wysok; k++) {
            for (int l = 0; l < szer; l++) {

                if (k >= 1 && l >= 1 && k < wysok - 1 && l < szer - 1) {
                    if (map[k][l] == 1 && map[k][l - 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l - 1]);
                    }
                    if (map[k][l] == 1 && map[k][l + 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l + 1]);
                    }
                    if (map[k][l] == 1 && map[k - 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k - 1][l]);
                    }
                    if (map[k][l] == 1 && map[k + 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k + 1][l]);
                    }
                }
                if (k == 0 && l >= 1 && l < szer - 1) {
                    if (map[k][l] == 1 && map[k][l - 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l - 1]);
                    }
                    if (map[k][l] == 1 && map[k][l + 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l + 1]);
                    }
                    if (map[k][l] == 1 && map[k + 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k + 1][l]);
                    }
                }

                if (k == wysok - 1 && l >= 1 && l < szer - 1) {
                    if (map[k][l] == 1 && map[k][l - 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l - 1]);
                    }
                    if (map[k][l] == 1 && map[k][l + 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l + 1]);
                    }
                    if (map[k][l] == 1 && map[k - 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k - 1][l]);
                    }
                }
                if (l == 0 && k >= 1 && k < wysok - 1) {
                    if (map[k][l] == 1 && map[k][l + 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l + 1]);
                    }
                    if (map[k][l] == 1 && map[k - 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k - 1][l]);
                    }
                    if (map[k][l] == 1 && map[k + 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k + 1][l]);
                    }
                }
                if (l == szer - 1 && k >= 1 && k < wysok - 1) {
                    if (map[k][l] == 1 && map[k][l - 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l - 1]);
                    }
                    if (map[k][l] == 1 && map[k - 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k - 1][l]);
                    }
                    if (map[k][l] == 1 && map[k + 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k + 1][l]);
                    }
                }

                if (l == 0 && k == 0) {
                    if (map[k][l] == 1 && map[k][l + 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l + 1]);
                    }
                    if (map[k][l] == 1 && map[k + 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k + 1][l]);
                    }
                }

                if (l == 0 && k == wysok - 1) {
                    if (map[k][l] == 1 && map[k][l + 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l + 1]);
                    }
                    if (map[k][l] == 1 && map[k - 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k - 1][l]);
                    }
                }

                if (l == szer - 1 && k == wysok - 1) {
                    if (map[k][l] == 1 && map[k][l - 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l - 1]);
                    }
                    if (map[k][l] == 1 && map[k - 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k - 1][l]);
                    }
                }
                if (l == szer - 1 && k == 0) {
                    if (map[k][l] == 1 && map[k][l - 1] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k][l - 1]);
                    }
                    if (map[k][l] == 1 && map[k + 1][l] == 1) {
                        graph.addEdge(nodesNumerated[k][l], nodesNumerated[k + 1][l]);
                    }
                }
            }

        }
        return graph;

    }

    int BFS(int start, int end, Graph graph) {

        int[] resultsToPoints = new int[graph.NO_OF_NODES];
        boolean[] visitedPoints = new boolean[graph.NO_OF_NODES];

        ListFifo listaNodow = new ListFifo();

        listaNodow.add_back(start);
        resultsToPoints[start] = 0;
        visitedPoints[start] = false;
        while (listaNodow.length() > 0) {

            int currentNode = (int) listaNodow.pop_front();
            int nextNode;
            if (visitedPoints[currentNode] == true)
                continue;
            {
                for (int h = 0; h < graph.edges[currentNode].length(); h++) {

                    nextNode = (int) graph.edges[currentNode].getByIndex(h);

                    resultsToPoints[nextNode] = resultsToPoints[currentNode] + 1;
                    if (nextNode == end)
                        return resultsToPoints[nextNode];

                    listaNodow.add_back(nextNode);

                }
                visitedPoints[currentNode] = true;

            }

        }
        return 1000;

    }

}
