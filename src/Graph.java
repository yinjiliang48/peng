package tuopusort;

import java.util.LinkedList;

/**
 * 拓扑排序20220724
 */
public class Graph {
    // 顶点个数
    private int v;

    // 邻接表
    private LinkedList<Integer> adj[];

    public Graph(int v) {
        this.v = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    public void addEdge(int s, int t) {
        adj[s].add(t);
    }

    public void topoSortByKahn() {
        int[] inDegree = new int[v];

        for (int i = 0; i < v; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                int w = adj[i].get(j); // i -> w
                inDegree[w]++; // w -> i
            }
        }

        LinkedList<Integer> queue = new LinkedList<>();

        for (int i = 0; i < v; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int i = queue.remove();

            System.out.println("-->" + i);

            for (int j = 0; j < adj[i].size(); j++) {
                int k = adj[i].get(j);

                inDegree[k]--;
                if (inDegree[k] == 0) {
                    queue.add(k);
                }
            }
        }
    }

    public void topoSortByDFS() {
        // 先构建逆邻接表，边s->t表示，s依赖于t，t先于s
        LinkedList<Integer> inverseAdj[] = new LinkedList[this.v];

        for (int i = 0; i < v; i++) {
            inverseAdj[i] = new LinkedList<>();
        }

        //根据邻接表，构建逆邻接表
        for (int i = 0; i < v; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                int w = adj[i].get(j); // i -> w
                inverseAdj[w].add(i); // w -> i
            }
        }

        boolean[] visited = new boolean[v];

        for (int i = 0; i < v; i++) {
            if (visited[i] == false) {
                visited[i] = true;
                dfs(i, inverseAdj, visited);
            }
        }
    }

    private void dfs(int vertex, LinkedList<Integer>[] inverseAdj, boolean[] visited) {
        for (int i = 0; i < inverseAdj[vertex].size(); i++) {
            int w = inverseAdj[vertex].get(i);
            if (visited[w] == true) {
                continue;
            }
            visited[w] = true;

            dfs(w, inverseAdj, visited);
        }
        System.out.println(("->" + vertex));
    }


    public static void main(String[] argv) {
        Graph graph = new Graph(4);

        // 0,依赖于1
        graph.addEdge(0, 1);
        // 1,依赖于2
        graph.addEdge(1, 2);
        // 3,依赖于1
        graph.addEdge(3, 1);

        // 最后排序结果：2->1->0->3 或者2->1->3->0
//        graph.topoSortByDFS();

        graph.topoSortByKahn();
    }
}
