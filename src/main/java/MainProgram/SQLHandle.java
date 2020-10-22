package MainProgram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class SQLHandle {
    private Connection connection;
    private Connection alternative;
    private Connection loadData;
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
            alternative = DriverManager.getConnection(URL, username, password);
            loadData = DriverManager.getConnection(URL, username, password);
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
            Statement load = loadData.createStatement();
            return load.executeQuery("select id, word, wordDefinition from E_V;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ArrayList<Word> dictionarySearcher(String targetWord, int limit) {

        ArrayList<Word> result = new ArrayList<>();
        try {
            String searchCommand = String.format("select id,word,wordDefinition\n" +
                    " from\n" +
                    "E_V\n" +
                    " where word like '%s%s'\n" +
                    " order by word asc\n" +
                    " limit %d;", targetWord, "%", limit);
            Statement search = alternative.createStatement();
            ResultSet resultSet = search.executeQuery(searchCommand);
            while (resultSet.next()) {
                Word temp = new Word();
                temp.setDbID(resultSet.getLong("id"));
                temp.setWord_target(resultSet.getString("word"));
                temp.setWord_explain(resultSet.getString("wordDefinition"));
                result.add(temp);
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public void changeWord(long id, String meaning) {
        String querry = "update E_V set wordDefinition = ? where id = ?;";
        try {
            preparedStatement = connection.prepareStatement(querry);
            preparedStatement.setString(1, meaning);
            preparedStatement.setLong(2, id);
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

    public void deleteWord(long id) {
        String querry = "delete from E_V where id = ?";
        try {
            preparedStatement = connection.prepareStatement(querry);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public long getCurrentID() {
        long id = 0;
        String query = "SELECT `AUTO_INCREMENT`\n" +
                "FROM  INFORMATION_SCHEMA.TABLES\n" +
                "WHERE TABLE_SCHEMA = 'dictionarywcs'\n" +
                "AND   TABLE_NAME   = 'E_V';";
        try {
            ResultSet result = statement.executeQuery(query);
            result.next();
            id = result.getLong("AUTO_INCREMENT");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }


}