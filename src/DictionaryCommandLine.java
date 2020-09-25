import java.io.IOException;

public class DictionaryCommandLine {
    /**
     * Display the dictionary in a formatted way.
     *
     * @param dictionary dictionary to display
     */
    public static void showAllWords(Dictionary dictionary) {
        System.out.format("%-8s %-30s %s \n", "No", "| English", "| Vietnamese");
        for (int i = 0; i < dictionary.size(); i++) {
            System.out.format("%-8d %s \n", i + 1, dictionary.getWord(i).formattedWord());
        }
    }

    public static void main(String[] args) throws IOException {
        Dictionary engDict = new Dictionary();
        DictionaryManagement.insertFromCommandline(engDict);
        DictionaryManagement.insertFromFile(engDict);
        DictionaryCommandLine.showAllWords(engDict);
    }
}

