package MainProgram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class SQLHandle {
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public SQLHandle() throws ClassNotFoundException {
        String driverName = "com.mysql.cj.jdbc.Driver";
        Class.forName(driverName);
        try {
            String URL = "jdbc:mysql://db4free.net:3306/dictionarywcs";
            String username = "dictuser";
            String password = "dictuser";
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
            return statement.executeQuery("select word, wordDefinition from E_V;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ArrayList<Word> dictionarySearcher(String targetWord, int limit) {
        String index;
        if (targetWord.equals("")) {
            index = "displayTable";
        } else if (targetWord.charAt(0) == '-') {
            index = "Spc";
        } else {
            index = "idx" + Character.toUpperCase(targetWord.charAt(0));
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet tbExist = metaData.getTables(null, null, index, new String[]{"Table"});
                if (!tbExist.next()) {
                    index = "displayTable";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        ArrayList<Word> result = new ArrayList<>();
        try {
            String searchCommand = String.format("select id,word,wordDefinition\n" +
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

    public void changeWord(String word, String meaning) {
        String querry = "update E_V set wordDefinition = ? where word = ?;";
        try {
            preparedStatement = connection.prepareStatement(querry);
            preparedStatement.setString(1, meaning);
            preparedStatement.setString(2, word);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void addWord(Word word) {
        String querry = "insert into E_V (word, wordDefinition) values (?,?);";
        try {
            preparedStatement = connection.prepareStatement(querry);
            preparedStatement.setString(1, word.getWord_target());
            preparedStatement.setString(2, word.getWord_explain());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteWord(String target) {
        String querry = "delete from E_V where word = ?";
        try {
            preparedStatement = connection.prepareStatement(querry);
            preparedStatement.setString(1, target);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}