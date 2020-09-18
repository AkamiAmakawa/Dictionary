import java.util.ArrayList;

public class Dictionary {
    private final ArrayList<Word> words = new ArrayList<Word>(10);

    /**
     * Add a word to this dictionary.
     *
     * @param word word to add
     */
    public void addWord(Word word) {
        words.add(word);
    }
}
