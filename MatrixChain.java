public class MatrixChain {

    // Module 3: Matrix Chain Multiplication
    // Time: O(n^3)

    public static long matrixChain(int[] p) {

        int n = p.length - 1;

        long[][] dp =
            new long[n][n];

        for (int length = 2;
             length <= n;
             length++) {

            for (int i = 0;
                 i + length - 1 < n;
                 i++) {

                int j =
                    i + length - 1;

                dp[i][j] =
                    Long.MAX_VALUE;

                for (int k = i;
                     k < j;
                     k++) {

                    long cost =
                        dp[i][k] +
                        dp[k + 1][j] +
                        (long)p[i] *
                        p[k + 1] *
                        p[j];

                    if (cost < dp[i][j])
                        dp[i][j] = cost;
                }
            }
        }

        return dp[0][n - 1];
    }

    // Small Bitmask-DP TSP demonstration.
    public static long tspDemo() {

        int[][] cost = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };

        int n = cost.length;
        int total = 1 << n;

        long INF = Long.MAX_VALUE / 4;

        long[][] dp =
            new long[total][n];

        for (int mask = 0;
             mask < total;
             mask++) {

            for (int i = 0;
                 i < n;
                 i++) {

                dp[mask][i] = INF;
            }
        }

        dp[1][0] = 0;

        for (int mask = 1;
             mask < total;
             mask++) {

            for (int u = 0;
                 u < n;
                 u++) {

                if (dp[mask][u] == INF)
                    continue;

                for (int v = 0;
                     v < n;
                     v++) {

                    if ((mask & (1 << v)) == 0) {

                        int newMask =
                            mask | (1 << v);

                        long value =
                            dp[mask][u] +
                            cost[u][v];

                        if (value <
                            dp[newMask][v]) {

                            dp[newMask][v] =
                                value;
                        }
                    }
                }
            }
        }

        long answer = INF;
        int all = total - 1;

        for (int i = 0; i < n; i++) {

            answer =
                Math.min(
                    answer,
                    dp[all][i] +
                    cost[i][0]
                );
        }

        return answer;
    }
}
