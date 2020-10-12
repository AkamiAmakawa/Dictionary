import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

public class DictionaryCommandLine {
    /**
     * Display the dictionary in a formatted way.
     *
     * @param dictionary dictionary to display
     */
    public static void showAllWords(Dictionary dictionary) {
        System.out.format("%-8s %-30s %s \n", "No", "| English", "| Vietnamese");
        int index = 0;
        Set<Character> keys = dictionary.getKey();
        for (Character key : keys) {
            for (int j = 0; j < dictionary.size(key); j++) {
                System.out.format("%-8d %s \n", index + 1, dictionary.getWord(key, j).formattedWord());
                index++;
            }
        }
    }

    public static void dictionaryLookup(Dictionary dictionary, String lookupWord) {
        char lookupIndex = Character.toUpperCase(lookupWord.charAt(0));
        int firstCharLibSize = dictionary.size(lookupIndex);
        ArrayList<Word> charLib = dictionary.getGroup(lookupIndex);

        boolean result = false;

        for (int i = 0; i < firstCharLibSize; i++) {
            if (charLib.get(i).getWord_target().equalsIgnoreCase(lookupWord)) {
                result = true;
                System.out.format("%s \n", charLib.get(i).formattedWord());
            }
        }

        if (!result) {
            System.out.println("Word won't exist");
        }
    }

    public static void dictionaryBasic() {
        Dictionary engDict = new Dictionary();
        DictionaryManagement.insertFromCommandline(engDict);
        DictionaryCommandLine.showAllWords(engDict);
    }


    public static void dictionaryAdvanced() throws IOException, URISyntaxException {
        Dictionary engDict = new Dictionary();
        DictionaryManagement.insertFromFile(engDict);
        showAllWords(engDict);
        DictionaryManagement.dictionaryExportToFile(engDict, "exportedDict.txt");
        DictionaryManagement.deleteWord(engDict);
        showAllWords(engDict);
    }

}

