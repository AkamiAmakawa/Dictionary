import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
     *
     * @param dictionary dictionary to add word to
     * @throws IOException when file not found
     */
    public static void insertFromFile(Dictionary dictionary) throws IOException, URISyntaxException {
        URL dataURL = DictionaryManagement.class.getClassLoader().getResource("data/E_V.txt");
        File data = new File(dataURL.toURI());
        BufferedReader fileReader = new BufferedReader(new FileReader(data));
        Pattern wordPattern = Pattern.compile("(.+?)(<html>.+?</html>)");
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
        int group = Character.toUpperCase(wordToDel.charAt(0)) - 64;
        if (group < 1 || group > 36) {
            group = 0;
        }
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
     * @param path       path to file
     */
    public static void dictionaryExportToFile(Dictionary dictionary, String path) {
        File exportFile = new File(path);
        try {
            exportFile.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        try {
            int linePerCommand = 0;
            FileWriter fileWriter = new FileWriter(exportFile);
            for (int i = 0; i < 40; i++) {
                for (int j = 0; j < dictionary.size(i); j++) {
                    if (linePerCommand <= 0) {
                        fileWriter.write("insert into dictionarywcs.E_V(word,wordDefinition) values " + "\n");
                        linePerCommand = 50;
                    }
                    String temp = String.format("(\"%s\",\"%s\")",
                            dictionary.getWord(i, j).getWord_target(),
                            dictionary.getWord(i, j).getWord_explain());
                    fileWriter.write(temp);
                    if (linePerCommand > 1) {
                        fileWriter.write(",\n");
                    }
                    if (linePerCommand == 1) {
                        fileWriter.write(";\n");
                    }
                    linePerCommand--;
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
            for (int i = 0; i < 40; i++) {
                for (int j = 0; j < dictionary.size(i) && maxWord != 0; j++) {
                    resultWord.add(dictionary.getWord(i, j));
                    maxWord--;
                }
            }
            return resultWord;
        }
        int group = group = Character.toUpperCase(targetWord.charAt(0)) - 64;
        if (group < 1 || group > 36) {
            group = 0;
        }
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
}