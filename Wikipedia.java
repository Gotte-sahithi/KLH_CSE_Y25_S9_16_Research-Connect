import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Wikipedia {

    /*
     * MODULE 1 - TEXT ANALYTICS ENGINE
     *
     * Wikipedia search component.
     *
     * This connects Research Connect with Wikipedia
     * and retrieves relevant articles for a user query.
     */

    private static final String WIKI_URL =
            "https://en.wikipedia.org/w/api.php";

    public static void search(String query) {

        try {

            String encodedQuery =
                    URLEncoder.encode(query, "UTF-8");

            String apiUrl =
                    WIKI_URL
                    + "?action=query"
                    + "&list=search"
                    + "&srsearch=" + encodedQuery
                    + "&format=json"
                    + "&srlimit=5";

            URL url = new URL(apiUrl);

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty(
                    "User-Agent",
                    "ResearchConnect/1.0"
            );

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()
                            )
                    );

            StringBuilder response =
                    new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            connection.disconnect();

            String json = response.toString();

            displayResults(json, query);

        } catch (Exception e) {

            System.out.println();
            System.out.println(
                    "Unable to connect to Wikipedia."
            );

            System.out.println(
                    "Please check your internet connection."
            );
        }
    }


    /*
     * Extract and display article titles and snippets.
     *
     * This is a lightweight parser so that the project
     * does not require external JSON libraries.
     */

    private static void displayResults(
            String json,
            String query) {

        System.out.println();
        System.out.println(
                "================================================"
        );

        System.out.println(
                "              WIKIPEDIA RESULTS"
        );

        System.out.println(
                "================================================"
        );

        System.out.println(
                "Search Query : " + query
        );

        System.out.println();

        int searchStart =
                json.indexOf("\"search\":[");

        if (searchStart == -1) {

            System.out.println(
                    "No Wikipedia articles found."
            );

            return;
        }

        String results =
                json.substring(searchStart);

        int position = 0;

        int count = 0;

        while (count < 5) {

            int titleStart =
                    results.indexOf(
                            "\"title\":\"",
                            position
                    );

            if (titleStart == -1) {
                break;
            }

            titleStart += 9;

            int titleEnd =
                    results.indexOf(
                            "\"",
                            titleStart
                    );

            if (titleEnd == -1) {
                break;
            }

            String title =
                    results.substring(
                            titleStart,
                            titleEnd
                    );

            title =
                    cleanText(title);

            int snippetStart =
                    results.indexOf(
                            "\"snippet\":\"",
                            titleEnd
                    );

            String snippet = "";

            if (snippetStart != -1) {

                snippetStart += 11;

                int snippetEnd =
                        results.indexOf(
                                "\"",
                                snippetStart
                        );

                if (snippetEnd != -1) {

                    snippet =
                            results.substring(
                                    snippetStart,
                                    snippetEnd
                            );

                    snippet =
                            cleanText(snippet);
                }
            }

            count++;

            System.out.println(
                    "---------------- Result " + count
                    + " ----------------"
            );

            System.out.println(
                    "Title   : " + title
            );

            System.out.println(
                    "Summary : " + snippet
            );

            System.out.println(
                    "Link    : https://en.wikipedia.org/wiki/"
                    + title.replace(" ", "_")
            );

            System.out.println();

            position = titleEnd + 1;
        }

        if (count == 0) {

            System.out.println(
                    "No matching articles found."
            );
        }

        System.out.println(
                "================================================"
        );
    }


    private static String cleanText(
            String text) {

        text =
                text.replace("\\\"", "\"");

        text =
                text.replace("\\n", " ");

        text =
                text.replace("&quot;", "\"");

        text =
                text.replace("&amp;", "&");

        text =
                text.replace("<span class=\"searchmatch\">", "");

        text =
                text.replace("</span>", "");

        return text;
    }
}
