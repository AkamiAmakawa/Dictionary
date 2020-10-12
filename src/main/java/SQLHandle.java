import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class SQLHandle {
    private final String driverName = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://db4free.net:3306/dictionarywcs";
    private final String username = "dictuser";
    private final String password = "dictuser";
    private Connection connection;
    private Statement statement;

    public SQLHandle() throws ClassNotFoundException {
        Class.forName(driverName);
        try {
            connection = DriverManager.getConnection(URL, username, password);
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void createSQLCommand(Dictionary dictionary, String path) {
        Set<Character> keys = dictionary.getKey();
        File exportFile = new File(path);
        try {
            exportFile.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        try {
            FileWriter fileWriter = new FileWriter(exportFile);
            for (char key : keys) {
                String command = String.format(
                        "insert into idx%c(word,wordDefinition)" +
                                "select word,wordDefinition" +
                                " from E_V where lower(word) like lower('%c%s');", key, key, "%");
                fileWriter.write(command + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getData() {
        try {
            return statement.executeQuery("select* from E_V");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ArrayList<Word> dictionarySearcher(String targetWord, int limit) {
        String index = "";
        if (targetWord.equals("")) {
            index = "displayTable";
        } else if (targetWord.charAt(0) == '-') {
            index = "Spc";
        } else {
            index = "idx" + Character.toUpperCase(targetWord.charAt(0));
        }
        ArrayList<Word> result = new ArrayList<>();
        try {
            String searchCommand = String.format("select word,wordDefinition\n" +
                    " from\n" +
                    "%s\n" +
                    " where word like '%s%s'\n" +
                    " order by word asc\n" +
                    " limit %d;", index, targetWord, "%", limit);
            ResultSet resultSet = statement.executeQuery(searchCommand);
            while (resultSet.next()) {
                Word temp = new Word();
                temp.setWord_target(resultSet.getString("word"));
                temp.setWord_explain(resultSet.getString("wordDefinition"));
                result.add(temp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }


}