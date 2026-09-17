ublic class Dinic {

    // Module 4: Dinic's Maximum Flow Algorithm
    // Complexity: O(V^2 E) general bound.

    private int n;
    private int[][] capacity;
    private int[] level;
    private int[] pointer;
    private int[] queue;

    public Dinic(int n) {

        this.n = n;
        capacity = new int[n][n];
        level = new int[n];
        pointer = new int[n];
        queue = new int[n];
    }

    public void addEdge(int from, int to, int cap) {
        capacity[from][to] += cap;
    }

    private boolean bfs(int source, int sink) {

        for (int i = 0; i < n; i++)
            level[i] = -1;

        int head = 0;
        int tail = 0;

        queue[tail++] = source;
        level[source] = 0;

        while (head < tail) {

            int u = queue[head++];

            for (int v = 0; v < n; v++) {

                if (capacity[u][v] > 0 &&
                    level[v] == -1) {

                    level[v] =
                        level[u] + 1;

                    queue[tail++] = v;
                }
            }
        }

        return level[sink] != -1;
    }

    private int dfs(
            int u,
            int sink,
            int pushed) {

        if (u == sink || pushed == 0)
            return pushed;

        while (pointer[u] < n) {

            int v =
                pointer[u];

            if (level[v] ==
                    level[u] + 1 &&
                capacity[u][v] > 0) {

                int flow =
                    dfs(
                        v,
                        sink,
                        Math.min(
                            pushed,
                            capacity[u][v]
                        )
                    );

                if (flow > 0) {

                    capacity[u][v] -= flow;
                    capacity[v][u] += flow;

                    return flow;
                }
            }

            pointer[u]++;
        }

        return 0;
    }

    public int maxFlow(
            int source,
            int sink) {

        int flow = 0;

        while (bfs(source, sink)) {

            for (int i = 0; i < n; i++)
                pointer[i] = 0;

            int pushed;

            while ((pushed =
                    dfs(
                        source,
                        sink,
                        Integer.MAX_VALUE
                    )) > 0) {

                flow += pushed;
            }
        }

        return flow;
    }
}
