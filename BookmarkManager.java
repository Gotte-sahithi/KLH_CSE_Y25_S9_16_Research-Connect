public class BookmarkManager {

    private static final String FILE = "bookmarks.txt";

    public static void add(Paper paper) {

        FileManager.append(
            FILE,
            paper.getId() + "|" +
            paper.getTitle() + "|" +
            paper.getAuthor() + "|" +
            paper.getYear() + "|" +
            paper.getDomain() + "|" +
            paper.getUrl()
        );

        System.out.println(
            "Paper bookmarked successfully."
        );
    }

    public static void showBookmarks() {
        System.out.println("\n===== SAVED BOOKMARKS =====");
        FileManager.showFile(FILE);
    }
}
