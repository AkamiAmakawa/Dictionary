import java.io.IOException;
import java.net.BindException;

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
            for(int j = 0; j < dictionary.size(i); j++){
                System.out.format("%-8d %s \n", index + 1, dictionary.getWord(i,j).formattedWord());
                index++;
            }
        }
    }

    public static void dictionaryBasic(){
        Dictionary engDict = new Dictionary();
        DictionaryManagement.insertFromCommandline(engDict);
        DictionaryCommandLine.showAllWords(engDict);
    }

    public static void dictionaryAdvanced() throws IOException {
        Dictionary engDict = new Dictionary();
        DictionaryManagement.insertFromFile(engDict);
        DictionaryCommandLine.showAllWords(engDict);
    }

    public static void main(String[] args) throws IOException{
        dictionaryBasic();
        dictionaryAdvanced();
    }
}

