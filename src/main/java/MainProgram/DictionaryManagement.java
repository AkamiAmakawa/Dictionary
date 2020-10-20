package MainProgram;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
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
     *
     * @param dictionary dictionary to add word to
     * @throws IOException when file not found
     */
    public static void insertFromFile(Dictionary dictionary) throws IOException, URISyntaxException {
        dictionary.clear();
        URL dataURL = DictionaryManagement.class.getClassLoader().getResource("data/E_V.txt");
        File data = new File(dataURL.toURI());
        BufferedReader fileReader = new BufferedReader(new FileReader(data));
        Pattern wordPattern = Pattern.compile("(.+?)<\\|>(.+?)");
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

    public static void insertFromDB(Dictionary dictionary, ResultSet data) {
        dictionary.clear();
        try {
            while (data.next()) {
                Word temp = new Word();
                temp.setWord_target(data.getString("word"));
                temp.setWord_explain(data.getString("wordDefinition"));
                dictionary.addWord(temp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Delete a word in the selected dictionary if existed.
     */
    public static void deleteWord(Dictionary dictionary) {
        String wordToDel = wordScan.nextLine();
        char group = Character.toUpperCase(wordToDel.charAt(0));
        Word targetWord;
        for (int i = 0; i < dictionary.size(group); i++) {
            targetWord = dictionary.getWord(group, i);
            if (targetWord.getWord_target().equalsIgnoreCase(wordToDel)) {
                dictionary.removeWord(group, i);
                return;
            }
        }
        System.out.println("Word not found");
    }

    /**
     * Write the dictionary data to a file, will create a new file if the selected path is not exist.
     *
     * @param dictionary dictionary to write
     */
    public static void dictionaryUpdate(Dictionary dictionary) {
        File exportFile = new File("src/main/resources/data/E_V.txt");
        try {
            exportFile.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        try {
            FileWriter fileWriter = new FileWriter(exportFile);
            Set<Character> keys = dictionary.getKey();
            for (char key : keys) {
                for (int j = 0; j < dictionary.size(key); j++) {
                    String temp = String.format("%s<|>%s\n",
                            dictionary.getWord(key, j).getWord_target(),
                            dictionary.getWord(key, j).getWord_explain());
                    fileWriter.write(temp);

                }
            }
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    public static ArrayList<Word> dictionarySearcher(Dictionary dictionary, String targetWord, int maxWord) {
        ArrayList<Word> resultWord = new ArrayList<>();
        if (targetWord.equals("")) {
            Set<Character> keys = dictionary.getKey();
            for (char key : keys) {
                for (int j = 0; j < dictionary.size(key) && maxWord != 0; j++) {
                    resultWord.add(dictionary.getWord(key, j));
                    maxWord--;
                }
            }
            return resultWord;
        }
        char group = Character.toUpperCase(targetWord.charAt(0));
        //Create pattern
        targetWord += ".*?";
        Pattern targetPattern = Pattern.compile((targetWord), Pattern.CASE_INSENSITIVE);
        for (int i = 0; i < dictionary.size(group) && maxWord != 0; i++) {
            Matcher wordMatcher = targetPattern.matcher(dictionary.getWord(group, i).getWord_target());
            if (wordMatcher.matches()) {
                resultWord.add(dictionary.getWord(group, i));
                maxWord--;
            }
        }
        return resultWord;
    }

    public static ArrayList<Word> suggestWord(Dictionary dictionary, String word, int limit) {
        ArrayList<Word> result = new ArrayList<>();
        Set<Character> keys = dictionary.getKey();
        LevenshteinDistance distance = new LevenshteinDistance();
        loop:
        for (char key : keys) {
            for (int i = 0; i < dictionary.size(key); i++) {
                if (!word.equals("")) {
                    if (!dictionary.containKey(Character.toUpperCase(word.charAt(0)))) {
                        break;
                    }
                }
                double check = distance.apply(dictionary.getWord(key, i).getWord_target(), word);
                if (check < 3) {
                    result.add(dictionary.getWord(key, i));
                    limit--;
                    if (limit <= 0) {
                        break loop;
                    }
                }
            }
        }
        return result;
    }
}