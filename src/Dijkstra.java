package tuopusort;

import java.util.LinkedList;
import java.util.List;

public class Dijkstra {

    private static Graph graph;

    private static class Graph {
        public int v;
        public List<Edge> adj[];

        public Graph(int v) {
            this.v = v;
            adj = new LinkedList[v + 1];
            for (int i = 1; i < v + 1; i++) {
                adj[i] = new LinkedList<>();
            }
        }

        public void addEdge(int s, int t, int w) {
            adj[s].add(new Edge(s, t, w));
        }
    }

    private static class Edge {
        public int s;
        public int t;
        public int w;

        public Edge(int s, int t, int w) {
            this.s = s;
            this.t = t;
            this.w = w;
        }
    }

    private static class Point {
        public int id;
        public int dist;

        public Point(int id, int dist) {
            this.id = id;
            this.dist = dist;
        }
    }

    private static class PriorityQueue {
        public int capacity;
        public int count;
        private Point arr[];

        public PriorityQueue(int capacity) {
            this.capacity = capacity;
            count = 0;
            arr = new Point[capacity + 1];
        }

        public void add(Point point) {
            count++;
            arr[count] = point;

            int i = count;

            while (i/2 > 0) {
                if (arr[i].dist < arr[i / 2].dist) {
                    swap(arr, i, i / 2);
                    i = i / 2;
                } else {
                    break;
                }
            }
        }

        public Point poll() {
            Point result = arr[1];

            arr[1] = new Point(arr[count].id,arr[count].dist);
            arr[count] = new Point(0,0);
            count--;

            int i = 1;
            while (i < count) {
                int minPos = i;
                if (2 * i < count && arr[i].dist > arr[2 * i].dist) {
                    minPos = 2 * i;
                }

                if (2 * i + 1 < count && arr[i].dist > arr[2 * i + 1].dist) {
                    minPos = 2 * i + 1;
                }
                if (i == minPos) {
                    break;
                }
                swap(arr, i, minPos);

                i = minPos;
            }


            return result;
        }

        private void swap(Point[] arr, int i, int minPos) {
            Point tmp = new Point(arr[i].id, arr[i].dist);

            arr[i].id = arr[minPos].id;
            arr[i].dist = arr[minPos].dist;

            arr[minPos].id = tmp.id;
            arr[minPos].dist = tmp.dist;
        }

        public boolean isEmpty() {
            return count == 0;
        }

        public void update(Point nextPoint) {
            for (int i=1;i<=count;i++){
                if (arr[i].id == nextPoint.id){
                    arr[i].dist = nextPoint.dist;

                    while (i/2 > 0) {
                        if (arr[i].dist < arr[i / 2].dist) {
                            swap(arr, i, i / 2);
                            i = i / 2;
                        } else {
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }


    public void dijkstra(int s, int t) {
        int v = graph.v;
        List<Edge> adj[] = graph.adj;
        PriorityQueue queue = new PriorityQueue(v + 1);
        int procedureArr[] = new int[v + 1];

        Point[] points = new Point[v + 1];
        for (int i = 1; i < v + 1; i++) {
            points[i] = new Point(i, Integer.MAX_VALUE);
        }
        boolean inqueueFlag[] = new boolean[v + 1];
        points[s].dist = 0;
        queue.add(points[s]);
        inqueueFlag[s] = true;

        while (!queue.isEmpty()) {
            Point minPoint = queue.poll();

            if (minPoint.id == t) {
                break;
            }
            int pos = minPoint.id;
            int size = adj[pos].size();

            for (int i = 0; i < size; i++) {
                Point nextPoint = points[adj[pos].get(i).t];
                if (minPoint.dist + adj[pos].get(i).w < nextPoint.dist) {
                    nextPoint.dist = minPoint.dist + adj[pos].get(i).w;

                    procedureArr[nextPoint.id] = minPoint.id;

                    if (inqueueFlag[nextPoint.id] == true) {
                        queue.update(nextPoint);
                    } else {
                        queue.add(nextPoint);

                        inqueueFlag[nextPoint.id] = true;
                    }

                }
            }
        }

        System.out.print(s + "-->");

        printOthers(s, t, procedureArr);


    }

    private void printOthers(int s, int t, int procedureArr[]) {
        if (s == t) {
            return;
        }
        printOthers(s, procedureArr[t], procedureArr);

        System.out.print(t + "-->");
    }

    public static void main(String[] argv) {
        graph = new Graph(6);

        graph.addEdge(1, 2, 10);
        graph.addEdge(1, 5, 15);

        graph.addEdge(2, 3, 2);
        graph.addEdge(2, 4, 5);

        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 6, 5);

        graph.addEdge(5, 6, 9);

        new Dijkstra().dijkstra(1, 6);
    }
}
