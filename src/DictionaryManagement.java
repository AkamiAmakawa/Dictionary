import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryManagement {
    static Scanner wordScan = new Scanner(System.in);

    /**
     * Insert a word from commandline to selected dictionary.
     * Type in the number of words to add first then type
     * word and meaning in separated line one by one.
     */
    public static void insertFromCommandline(Dictionary dictionary) {
        int n;
        n = wordScan.nextInt();
        String buffer = wordScan.nextLine();
        for (int i = 0; i < n; i++) {
            Word temp = new Word();
            temp.setWord_target(wordScan.nextLine());
            temp.setWord_explain(wordScan.nextLine());
            dictionary.addWord(temp);
        }
    }

    /**
     * Read words from data/dictionary.txt.
     * Each word must be on one line along with its meaning separated by tab.
     * @param dictionary dictionary to add word to
     * @throws IOException when file not found
     */
    public static void insertFromFile(Dictionary dictionary) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/data/dictionaries.txt"));
        Pattern wordPattern = Pattern.compile("(.+?)\t(.+?)");
        String line;
        while ((line = fileReader.readLine()) != null) {
            Matcher wordMatcher = wordPattern.matcher(line);
            if (wordMatcher.matches()) {
                Word temp = new Word();
                temp.setWord_target(wordMatcher.group(1));
                temp.setWord_explain(wordMatcher.group(2));
                dictionary.addWord(temp);
            }
        }
    }
}
