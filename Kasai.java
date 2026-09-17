public class Kasai {

    // Module 2: Kasai LCP construction
    // Time: O(n) after suffix array.
    public static int[] build(String text, int[] suffixArray) {

        int n = text.length();
        int[] lcp = new int[n];
        int[] inverse = new int[n];

        for (int i = 0; i < n; i++) {
            inverse[suffixArray[i]] = i;
        }

        int h = 0;

        for (int i = 0; i < n; i++) {

            int rank = inverse[i];

            if (rank == 0) continue;

            int j = suffixArray[rank - 1];

            while (i + h < n &&
                   j + h < n &&
                   text.charAt(i + h) == text.charAt(j + h)) {
                h++;
            }

            lcp[rank] = h;

            if (h > 0) h--;
        }

        return lcp;
    }
}
