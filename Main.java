mport java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        SearchEngine engine = new SearchEngine();

        while (true) {
            System.out.println("\n======================================");
            System.out.println("          RESEARCH CONNECT");
            System.out.println("======================================");
            System.out.println("1. Search Research Papers");
            System.out.println("2. Search Wikipedia");
            System.out.println("3. Fuzzy Search");
            System.out.println("4. Document Similarity");
            System.out.println("5. Citation Flow");
            System.out.println("6. Project Scheduling");
            System.out.println("7. Large Prime Test");
            System.out.println("8. View Bookmarks");
            System.out.println("9. View Search History");
            System.out.println("10. Show All Papers");
            System.out.println("11. Run DSA Demo");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice = Integer.parseInt(br.readLine());

            switch (choice) {
                case 1:
                    System.out.print("Search: ");
                    engine.search(br.readLine());
                    break;
                case 2:
                    System.out.print("Wikipedia topic: ");
                    Wikipedia.search(br.readLine());
                    break;
                case 3:
                    System.out.print("Enter title/text: ");
                    FuzzySearch.search(br.readLine(), engine.getPapers());
                    break;
                case 4:
                    System.out.print("First document: ");
                    String a = br.readLine();
                    System.out.print("Second document: ");
                    String b = br.readLine();
                    SearchEngine.documentSimilarity(a, b);
                    break;
                case 5:
                    engine.citationFlow();
                    break;
                case 6:
                    engine.scheduling();
                    break;
                case 7:
                    System.out.print("Enter number: ");
                    long n = Long.parseLong(br.readLine());
                    System.out.println(n + (SearchEngine.isPrime(n)
                            ? " is probably PRIME."
                            : " is COMPOSITE."));
                    break;
                case 8:
                    BookmarkManager.showBookmarks();
                    break;
                case 9:
                    FileManager.showHistory();
                    break;
                case 10:
                    engine.showAll();
                    break;
                case 11:
                    engine.runDSADemo();
                    break;
                case 0:
                    System.out.println("Thank you for using Research Connect.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
