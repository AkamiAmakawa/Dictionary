import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryCommandLine {
    /**
     * Display the dictionary in a formatted way.
     *
     * @param dictionary dictionary to display
     */
    public static void showAllWords(Dictionary dictionary) {
        System.out.format("%-8s %-30s %s \n", "No", "| English", "| Vietnamese");
        int index = 0;
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < dictionary.size(i); j++) {
                System.out.format("%-8d %s \n", index + 1, dictionary.getWord(i, j).formattedWord());
                index++;
            }
        }
    }

    public static void dictionaryLookup(Dictionary dictionary) {
        Scanner scanInput = new Scanner(System.in);
        String lookupWord = scanInput.nextLine();
        int lookupIndex = Character.toUpperCase(lookupWord.charAt(0)) - 64;
        if (lookupIndex < 1 || lookupIndex > 36) {
            lookupIndex = 0;
        }

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

    public static void dictionarySearcher(Dictionary dictionary) {
        Scanner wordScan = new Scanner(System.in);
        String targetWord = wordScan.nextLine();
        int group = Character.toUpperCase(targetWord.charAt(0)) - 64;
        if (group < 1 || group > 36) {
            group = 0;
        }

        //Create pattern
        targetWord += ".+?";
        Pattern targetPattern = Pattern.compile((targetWord), Pattern.CASE_INSENSITIVE);

        for (int i = 0; i < dictionary.size(group); i++) {
            Matcher wordMatcher = targetPattern.matcher(dictionary.getWord(group, i).getWord_target());
            if (wordMatcher.matches()) {
                System.out.println(dictionary.getWord(group,i).formattedWord());
            }
        }
    }

    public static void dictionaryAdvanced() throws IOException {
        Dictionary engDict = new Dictionary();
        DictionaryManagement.insertFromFile(engDict);
        showAllWords(engDict);
        DictionaryManagement.deleteWord(engDict);
        showAllWords(engDict);
        dictionarySearcher(engDict);
    }


    public static void main(String[] args) throws IOException {
        dictionaryAdvanced();
    }
}

