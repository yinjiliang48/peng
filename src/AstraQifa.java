package tuopusort;

import java.util.LinkedList;
import java.util.List;

public class AstraQifa {

    private static Graph graph;

    private static Point[] points;

    private static class Graph {
        public int v;
        public List<Edge> adj[];

        public Graph(int v) {
            this.v = v;
            adj = new LinkedList[v + 1];
            for (int i = 1; i < v + 1; i++) {
                adj[i] = new LinkedList<>();
            }

            points = new Point[v + 1];
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

        // 评估函数值
        public int f;

        public int x;
        public int y;

        public Point(int id, int dist, int x, int y) {
            this.id = id;
            this.dist = dist;
            this.x = x;
            this.y = y;
        }

        public Point(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.dist = Integer.MAX_VALUE;
            this.f = Integer.MAX_VALUE;
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

            while (i / 2 > 0) {
                if (arr[i].f < arr[i / 2].f) {
                    swap(arr, i, i / 2);
                    i = i / 2;
                } else {
                    break;
                }
            }
        }

        public Point poll() {
            Point result = arr[1];

            arr[1] = new Point(arr[count].id, arr[count].dist, arr[count].x, arr[count].y);
            arr[1].f = arr[count].f;
            arr[count] = new Point(0, 0, 0, 0);
            count--;

            int i = 1;
            while (i < count) {
                int minPos = i;
                if (2 * i < count && arr[i].f > arr[2 * i].f) {
                    minPos = 2 * i;
                }

                if (2 * i + 1 < count && arr[i].f > arr[2 * i + 1].f) {
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
            Point tmp = new Point(arr[i].id, arr[i].dist, arr[i].x, arr[i].y);
            tmp.f = arr[i].f;

            arr[i].id = arr[minPos].id;
            arr[i].dist = arr[minPos].dist;
            arr[i].f = arr[minPos].f;
            arr[i].x = arr[minPos].x;
            arr[i].y = arr[minPos].y;

            arr[minPos].id = tmp.id;
            arr[minPos].dist = tmp.dist;
            arr[minPos].f = tmp.f;
            arr[minPos].x = tmp.x;
            arr[minPos].y = tmp.y;
        }

        public boolean isEmpty() {
            return count == 0;
        }

        public void update(Point nextPoint) {
            for (int i = 1; i <= count; i++) {
                if (arr[i].id == nextPoint.id) {
                    arr[i].f = nextPoint.f;
                    arr[i].dist =nextPoint.dist;
                    arr[i].x = nextPoint.x;
                    arr[i].y = nextPoint.y;
                    while (i / 2 > 0) {
                        if (arr[i].f < arr[i / 2].f) {
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

        public void clear() {
            count = 0;

        }
    }

    private static void addPoint(int id, int x, int y) {
        points[id] = new Point(id, x, y);
    }


    public void astra(int s, int t) {
        int v = graph.v;
        List<Edge> adj[] = graph.adj;
        PriorityQueue queue = new PriorityQueue(v + 1);
        int procedureArr[] = new int[v + 1];

        boolean inqueueFlag[] = new boolean[v + 1];
        points[s].dist = 0;
        points[s].f = 0;
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

                    // 这里很关键！！！
                    nextPoint.f = nextPoint.dist + hManhattanDistance(nextPoint, points[t]);
                    procedureArr[nextPoint.id] = minPoint.id;

                    if (inqueueFlag[nextPoint.id] == true) {
                        queue.update(nextPoint);
                    } else {
                        queue.add(nextPoint);

                        inqueueFlag[nextPoint.id] = true;
                    }

                }

                // 只要找出一个是目标即可
                if (nextPoint.id == t ){
                    queue.clear();
                    break;
                }
            }
        }

        System.out.print(s + "-->");
        printOthers(s, t, procedureArr);
    }

    private int hManhattanDistance(Point nextPoint, Point point) {
        return Math.abs(nextPoint.x - point.x) + Math.abs(nextPoint.y - point.y);
    }

    private void printOthers(int s, int t, int procedureArr[]) {
        if (s == t) {
            return;
        }
        printOthers(s, procedureArr[t], procedureArr);

        System.out.print(t + "-->");
    }

    public static void main(String[] argv) {
        graph = new Graph(14);

        graph.addEdge(1, 2, 20);
        graph.addEdge(2, 3, 20);

        graph.addEdge(3, 4, 10);

        graph.addEdge(4, 13, 40);
        graph.addEdge(4, 14, 30);

        graph.addEdge(14, 7, 50);
        graph.addEdge(13, 5, 40);

        graph.addEdge(1, 5, 60);
        graph.addEdge(1, 7, 60);

        graph.addEdge(7, 8, 70);
        graph.addEdge(8, 12, 50);

        graph.addEdge(12, 11, 60);

        graph.addEdge(1, 6, 60);
        graph.addEdge(6, 11, 50);

        graph.addEdge(5, 9, 50);
        graph.addEdge(9, 10, 50
        );
        graph.addEdge(10, 11, 60);
        graph.addEdge(6, 10, 80);

        addPoint(1, 0, 0);
        addPoint(2, -20, 0);
        addPoint(3, -40, 0);
        addPoint(4, -50, 0);

        addPoint(5, 0, 60);
        addPoint(6, 60, 0);

        addPoint(7, 0, -60);

        addPoint(8, 70, -60);

        addPoint(9, 60, 70);

        addPoint(10, 110, 60);

        addPoint(11, 110, 0);

        addPoint(12, 120, -60);

        addPoint(13, -50, 40);

        addPoint(14, -50, -30);


        new AstraQifa().astra(1, 11);
    }
}
