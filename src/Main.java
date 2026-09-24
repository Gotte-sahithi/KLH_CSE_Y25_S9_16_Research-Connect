import java.io.*;
import java.util.*;

public class Main {

    // ============================================================
    // RESEARCH PAPER DATA STRUCTURE
    // ============================================================

    static class ResearchPaper {

        String paperId;
        String title;
        String author;
        int publicationYear;
        String researchDomain;
        String keywords;
        String status;
        int citationCount;
        String language;
        String documentType;
        String queryTerms;
        String indexedTerms;

        public ResearchPaper() {
        }
    }

    // ============================================================
    // KMP STRING MATCHING
    // ============================================================

    static int[] buildLPS(String pattern) {

        int[] lps = new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(length)) {

                length++;
                lps[i] = length;
                i++;

            } else {

                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    static ArrayList<Integer> kmpSearch(
            String text,
            String pattern) {

        ArrayList<Integer> positions =
                new ArrayList<>();

        if (text == null || pattern == null) {
            return positions;
        }

        if (pattern.length() == 0) {
            return positions;
        }

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int[] lps = buildLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {

                i++;
                j++;

                if (j == pattern.length()) {

                    positions.add(i - j);

                    j = lps[j - 1];
                }

            } else {

                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return positions;
    }

    // ============================================================
    // EDIT DISTANCE
    // ============================================================

    static int editDistance(String a, String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();

        int n = a.length();
        int m = b.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                if (a.charAt(i - 1) ==
                        b.charAt(j - 1)) {

                    dp[i][j] =
                            dp[i - 1][j - 1];

                } else {

                    int insert =
                            dp[i][j - 1];

                    int delete =
                            dp[i - 1][j];

                    int replace =
                            dp[i - 1][j - 1];

                    dp[i][j] =
                            1 + Math.min(
                                    insert,
                                    Math.min(
                                            delete,
                                            replace
                                    )
                            );
                }
            }
        }

        return dp[n][m];
    }

    // ============================================================
    // LOAD 100 RESEARCH PAPER FILES
    // ============================================================

    static ArrayList<ResearchPaper> loadCorpus(
            String folderPath) {

        ArrayList<ResearchPaper> papers =
                new ArrayList<>();

        File folder =
                new File(folderPath);

        if (!folder.exists()) {

            System.out.println(
                    "ERROR: corpus folder not found."
            );

            System.out.println(
                    "Expected: " +
                    folder.getAbsolutePath()
            );

            return papers;
        }

        File[] files =
                folder.listFiles();

        if (files == null) {
            return papers;
        }

        Arrays.sort(files);

        for (File file : files) {

            if (!file.isFile()) {
                continue;
            }

            if (!file.getName()
                    .toLowerCase()
                    .endsWith(".txt")) {

                continue;
            }

            ResearchPaper paper =
                    readPaper(file);

            if (paper != null) {
                papers.add(paper);
            }
        }

        return papers;
    }

    // ============================================================
    // READ ONE PAPER FILE
    // ============================================================

    static ResearchPaper readPaper(File file) {

        ResearchPaper paper =
                new ResearchPaper();

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                int position =
                        line.indexOf('=');

                if (position == -1) {
                    continue;
                }

                String key =
                        line.substring(
                                0,
                                position
                        ).trim();

                String value =
                        line.substring(
                                position + 1
                        ).trim();

                switch (key) {

                    case "paper_id":
                        paper.paperId = value;
                        break;

                    case "title":
                        paper.title = value;
                        break;

                    case "author":
                        paper.author = value;
                        break;

                    case "publication_year":

                        try {
                            paper.publicationYear =
                                    Integer.parseInt(value);
                        } catch (Exception e) {
                            paper.publicationYear = 0;
                        }

                        break;

                    case "research_domain":
                        paper.researchDomain = value;
                        break;

                    case "keywords":
                        paper.keywords = value;
                        break;

                    case "status":
                        paper.status = value;
                        break;

                    case "citation_count":

                        try {
                            paper.citationCount =
                                    Integer.parseInt(value);
                        } catch (Exception e) {
                            paper.citationCount = 0;
                        }

                        break;

                    case "language":
                        paper.language = value;
                        break;

                    case "document_type":
                        paper.documentType = value;
                        break;

                    case "query_terms":
                        paper.queryTerms = value;
                        break;

                    case "indexed_terms":
                        paper.indexedTerms = value;
                        break;
                }
            }

