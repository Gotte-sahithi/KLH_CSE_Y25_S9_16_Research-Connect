public class ZAlgorithm {

    /*
     * MODULE 2 - STRING ALGORITHMS
     *
     * Z-Function
     *
     * Time Complexity: O(n)
     *
     * z[i] = length of the longest substring
     * starting from i that matches the prefix.
     */

    public static int[] build(String s) {

        int n = s.length();

        int[] z = new int[n];

        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {

            if (i <= right) {
                z[i] =
                    Math.min(
                        right - i + 1,
                        z[i - left]
                    );
            }

            while (
                i + z[i] < n &&
                s.charAt(z[i]) ==
                s.charAt(i + z[i])
            ) {
                z[i]++;
            }

            if (i + z[i] - 1 > right) {

                left = i;

                right =
                    i + z[i] - 1;
            }
        }

        return z;
    }


    /*
     * Search a pattern using Z-function.
     *
     * Example:
     *
     * text    = "research algorithms"
     * pattern = "algorithm"
     */

    public static int search(
            String text,
            String pattern) {

        String combined =
            pattern + "$" + text;

        int[] z =
            build(combined);

        for (
            int i = pattern.length() + 1;
            i < combined.length();
            i++
        ) {

            if (z[i] == pattern.length()) {

                return
                    i -
                    pattern.length() -
                    1;
            }
        }

        return -1;
    }
}
