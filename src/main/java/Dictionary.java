import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class Dictionary {
    private final TreeMap<Character, ArrayList<Word>> words = new TreeMap<>();

    public Dictionary() {
    }

    /**
     * Add word to respective group according to index
     * from a, b, c will be in group 1, 2, 3 accordingly.
     * Unknown character will be in group 0.
     *
     * @param word word to add
     */
    public void addWord(Word word) {
        char index = Character.toUpperCase(word.getWord_target().charAt(0));
        if (words.get(index) == null) {
            words.put(index, new ArrayList<>());
        }
        words.get(index).add(word);
    }

    public Word getWord(char group, int index) {
        return words.get(group).get(index);
    }

    public ArrayList<Word> getGroup(Character group) {
        return words.get(group);
    }

    public int size(char group) {
        if (!words.containsKey(group)) {
            return 0;
        }
        return words.get(group).size();
    }

    public Set<Character> getKey() {
        return words.keySet();
    }

    public void removeWord(char group, int index) {
        words.get(group).remove(index);
    }

    boolean containKey(char key) {
        return words.containsKey(key);
    }
}