            return paper;

        } catch (IOException e) {

            System.out.println(
                    "Error reading " +
                    file.getName()
            );

            return null;
        }
    }

    // ============================================================
    // KMP SEARCH THROUGH DATASET
    // ============================================================

    static void searchCorpus(
            ArrayList<ResearchPaper> papers,
            String query) {

        System.out.println();
        System.out.println(
                "================================================"
        );

        System.out.println(
                "             KMP SEARCH RESULTS"
        );

        System.out.println(
                "================================================"
        );

        int resultCount = 0;

        for (ResearchPaper paper : papers) {

            String searchableText =
                    safe(paper.title) + " " +
                    safe(paper.author) + " " +
                    safe(paper.researchDomain) + " " +
                    safe(paper.keywords) + " " +
                    safe(paper.queryTerms) + " " +
                    safe(paper.indexedTerms);

            ArrayList<Integer> positions =
                    kmpSearch(
                            searchableText,
                            query
                    );

            if (!positions.isEmpty()) {

                resultCount++;

                System.out.println();
                System.out.println(
                        "--------------------------------------------"
                );

                System.out.println(
                        "Paper ID        : " +
                        paper.paperId
                );

                System.out.println(
                        "Title           : " +
                        paper.title
                );

                System.out.println(
                        "Author          : " +
                        paper.author
                );

                System.out.println(
                        "Year            : " +
                        paper.publicationYear
                );

                System.out.println(
                        "Research Domain : " +
                        paper.researchDomain
                );

                System.out.println(
                        "Keywords        : " +
                        paper.keywords
                );

                System.out.println(
                        "KMP Positions   : " +
                        positions
                );
            }
        }

        System.out.println();
        System.out.println(
                "Total matching papers: " +
                resultCount
        );
    }

    // ============================================================
    // EDMONDS-KARP NETWORK FLOW
    // ============================================================

    static class MaxFlow {

        int vertices;

        int[][] capacity;

        MaxFlow(int vertices) {

            this.vertices = vertices;

            capacity =
                    new int[vertices][vertices];
        }

        // --------------------------------------------------------
        // ADD DIRECTED EDGE
        // --------------------------------------------------------

        void addEdge(
                int from,
                int to,
                int capacityValue) {

            capacity[from][to] +=
                    capacityValue;
        }

        // --------------------------------------------------------
        // BFS FOR AUGMENTING PATH
        // --------------------------------------------------------

        boolean bfs(
                int source,
                int sink,
                int[] parent,
                int[][] residual) {

            boolean[] visited =
                    new boolean[vertices];

            Queue<Integer> queue =
                    new LinkedList<>();

            queue.add(source);

            visited[source] = true;

            parent[source] = -1;

            while (!queue.isEmpty()) {

                int current =
                        queue.poll();

                for (int next = 0;
                        next < vertices;
                        next++) {

                    if (!visited[next]
                            && residual[current][next] > 0) {

                        parent[next] =
                                current;

                        visited[next] = true;

                        if (next == sink) {
                            return true;
                        }

                        queue.add(next);
                    }
                }
            }

            return false;
        }

        // --------------------------------------------------------
        // EDMONDS-KARP MAXIMUM FLOW
        // --------------------------------------------------------

        int maxFlow(
                int source,
                int sink) {

            int[][] residual =
                    new int[vertices][vertices];

            for (int i = 0;
                    i < vertices;
                    i++) {

                for (int j = 0;
                        j < vertices;
                        j++) {

                    residual[i][j] =
                            capacity[i][j];
                }
            }

            int[] parent =
                    new int[vertices];

            int maximumFlow = 0;

            // Find augmenting paths using BFS
            while (bfs(
                    source,
                    sink,
                    parent,
                    residual)) {

                // Find minimum capacity
                // on the path
                int pathFlow =
                        Integer.MAX_VALUE;

                int current = sink;

                while (current != source) {

                    int previous =
                            parent[current];

                    pathFlow =
                            Math.min(
                                    pathFlow,
                                    residual[
                                            previous
                                    ][
                                            current
                                    ]
                            );

                    current = previous;
                }

                // Update residual capacities
                current = sink;

                while (current != source) {

                    int previous =
                            parent[current];

                    residual[
                            previous
                    ][
                            current
                    ] -= pathFlow;

                    residual[
                            current
                    ][
                            previous
                    ] += pathFlow;

                    current = previous;
                }

                maximumFlow += pathFlow;
            }

            return maximumFlow;
        }
    }

    // ============================================================
    // RESEARCH CONNECT NETWORK FLOW
    // ============================================================

    static void runNetworkFlow() {

        System.out.println();
        System.out.println(
                "================================================"
        );

        System.out.println(
                "          RESEARCH CONNECT NETWORK FLOW"
        );

        System.out.println(
                "================================================"
        );

        /*
         * Network:
         *
         *                 AI
         *                /  \
         *               /    \
         * SOURCE -------      ---- PAPER 1
         *               \          |
         *                \         |
         *                 CYBER --- PAPER 2
         *                   \
         *                    DATA ---- PAPER 3
         *                              |
         *                              |
         *                             SINK
         *
         * This demonstrates capacity-constrained
         * assignment of research domains to papers.
         */

        // --------------------------------------------------------
        // NODE NUMBERS
        // --------------------------------------------------------

        int SOURCE = 0;

        int AI = 1;

        int CYBER = 2;

        int DATA = 3;

        int PAPER1 = 4;

        int PAPER2 = 5;

        int PAPER3 = 6;

        int SINK = 7;

        // Total 8 nodes
        MaxFlow network =
                new MaxFlow(8);

        // --------------------------------------------------------
        // SOURCE -> RESEARCH DOMAINS
        // --------------------------------------------------------

        network.addEdge(
                SOURCE,
                AI,
                3
        );

        network.addEdge(
                SOURCE,
                CYBER,
                2
        );

        network.addEdge(
                SOURCE,
                DATA,
                2
        );

        // --------------------------------------------------------
        // AI -> PAPERS
        // --------------------------------------------------------

        network.addEdge(
                AI,
                PAPER1,
                1
        );

        network.addEdge(
                AI,
                PAPER2,
                1
        );

        network.addEdge(
                AI,
                PAPER3,
                1
        );

        // --------------------------------------------------------
        // CYBER SECURITY -> PAPERS
        // --------------------------------------------------------

        network.addEdge(
                CYBER,
                PAPER1,
                1
        );

        network.addEdge(
                CYBER,
                PAPER2,
                1
        );

        // --------------------------------------------------------
        // DATA SCIENCE -> PAPERS
        // --------------------------------------------------------

        network.addEdge(
                DATA,
                PAPER2,
                1
        );

        network.addEdge(
                DATA,
                PAPER3,
                1
        );

        // --------------------------------------------------------
        // PAPERS -> SINK
        // --------------------------------------------------------

        network.addEdge(
                PAPER1,
                SINK,
                1
        );

        network.addEdge(
                PAPER2,
                SINK,
                1
        );

        network.addEdge(
                PAPER3,
                SINK,
                1
        );

        // --------------------------------------------------------
        // CALCULATE MAX FLOW
        // --------------------------------------------------------

        int maximumFlow =
                network.maxFlow(
                        SOURCE,
                        SINK
                );

        System.out.println();

        System.out.println(
                "Source -> AI capacity       : 3"
        );

        System.out.println(
                "Source -> Cyber capacity    : 2"
        );

        System.out.println(
                "Source -> Data capacity     : 2"
        );

        System.out.println();

        System.out.println(
                "Maximum Flow = " +
                maximumFlow
        );

        System.out.println();

        System.out.println(
                "Edmonds-Karp completed successfully."
        );

        System.out.println(
                "Time Complexity: O(V * E^2)"
        );
    }

    // ============================================================
    // SAFE STRING
    // ============================================================

    static String safe(String value) {

        if (value == null) {
            return "";
        }

        return value;
    }

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(String[] args) {

        Scanner scanner =
                new Scanner(System.in);

        System.out.println();
        System.out.println(
                "================================================"
        );

        System.out.println(
                "              RESEARCH CONNECT"
        );

        System.out.println(
                "              DSA-3 PROJECT"
        );

        System.out.println(
                "================================================"
        );

        // ========================================================
        // LOAD DATASET
        // ========================================================

        System.out.println();
        System.out.println(
                "Loading research papers..."
        );

        ArrayList<ResearchPaper> papers =
                loadCorpus(".");

        System.out.println(
                "Research papers loaded: " +
                papers.size()
        );

        if (papers.isEmpty()) {

            System.out.println();

            System.out.println(
                    "No research papers found."
            );

            System.out.println(
                    "Check your corpus folder."
            );

            scanner.close();

            return;
        }

        // ========================================================
        // KMP SEARCH
        // ========================================================

        System.out.println();
        System.out.println(
                "================================================"
        );

        System.out.println(
                "                 KMP SEARCH"
        );

        System.out.println(
                "================================================"
        );

        System.out.print(
                "Enter keyword: "
        );

        String query =
                scanner.nextLine().trim();

        if (!query.isEmpty()) {

            long start =
                    System.nanoTime();

            searchCorpus(
                    papers,
                    query
            );

            long end =
                    System.nanoTime();

            double time =
                    (end - start)
                    / 1_000_000.0;

            System.out.printf(
                    "\nKMP execution time: %.4f ms%n",
                    time
            );
        }

        // ========================================================
        // EDIT DISTANCE
        // ========================================================

        System.out.println();
        System.out.println(
                "================================================"
        );

        System.out.println(
                "               EDIT DISTANCE"
        );

        System.out.println(
                "================================================"
        );

        System.out.print(
                "Enter first string: "
        );

        String first =
                scanner.nextLine();

        System.out.print(
                "Enter second string: "
        );

        String second =
                scanner.nextLine();

        int distance =
                editDistance(
                        first,
                        second
                );

        System.out.println();

        System.out.println(
                "Edit Distance = " +
                distance
        );

        // ========================================================
        // NETWORK FLOW
        // ========================================================

        runNetworkFlow();

        // ========================================================
        // PROGRAM END
        // ========================================================

        System.out.println();
        System.out.println(
                "================================================"
        );

        System.out.println(
                "             PROGRAM COMPLETED"
        );

        System.out.println(
                "================================================"
        );

        scanner.close();
    }
}
