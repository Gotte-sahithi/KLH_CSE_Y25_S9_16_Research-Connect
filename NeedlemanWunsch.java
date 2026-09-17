public class NeedlemanWunsch {

    // Module 3: Global sequence alignment.
    // Time: O(n*m)

    public static int align(
            String a,
            String b) {

        int[][] dp =
            new int[a.length() + 1]
                    [b.length() + 1];

        int gap = -2;
        int match = 2;
        int mismatch = -1;

        for (int i = 1;
             i <= a.length();
             i++) {

            dp[i][0] = i * gap;
        }

        for (int j = 1;
             j <= b.length();
             j++) {

            dp[0][j] = j * gap;
        }

        for (int i = 1;
             i <= a.length();
             i++) {

            for (int j = 1;
                 j <= b.length();
                 j++) {

                int diagonal =
                    dp[i - 1][j - 1] +
                    (a.charAt(i - 1) ==
                     b.charAt(j - 1)
                        ? match
                        : mismatch);

                int up =
                    dp[i - 1][j] + gap;

                int left =
                    dp[i][j - 1] + gap;

                dp[i][j] =
                    Math.max(
                        diagonal,
                        Math.max(up, left)
                    );
            }
        }

        return dp[a.length()][b.length()];
    }
}
