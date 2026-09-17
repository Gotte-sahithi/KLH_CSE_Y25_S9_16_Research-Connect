public class SearchEngine {

    private Paper[] papers;

    public SearchEngine() {

        papers = new Paper[] {

            new Paper(
                1,
                "Attention Is All You Need",
                "Ashish Vaswani",
                2017,
                "Artificial Intelligence",
                95000,
                "attention transformer NLP deep learning",
                "https://arxiv.org/abs/1706.03762"
            ),

            new Paper(
                2,
                "Deep Residual Learning for Image Recognition",
                "Kaiming He",
                2015,
                "Computer Vision",
                220000,
                "CNN image recognition deep learning residual",
                "https://arxiv.org/abs/1512.03385"
            ),

            new Paper(
                3,
                "MapReduce Simplified Data Processing on Large Clusters",
                "Jeffrey Dean",
                2004,
                "Distributed Systems",
                45000,
                "distributed systems big data mapreduce",
                "https://research.google/pubs/mapreduce-simplified-data-processing-on-large-clusters/"
            ),

            new Paper(
                4,
                "Large Language Models Survey",
                "Research Group",
                2023,
                "Natural Language Processing",
                8000,
                "LLM NLP generative AI transformer language",
                "https://arxiv.org/"
            ),

            new Paper(
                5,
                "Blockchain Peer-to-Peer Electronic Cash",
                "Satoshi Nakamoto",
                2008,
                "Blockchain",
                35000,
                "blockchain cryptography peer-to-peer distributed ledger",
                "https://bitcoin.org/bitcoin.pdf"
            )
        };
    }

    public Paper[] getPapers() {
        return papers;
    }

    /*
     * MODULE 2:
     * KMP is used for exact paper searching.
     */
    public void search(String query) {

        FileManager.saveSearch(query);

        System.out.println(
            "\n===== SEARCH RESULTS ====="
        );

        boolean found = false;

        for (int i = 0; i < papers.length; i++) {

            String text =
                papers[i].searchableText();

            if (KMP.search(
                    text,
                    query.toLowerCase()
                ) != -1) {

                papers[i].display();
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No exact match found."
            );

            System.out.println(
                "Try Fuzzy Search."
            );
        }
    }

    /*
     * MODULE 2 + MODULE 3:
     * Suffix Array + Kasai LCP for similarity.
     */
    public static void documentSimilarity(
            String a, String b) {

        String combined =
            a.toLowerCase() +
            "#" +
            b.toLowerCase();

        int[] sa =
            SuffixArray.build(combined);

        int[] lcp =
            Kasai.build(combined, sa);

        int boundary = a.length();

        int best = 0;

        for (int i = 1; i < sa.length; i++) {

            boolean first =
                sa[i - 1] < boundary;

            boolean second =
                sa[i] < boundary;

            if (first != second) {

                if (lcp[i] > best) {
                    best = lcp[i];
                }
            }
        }

        double score =
            (double) best /
            Math.max(
                1,
                Math.max(a.length(), b.length())
            );

        System.out.println(
            "\n===== DOCUMENT SIMILARITY ====="
        );

        System.out.println(
            "Longest common substring = " +
            best
        );

        System.out.printf(
            "Similarity = %.2f%%\n",
            score * 100
        );
    }

    /*
     * MODULE 4:
     * Citation graph represented as capacity network.
     * Dinic is used for max flow.
     */
    public void citationFlow() {

        System.out.println(
            "\n===== CITATION FLOW ====="
        );

        Dinic flow = new Dinic(5);

        flow.addEdge(0, 1, 10);
        flow.addEdge(0, 2, 8);
        flow.addEdge(1, 2, 2);
        flow.addEdge(1, 3, 6);
        flow.addEdge(2, 3, 3);
        flow.addEdge(2, 4, 5);
        flow.addEdge(3, 4, 7);

        int result =
            flow.maxFlow(0, 4);

        System.out.println(
            "Maximum citation flow = " +
            result
        );
    }

    /*
     * MODULE 1:
     * Scheduling demonstration.
     * This is a greedy approximation demonstration,
     * not an exact solver for every NP-hard scheduling case.
     */
    public void scheduling() {

        System.out.println(
            "\n===== PROJECT SCHEDULING ====="
        );

        int[] deadline =
            {2, 1, 2, 3, 1};

        int[] reward =
            {100, 80, 60, 40, 30};

        boolean[] slot =
            new boolean[4];

        int total = 0;

        for (int count = 0;
             count < reward.length;
             count++) {

            int best = -1;

            for (int i = 0;
                 i < reward.length;
                 i++) {

                if (best == -1 ||
                    reward[i] > reward[best]) {

                    best = i;
                }
            }

            int d = deadline[best];

            while (d > 0 && slot[d]) {
                d--;
            }

            if (d > 0) {
                slot[d] = true;
                total += reward[best];

                System.out.println(
                    "Task " + best +
                    " -> slot " + d +
                    " -> reward " + reward[best]
                );
            }

            reward[best] = -1;
        }

        System.out.println(
            "Total reward = " + total
        );
    }

    /*
     * MODULE 1:
     * Miller-Rabin large-prime test.
     */
    public static boolean isPrime(long n) {

        if (n < 2) return false;

        int[] small =
            {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};

        for (int p : small) {

            if (n == p) return true;

            if (n % p == 0) return false;
        }

        long d = n - 1;
        int s = 0;

        while ((d & 1) == 0) {
            d >>= 1;
            s++;
        }

        long[] bases =
            {2, 3, 5, 7, 11, 13};

        for (int i = 0;
             i < bases.length;
             i++) {

            long a = bases[i] % n;

            long x =
                modPow(a, d, n);

            if (x == 1 || x == n - 1)
                continue;

            boolean composite = true;

            for (int r = 1;
                 r < s;
                 r++) {

                x = modMul(x, x, n);

                if (x == n - 1) {
                    composite = false;
                    break;
                }
            }

            if (composite)
                return false;
        }

        return true;
    }

    private static long modMul(
            long a, long b, long mod) {

        long result = 0;

        a %= mod;

        while (b > 0) {

            if ((b & 1) == 1)
                result =
                    (result + a) % mod;

            a =
                (a + a) % mod;

            b >>= 1;
        }

        return result;
    }

    private static long modPow(
            long a, long b, long mod) {

        long result = 1;

        while (b > 0) {

            if ((b & 1) == 1)
                result =
                    modMul(result, a, mod);

            a =
                modMul(a, a, mod);

            b >>= 1;
        }

        return result;
    }

    public void showAll() {

        System.out.println(
            "\n===== ALL RESEARCH PAPERS ====="
        );

        for (int i = 0;
             i < papers.length;
             i++) {

            papers[i].display();
        }
    }

    public void runDSADemo() {

        System.out.println(
            "\n===== DSA ALGORITHM DEMO ====="
        );

        String text =
            "research algorithms";

        String pattern =
            "algorithm";

        System.out.println(
            "KMP index = " +
            KMP.search(text, pattern)
        );

        System.out.println(
            "Rabin-Karp index = " +
            RabinKarp.search(text, pattern)
        );

        int[] z =
            ZAlgorithm.build(
                pattern + "$" + text
            );

        System.out.println(
            "Z-function calculated."
        );

        System.out.println(
            "Edit distance = " +
            FuzzySearch.editDistance(
                "research",
                "researh"
            )
        );

        System.out.println(
            "TSP result = " +
            MatrixChain.tspDemo()
        );

        citationFlow();
    }
}
