import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    public static void append(String filename, String data) {

        try {
            FileWriter fw =
                new FileWriter(filename, true);

            fw.write(data);
            fw.write(System.lineSeparator());

            fw.close();

        } catch (IOException e) {
            System.out.println(
                "File error: " + e.getMessage()
            );
        }
    }

    public static void saveSearch(String query) {
        append(
            "history.txt",
            query
        );
    }

    public static void showHistory() {
        System.out.println("\n===== SEARCH HISTORY =====");
        showFile("history.txt");
    }

    public static void showFile(String filename) {

        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("No saved data yet.");
            return;
        }

        try {

            BufferedReader br =
                new BufferedReader(
                    new FileReader(file)
                );

            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                "Unable to read file."
            );
        }
    }
}
