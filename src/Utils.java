



import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class Utils {

    static Random random = new Random();

    public static Connection getConnection() {
        final String USER = "postgres";
        final String PASSWORD = "postgres";
        final String URL = "jdbc:postgresql://localhost:5432/zoo";

        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Connection getConnection(int levelOfIsolation) {
        Connection connection = getConnection();
        try {
            connection.setTransactionIsolation(levelOfIsolation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void writeTimeResultsToFile(String path, ArrayList<Long> data) {
        try(FileWriter writer = new FileWriter(path, false))
        {
            for (int i = 0; i < data.size(); i++) {
                writer.write(String.valueOf(data.get(i)));
                writer.append('\n');
            }

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static ArrayList<String> fileDataToArray(String path) {
        ArrayList<String> list = new  ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(path), "UTF-8"))) {
            String sub;
            while ((sub = br.readLine()) != null) {
                list.add(sub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
