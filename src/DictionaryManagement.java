import java.util.Scanner;

public class DictionaryManagement {
    Scanner wordScan = new Scanner(System.in);
    Dictionary dictionary = new Dictionary();

    /**
     * Construct manager for a existing dictionary.
     *
     * @param dictionary dictionary to manage
     */
    public DictionaryManagement(Dictionary dictionary) {
        this.dictionary = dictionary;
    }


    public void insertFromCommandline() {
        int n;
        n = wordScan.nextInt();
        Word temp = new Word();
        for (int i = 0; i < n; i++) {
            temp.setWord_target(wordScan.nextLine());
            temp.setWord_explain(wordScan.nextLine());
            dictionary.addWord(temp);
        }
    }
}
