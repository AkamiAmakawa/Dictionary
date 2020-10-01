import java.util.ArrayList;

public class Dictionary {
    private final ArrayList<ArrayList<Word>> words = new ArrayList<>();
    public Dictionary(){
        for(int i = 0; i < 40; i++){
            words.add(new ArrayList<>());
        }
    }

    /**
     * Add word to respective group according to index
     * from a, b, c will be in group 1, 2, 3 accordingly.
     * Unknown character will be in group 0.
     * @param word word to add
     */
    public void addWord(Word word) {
        int index = Character.toUpperCase(word.getWord_target().charAt(0)) - 64;
        if(index < 1 || index > 36){
            words.get(0).add(word);
        }
        else{
            words.get(index).add(word);
        }
    }

    public Word getWord(int group,int index) {
        return words.get(group).get(index);
    }

    public ArrayList<Word> getGroup(int group) { return words.get(group); }

    public int size(int group) {
        return words.get(group).size();
    }
}
