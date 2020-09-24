import java.util.ArrayList;

public class Dictionary {
    private final ArrayList<Word> words = new ArrayList<>();

    /**
     * Add a word to index location.
     *
     * @param index location to add to
     * @param word  word to add
     */
    public void addWord(int index, Word word) {
        words.add(index, word);
    }

    public Word getWord(int index) {
        return words.get(index);
    }

    /**
     * @return size of the dictionary
     */
    public int size() {
        return words.size();
    }
}
