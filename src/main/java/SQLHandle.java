import java.sql.*;
import java.util.ArrayList;

public class SQLHandle {
    private Connection connection;
    private final String driverName = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://db4free.net:3306/dictionarywcs";
    private final String username = "dictuser";
    private final String password = "dictuser";
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

    public ResultSet getData() {
        try {
            return statement.executeQuery("select* from dictionarywcs.E_V");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ArrayList<Word> dictionarySearcher(String targetWord, int limit) {
        ArrayList<Word> result = new ArrayList<>();
        String command = String.format("select word, wordDefinition from\n" +
                "dictionarywcs.E_V\n" +
                " where word like '%s%s'\n" +
                " order by word asc\n" +
                " limit %d;", targetWord, "%", limit);
        try {
            ResultSet resultSet = statement.executeQuery(command);
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