public class SuffixArray {

    // Module 2: practical suffix-array construction.
    // Doubling method: approximately O(n log^2 n)
    public static int[] build(String s) {
        int n = s.length();
        int[] sa = new int[n];
        int[] rank = new int[n];
        int[] temp = new int[n];

        for (int i = 0; i < n; i++) {
            sa[i] = i;
            rank[i] = s.charAt(i);
        }

        for (int k = 1; k < n; k *= 2) {

            // Manual insertion sort.
            // This avoids java.util.Arrays.
            for (int i = 1; i < n; i++) {
                int key = sa[i];
                int j = i - 1;

                while (j >= 0 &&
                       compare(sa[j], key, k, rank, n) > 0) {
                    sa[j + 1] = sa[j];
                    j--;
                }

                sa[j + 1] = key;
            }

            temp[sa[0]] = 0;

            for (int i = 1; i < n; i++) {
                temp[sa[i]] = temp[sa[i - 1]] +
                    (compare(sa[i - 1], sa[i], k, rank, n) < 0 ? 1 : 0);
            }

            for (int i = 0; i < n; i++) {
                rank[i] = temp[i];
            }

            if (rank[sa[n - 1]] == n - 1) break;
        }

        return sa;
    }

    private static int compare(
            int a, int b, int k,
            int[] rank, int n) {

        if (rank[a] != rank[b])
            return rank[a] - rank[b];

        int ra = (a + k < n) ? rank[a + k] : -1;
        int rb = (b + k < n) ? rank[b + k] : -1;

        return ra - rb;
    }
}
